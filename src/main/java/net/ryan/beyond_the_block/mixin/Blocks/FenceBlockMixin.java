package net.ryan.beyond_the_block.mixin.Blocks;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(HorizontalConnectingBlock.class)
public abstract class FenceBlockMixin extends Block {
    public FenceBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getOutlineShape", at = @At("RETURN"), cancellable = true)
    private void lowerFenceOutline(BlockState state, net.minecraft.world.BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (!(state.getBlock() instanceof FenceBlock) && !(state.getBlock() instanceof WallBlock)) return;

        if (world.getBlockState(pos.down()).isOf(Blocks.DIRT_PATH)) {
            VoxelShape original = cir.getReturnValue();
            cir.setReturnValue(lowerShape(original, 0.0625));
        }
    }

    @Inject(method = "getCollisionShape", at = @At("RETURN"), cancellable = true)
    private void lowerFenceCollision(BlockState state, net.minecraft.world.BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (!(state.getBlock() instanceof FenceBlock) && !(state.getBlock() instanceof WallBlock)) return;

        if (world.getBlockState(pos.down()).isOf(Blocks.DIRT_PATH)) {
            VoxelShape original = cir.getReturnValue();
            cir.setReturnValue(lowerShape(original, 0.0625));
        }
    }

    private VoxelShape lowerShape(VoxelShape original, double amount) {
        VoxelShape lowered = VoxelShapes.empty();
        for (var box : original.getBoundingBoxes()) {
            lowered = VoxelShapes.union(lowered, VoxelShapes.cuboid(
                    box.minX, box.minY - amount, box.minZ,
                    box.maxX, box.maxY - amount, box.maxZ
            ));
        }
        return lowered;
    }
}


