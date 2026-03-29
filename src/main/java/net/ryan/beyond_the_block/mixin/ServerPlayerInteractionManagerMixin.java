package net.ryan.beyond_the_block.mixin;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.content.enchantment.MyEnchantmentHelper;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.utils.ReachHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Shadow
    @Final
    protected ServerPlayerEntity player;
    @Unique
    private BlockState cachedState;
    @Unique
    private BlockEntity cachedBlockEntity;
    @Unique
    private BlockPos cachedPos;
    @Unique
    private ItemStack cachedTool;
    @Unique
    private boolean beyondTheBlock$suppressVanillaDrops;

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void onInteractBlock(ServerPlayerEntity player, World world, ItemStack item, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (hand != Hand.MAIN_HAND) return;

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        ItemStack tool = player.getMainHandStack();


        // ---------------- Fertility (Shovel) ----------------
        if (tool.getItem() instanceof ShovelItem) {
            int level = EnchantmentHelper.getLevel(ModEnchantments.FERTILITY, tool);
            if (level > 0) {
                if (world instanceof ServerWorld serverWorld) {
                    if (state.getBlock() instanceof Fertilizable fertilizable) {
                        if (fertilizable.isFertilizable(world, pos, state, world.isClient)) {
                            BeyondTheBlock.LOGGER.warn("Fertilizable is fertilizable");
                            if (fertilizable.canGrow(world, world.random, pos, state)) {
                                fertilizable.grow(serverWorld, serverWorld.random, pos, state);
                                BeyondTheBlock.LOGGER.info("Growing block");
                                cir.setReturnValue(ActionResult.SUCCESS);
                                return;
                            }
                        } else {
                            BeyondTheBlock.LOGGER.warn("Can't grow anymore");
                            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(state.getBlock().asItem())));
                            cir.setReturnValue(ActionResult.SUCCESS);
                            return;
                        }
                    } else {
                        // if (world.random.nextFloat() < 0.3f) {
                        if (block instanceof PlantBlock) {
                            BeyondTheBlock.LOGGER.warn("Can't grow shovel - " + block.asItem());
                            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(state.getBlock().asItem())));
                            cir.setReturnValue(ActionResult.SUCCESS);
                            return;
                        }
                        //  }
                    }
                }
            }
        }

        // ---------------- Frozen Momentum (Leggings) ----------------
        if (EnchantmentHelper.getLevel(ModEnchantments.FROZEN_MOMENTUM, player.getEquippedStack(EquipmentSlot.LEGS)) > 0) {
            BlockState below = world.getBlockState(player.getBlockPos().down());
            if (below.isOf(Blocks.POWDER_SNOW)) {
                cir.setReturnValue(ActionResult.FAIL); // Cancel sinking
            }
        }
    }

    @Unique
    private boolean isDarkArea(ServerWorld world, ServerPlayerEntity player) {
        return world.isNight() || player.getWorld().getLightLevel(player.getBlockPos()) < 5 || !player.getWorld().getDimension().bedWorks();
    }

    @Inject(method = "tryBreakBlock", at = @At("HEAD"))
    private void cacheBlockData(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntity player = ((ServerPlayerInteractionManagerAccessor) this).getPlayer();
        ServerWorld world = player.getWorld();

        this.cachedState = world.getBlockState(pos);
        this.cachedBlockEntity = world.getBlockEntity(pos);
        this.cachedPos = pos;
        this.cachedTool = player.getMainHandStack().copy();

        this.beyondTheBlock$suppressVanillaDrops = false;

        int shadowMiningLevel = EnchantmentHelper.getLevel(ModEnchantments.SHADOW_MINING, this.cachedTool);
        int darkDigLevel = EnchantmentHelper.getLevel(ModEnchantments.DARK_DIG, this.cachedTool);
        int bountyLevel = EnchantmentHelper.getLevel(ModEnchantments.GARDENS_BOUNTY, this.cachedTool);
        int nightLevel = EnchantmentHelper.getLevel(ModEnchantments.NIGHT_CULTIVATION, this.cachedTool);

        boolean isCrop = this.cachedState.getBlock() instanceof CropBlock;

        if (shadowMiningLevel > 0
                && this.cachedTool.getItem() instanceof PickaxeItem
                && this.cachedState.isIn(BlockTags.PICKAXE_MINEABLE)
                && isDarkArea(world, player)) {
            this.beyondTheBlock$suppressVanillaDrops = true;
        }

        if (darkDigLevel > 0
                && this.cachedState.isIn(BlockTags.SHOVEL_MINEABLE)
                && isDarkArea(world, player)) {
            this.beyondTheBlock$suppressVanillaDrops = true;
        }

        if (bountyLevel > 0 && isCrop) {
            this.beyondTheBlock$suppressVanillaDrops = true;
        }

        if (nightLevel > 0 && isCrop && isDarkArea(world, player)) {
            this.beyondTheBlock$suppressVanillaDrops = true;
        }
    }


    @Inject(method = "tryBreakBlock", at = @At("RETURN"))
    private void onBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) return; // Only proceed if block was broken

        int shadowMiningLevel = EnchantmentHelper.getLevel(ModEnchantments.SHADOW_MINING, cachedTool);
        int darkDigLevel = EnchantmentHelper.getLevel(ModEnchantments.DARK_DIG, cachedTool);
        int bountyLevel = EnchantmentHelper.getLevel(ModEnchantments.GARDENS_BOUNTY, cachedTool);
        int nightLevel = EnchantmentHelper.getLevel(ModEnchantments.NIGHT_CULTIVATION, cachedTool);
        //Return if no enchantments apply
        if (shadowMiningLevel <= 0 && darkDigLevel <= 0 && bountyLevel <= 0 && nightLevel <= 0) return;

        ServerPlayerEntity player = ((ServerPlayerInteractionManagerAccessor) this).getPlayer();
        ServerWorld world = player.getWorld();
        Block block = cachedState.getBlock();

        // ---------------- Shadow Mining (Pickaxe) ----------------
        if (shadowMiningLevel > 0 && cachedTool.getItem() instanceof PickaxeItem && cachedState.isIn(BlockTags.PICKAXE_MINEABLE) && isDarkArea(world, player)) {
            applyShadowMiningEffect(world, cachedState, cachedBlockEntity, cachedPos, player, shadowMiningLevel);
        }

        // ---------------- Dark Dig (Shovel) ----------------
        if (darkDigLevel > 0
                && cachedState.isIn(BlockTags.SHOVEL_MINEABLE)
                && isDarkArea(world, player)) {
            //Speed up digging by reducing block break time or adding extra drops
            applyDarkDigEffect(world, cachedState, cachedBlockEntity, cachedPos, player, darkDigLevel);
        }

        // ---------------- Garden's Bounty (Hoe) ----------------
        if (bountyLevel > 0 && block instanceof CropBlock) {
            enhanceCropDrops(world, cachedState, cachedPos, bountyLevel, true);
        }

        // ---------------- Night Cultivation (Hoe) ----------------
        if (nightLevel > 0
                && block instanceof CropBlock
                && isDarkArea(world, player)) {
            enhanceCropDrops(world, cachedState, cachedPos, nightLevel, false);
        }

    }

    @Redirect(
            method = "tryBreakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V"
            )
    )
    private void beyond_the_block$conditionallySkipAfterBreak(
            Block block,
            World world,
            PlayerEntity player,
            BlockPos pos,
            BlockState state,
            BlockEntity blockEntity,
            ItemStack stack
    ) {
        if (this.beyondTheBlock$suppressVanillaDrops) {
            return;
        }

        block.afterBreak(world, player, pos, state, blockEntity, stack);
    }

    @Inject(method = "tryBreakBlock", at = @At("RETURN"))
    private void clearSuppressionFlagReturn(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        this.beyondTheBlock$suppressVanillaDrops = false;
    }

    @Unique
    private void applyDarkDigEffect(ServerWorld world, BlockState state, BlockEntity blockEntity, BlockPos pos, PlayerEntity player, int level) {
        // Speed up digging (reduce time to break the block) or apply special effects
        if (state.isIn(BlockTags.SHOVEL_MINEABLE)) {
            // Get normal drops
            List<ItemStack> drops = Block.getDroppedStacks(state, world, pos, blockEntity, player, cachedTool);
            List<ItemStack> multipliedDrops = new ArrayList<>(drops);

            for (ItemStack drop : drops) {
                if (drop.isEmpty()) continue;

                int totalCount = drop.getCount() * level;
                int maxCount = drop.getMaxCount();

                while (totalCount > 0) {
                    int splitCount = Math.min(totalCount, maxCount);
                    ItemStack split = drop.copy();
                    split.setCount(splitCount);
                    multipliedDrops.add(split);
                    totalCount -= splitCount;
                }
            }


            // Small chance for diamond drop
            if (world.random.nextFloat() < 0.05f * level) {
                multipliedDrops.add(new ItemStack(Items.DIAMOND));
            }
            MyEnchantmentHelper.giveDropsWithMode(world, pos, player, multipliedDrops);
        }
    }

    @Unique
    private void applyShadowMiningEffect(ServerWorld world, BlockState state, BlockEntity blockEntity, BlockPos pos, PlayerEntity player, int level) {
        List<ItemStack> drops = Block.getDroppedStacks(state, world, pos, blockEntity, player, cachedTool);
        List<ItemStack> multipliedDrops = new ArrayList<>(drops);

        for (ItemStack drop : drops) {
            if (drop.isEmpty()) continue;

            int totalCount = drop.getCount() * level;
            int maxCount = drop.getMaxCount();

            while (totalCount > 0) {
                int splitCount = Math.min(totalCount, maxCount);
                ItemStack split = drop.copy();
                split.setCount(splitCount);
                multipliedDrops.add(split);
                totalCount -= splitCount;
            }
        }

        MyEnchantmentHelper.giveDropsWithMode(world, pos, player, multipliedDrops);
    }

    @Unique
    private void enhanceCropDrops(ServerWorld world, BlockState state, BlockPos pos, int level, boolean isBounty) {
        List<ItemStack> drops = Block.getDroppedStacks(state, world, pos, null, player, cachedTool);
        List<ItemStack> enhanced = new ArrayList<>(drops);

        for (int i = 0; i < level; i++) {
            for (ItemStack drop : drops) {
                enhanced.add(drop.copy());
            }
        }

        if (level >= 2 && isBounty) {
            enhanced.add(new ItemStack(world.random.nextBoolean() ? Items.GOLDEN_CARROT : Items.GOLDEN_APPLE));
        }
        MyEnchantmentHelper.giveDropsWithMode(world, pos, player, enhanced);

    }

    @Redirect(
            method = "processBlockBreakingAction",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D",
                    opcode = Opcodes.GETSTATIC)
    )
    private double beyond_the_block$increaseBreakDistanceLimit() {
        double vanillaSq = ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE;
        ServerPlayerEntity player = ((ServerPlayerInteractionManagerAccessor) this).getPlayer();
        double bonus = ReachHelper.getReachBonusDouble(player.getMainHandStack());

        if (bonus <= 0.0f) {
            return vanillaSq;
        }

        double vanillaReach = (float) Math.sqrt(vanillaSq);
        double boostedReach = vanillaReach + bonus;
        return boostedReach * boostedReach;
    }

}
