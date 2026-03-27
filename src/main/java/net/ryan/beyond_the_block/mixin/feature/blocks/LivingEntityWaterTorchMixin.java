package net.ryan.beyond_the_block.mixin.feature.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityWaterTorchMixin extends Entity {
    public LivingEntityWaterTorchMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void beyond_the_block$slowDrowningNearWaterTorch(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.getWorld().isClient) return;
        if (!(self instanceof PlayerEntity)) return;
        if (!self.isSubmergedIn(FluidTags.WATER)) return;
        if (self.getAir() >= self.getMaxAir()) return;
        if (!beyond_the_block$isNearWaterTorch(self.getBlockPos(), 4)) return;


        // Stronger, noticeable effect:
        if (self.age % 2 == 0) {
            self.setAir(Math.min(self.getAir() + 1, self.getMaxAir()));
        }

    }

    @Unique
    private boolean beyond_the_block$isNearWaterTorch(BlockPos center, int radius) {
        for (BlockPos pos : BlockPos.iterateOutwards(center, radius, radius, radius)) {
            BlockState state = this.getWorld().getBlockState(pos);
            if (state.isOf(ModBlocks.WATER_TORCH_BLOCK) || state.isOf(ModBlocks.WALL_WATER_TORCH_BLOCK)) {
                return true;
            }
        }
        return false;
    }
}