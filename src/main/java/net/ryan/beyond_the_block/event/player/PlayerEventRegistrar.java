package net.ryan.beyond_the_block.event.player;

import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AxeItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.client.visual.Glowable;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.content.block.PlayerVaultBlock;
import net.ryan.beyond_the_block.content.block.cauldrons.DyedWaterCauldronBlock;
import net.ryan.beyond_the_block.content.blockentity.DyedWaterCauldronBlockEntity;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;
import net.ryan.beyond_the_block.content.enchantment.MyEnchantmentHelper;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.feature.armourstand.ArmourStandEquipmentHandler;
import net.ryan.beyond_the_block.feature.blockconversion.BlockConversionHandler;
import net.ryan.beyond_the_block.feature.cauldrons.ModdedFluidCauldronHandler;
import net.ryan.beyond_the_block.feature.combat.CombatEnchantmentHandler;
import net.ryan.beyond_the_block.feature.restore.RestoreProtectionHandler;
import net.ryan.beyond_the_block.utils.helpers.BetterLadderPlacement;
import net.ryan.beyond_the_block.utils.helpers.DoubleOpenablesHandler;
import net.ryan.beyond_the_block.utils.helpers.RestoreManager;
import org.jetbrains.annotations.Nullable;

public class PlayerEventRegistrar {

    public static void register(){
        registerPlayerEvents();
    }
    private static void registerPlayerEvents() {
        AttackBlockCallback.EVENT.register(PlayerEventRegistrar::onBlockMined);
        AttackEntityCallback.EVENT.register(CombatEnchantmentHandler::onEntityAttacked);
        UseBlockCallback.EVENT.register(PlayerEventRegistrar::onBlockUsed);
        UseItemCallback.EVENT.register(RestoreProtectionHandler::onItemUsed);
        UseEntityCallback.EVENT.register(ArmourStandEquipmentHandler::onEntityUsed);


        PlayerBlockBreakEvents.BEFORE.register(PlayerEventRegistrar::onBlockBreak);
        PlayerBlockBreakEvents.BEFORE.register(PlayerVaultBlock::handleBreak);
        UseBlockCallback.EVENT.register(DoubleOpenablesHandler::onUse);

        UseBlockCallback.EVENT.register(BetterLadderPlacement::onUseBlock);

        UseItemCallback.EVENT.register(BetterLadderPlacement::onUseItem);

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);

            if (!(state.getBlock() instanceof BannerBlock)) return ActionResult.PASS;

            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof Glowable glowable) {
                ItemStack stack = player.getStackInHand(hand);

                if (stack.isOf(Items.GLOW_INK_SAC)) {
                    BeyondTheBlock.LOGGER.info("Glowing");
                    glowable.bt$setGlowing(true);
                    if (!player.getAbilities().creativeMode) stack.decrement(1);
                    return ActionResult.SUCCESS;
                } else if (stack.isOf(Items.INK_SAC)) {
                    glowable.bt$setGlowing(false);
                    if (!player.getAbilities().creativeMode) stack.decrement(1);
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.PASS;
        });
    }

    private static ActionResult onBlockMined(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
        ItemStack stack = player.getStackInHand(hand);
        int miningLevel = EnchantmentHelper.getLevel(ModEnchantments.STONE_BREAKER, stack);
        if (miningLevel > 0) {
            MyEnchantmentHelper.handleInstantMining(player, stack, pos, world, world.getBlockState(pos));
        }
        return ActionResult.PASS;
    }
    private static ActionResult onBlockUsed(
            PlayerEntity player,
            World world,
            Hand hand,
            BlockHitResult hit
    ) {

        BlockState blockState = world.getBlockState(hit.getBlockPos());
        if (blockState.contains(Properties.OPEN)) {
            return ActionResult.PASS;
        }

        BlockPos placePos = hit.getBlockPos().offset(hit.getSide());

        // =========================
        // CLIENT: stop prediction
        // =========================
        if (world.isClient) {
            if (RestoreManager.isProtectedClient(placePos)) {
                player.sendMessage(
                        Text.literal("This area is restoring.")
                                .formatted(Formatting.GRAY),
                        true
                );
                return ActionResult.FAIL; // hard stop
            }
            return ActionResult.PASS; // allow prediction
        }

        // =========================
        // SERVER: stop authority
        // =========================
        if (world instanceof ServerWorld serverWorld) {
            if (RestoreManager.isProtected(serverWorld, placePos)) {
                player.sendMessage(
                        Text.literal("This area is restoring.")
                                .formatted(Formatting.GRAY),
                        true
                );
                return ActionResult.FAIL; // hard stop
            }
        }

        // =========================
        // NORMAL LOGIC CONTINUES
        // =========================

        // Lava → sand logic
        if (world instanceof ServerWorld && world.getFluidState(placePos).isOf(Fluids.LAVA)
                && world.getFluidState(placePos).isStill()) {
            BlockConversionHandler.queueAdjacentSand(world, placePos.toImmutable());
        }

        BlockPos pos = hit.getBlockPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = player.getStackInHand(hand);

        // Water cauldron dye logic
        if (state.isOf(Blocks.WATER_CAULDRON) && stack.getItem() instanceof DyeItem dyeItem) {
            int rgb = DyedWaterCauldronBlock.toRgb(dyeItem.getColor());

            world.setBlockState(
                    pos,
                    ModBlocks.DYED_WATER_CAULDRON_BLOCK.getDefaultState()
                            .with(LeveledCauldronBlock.LEVEL,
                                    state.get(LeveledCauldronBlock.LEVEL)),
                    3
            );

            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof DyedWaterCauldronBlockEntity dyed) {
                dyed.mixDye(rgb);
            }

            if (!player.isCreative()) stack.decrement(1);

            world.playSound(null, pos,
                    SoundEvents.ITEM_DYE_USE,
                    SoundCategory.BLOCKS, 1, 1.1f);

            return ActionResult.SUCCESS;
        }

        ActionResult cauldronResult =
                ModdedFluidCauldronHandler.handleCauldronUse(player, world, hand, hit);
        if (cauldronResult != ActionResult.PASS) {
            return cauldronResult;
        }

        int tillLevel = EnchantmentHelper.getLevel(ModEnchantments.DEEP_TILL, stack);
        if (tillLevel > 0) {
            MyEnchantmentHelper.tillArea(world, pos, tillLevel);
            return ActionResult.SUCCESS;
        }

        int barkskinLevel = EnchantmentHelper.getLevel(ModEnchantments.BARKSKIN, stack);
        if (barkskinLevel > 0) {
            MyEnchantmentHelper.handleTreeStripping(player, stack, pos, world);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private static boolean onBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

        int choppingLevel = EnchantmentHelper.getLevel(ModEnchantments.TIMBER_CUT, stack);
        if (choppingLevel > 0) {
            MyEnchantmentHelper.handleTreeBreaking(player, stack, pos, world);
            return false;
        }

        int flameSweepLevel = EnchantmentHelper.getLevel(ModEnchantments.FLAME_SWEEP, stack);
        if (flameSweepLevel > 0 && stack.getItem() instanceof AxeItem && state.isIn(BlockTags.LOGS_THAT_BURN)) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            Block.dropStack(world, pos, new ItemStack(Items.CHARCOAL, flameSweepLevel));
            if (world instanceof ServerWorld sw) {
                sw.spawnParticles(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, 0.2, 0.2, 0.2, 0.05);
            }
        }
        return true;
    }
}
