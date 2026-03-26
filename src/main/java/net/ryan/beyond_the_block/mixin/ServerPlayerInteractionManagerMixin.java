package net.ryan.beyond_the_block.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Unique
    private BlockState cachedState;
    @Unique
    private BlockEntity cachedBlockEntity;
    @Unique
    private BlockPos cachedPos;
    @Unique
    private ItemStack cachedTool;

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
            if (level > 0 && block instanceof CropBlock) {
                BlockPos above = pos.up();
                BlockState aboveState = world.getBlockState(above);
                if (aboveState.getBlock() instanceof CropBlock crop) {
                    int age = aboveState.get(CropBlock.AGE);
                    int maxAge = crop.getMaxAge();

                    if (world.random.nextFloat() < 0.3f) {
                        if (age < maxAge) {
                            world.setBlockState(above, aboveState.with(CropBlock.AGE, maxAge), 3);
                        } else {
                            world.spawnEntity(new ItemEntity(world, above.getX() + 0.5, above.getY() + 0.5, above.getZ() + 0.5, new ItemStack(aboveState.getBlock().asItem())));
                        }
                        cir.setReturnValue(ActionResult.SUCCESS);
                        return;
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
        this.cachedTool = player.getMainHandStack().copy(); // In case it's modified later
    }


    @Inject(method = "tryBreakBlock", at = @At("TAIL"))
    private void onBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValue()) return; // Only proceed if block was broken

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
         if(darkDigLevel > 0 && isDarkArea(world, player)){
            //Speed up digging by reducing block break time or adding extra drops
            applyDarkDigEffect(world, cachedState, cachedBlockEntity, cachedPos, player, darkDigLevel);
        }

        // ---------------- Garden's Bounty (Hoe) ----------------
          if (bountyLevel > 0 && block instanceof CropBlock) {
            enhanceCropDrops(world, cachedState, pos, bountyLevel, true);
        }

        // ---------------- Night Cultivation (Hoe) ----------------
         if (nightLevel > 0 && isDarkArea(world, player)) {
            enhanceCropDrops(world, cachedState, pos, nightLevel, false);
        }

    }

    private void applyDarkDigEffect(ServerWorld world, BlockState state, BlockEntity blockEntity, BlockPos pos, PlayerEntity player, int level) {
        // Speed up digging (reduce time to break the block) or apply special effects
        if (state.isIn(BlockTags.SHOVEL_MINEABLE)) {
            // Get normal drops
            List<ItemStack> drops = Block.getDroppedStacks(state, world, pos, blockEntity, player, player.getMainHandStack());

            // Double drops
            for (int i = 0; i < level; i++) {
                for (ItemStack drop : drops) {
                    Block.dropStack(world, pos, drop.copy());
                }
            }

            // Small chance for diamond drop
            if (world.random.nextFloat() < 0.05f * level) {
                Block.dropStack(world, pos, new ItemStack(Items.DIAMOND));
            }
        }
    }

    private void applyShadowMiningEffect(ServerWorld world, BlockState state, BlockEntity blockEntity, BlockPos pos, PlayerEntity player, int level) {
        // Default behavior: duplicate regular drops
        List<ItemStack> drops = Block.getDroppedStacks(state, world, pos, blockEntity, player, player.getMainHandStack());
        for (int i = 0; i < level; i++) {
            for (ItemStack drop : drops) {
                Block.dropStack(world, pos, drop.copy());
            }
        }
    }

    private void enhanceCropDrops(ServerWorld world, BlockState state, BlockPos pos, int level, boolean isBounty) {
        List<ItemStack> drops = Block.getDroppedStacks(state, world, pos, null);
        List<ItemStack> enhanced = new ArrayList<>(drops);

        for (int i = 0; i < level; i++) {
            for (ItemStack drop : drops) {
                enhanced.add(drop.copy());
            }
        }

        if (level >= 2 && isBounty) {
            enhanced.add(new ItemStack(world.random.nextBoolean() ? Items.GOLDEN_CARROT : Items.GOLDEN_APPLE));
        }

        for (ItemStack drop : enhanced) {
            Block.dropStack(world, pos, drop);
        }
    }

}
