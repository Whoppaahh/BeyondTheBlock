package net.ryan.beyond_the_block.mixin.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.utils.helpers.CobwebDecayScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Inject(
            method = "scheduledTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void btw_handleCobwebDecay(
            BlockState state,
            ServerWorld world,
            BlockPos pos,
            Random random,
            CallbackInfo ci
    ) {
        if (!state.isOf(Blocks.COBWEB)) return;

        int light = world.getLightLevel(pos);
        float chance = Configs.server().features.webs.baseDecayChance;

        if (light > 11)
            chance += Configs.server().features.webs.lightDecayBonus;
        if (light < 6)
            chance -= Configs.server().features.webs.darknessDecayReduction;

        chance = MathHelper.clamp(chance, 0.05f, 0.95f);

        if (random.nextFloat() < chance) {
            world.breakBlock(pos, false);
            ci.cancel();
            return;
        }

        CobwebDecayScheduler.schedule(world, pos);
        ci.cancel();
    }

    @Unique
    private static final VoxelShape HORSE_LEAF_TOP = Block.createCuboidShape(
            0.0, 15.0, 0.0,
            16.0, 16.0, 16.0
    );

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void beyond$horseLeavesCollision(
            BlockState state,
            BlockView world,
            BlockPos pos,
            ShapeContext context,
            CallbackInfoReturnable<VoxelShape> cir
    ) {
        if (!(state.getBlock() instanceof LeavesBlock)) return;

        Entity entity = context instanceof EntityShapeContext esc ? esc.getEntity() : null;
        if (!(entity instanceof AbstractHorseEntity horse)) return;
        if (!horse.hasPassengers()) return;

        cir.setReturnValue(HORSE_LEAF_TOP);
    }

    @Inject(method = "getCameraCollisionShape", at = @At("HEAD"), cancellable = true)
    private void beyond$noLeafCameraCollisionForMountedHorse(
            BlockState state,
            BlockView world,
            BlockPos pos,
            ShapeContext context,
            CallbackInfoReturnable<VoxelShape> cir
    ) {
        if (!(state.getBlock() instanceof LeavesBlock)) return;

        Entity entity = context instanceof net.minecraft.block.EntityShapeContext esc ? esc.getEntity() : null;
        if (!(entity instanceof PlayerEntity player)) return;
        if (!(player.getVehicle() instanceof AbstractHorseEntity)) return;

        cir.setReturnValue(VoxelShapes.empty());
    }
}


