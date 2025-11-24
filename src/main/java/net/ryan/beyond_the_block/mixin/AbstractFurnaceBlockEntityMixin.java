package net.ryan.beyond_the_block.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.utils.Accessors.FurnaceAccessor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin implements FurnaceAccessor {

    @Shadow int cookTime;
    @Shadow int cookTimeTotal;
    @Shadow int burnTime;

    @Override
    public int btb$getCookTime() {
        return cookTime;
    }

    @Override
    public int btb$getCookTimeTotal() {
        return cookTimeTotal;
    }

    @Override
    public boolean btb$getIsBurning() {
        return burnTime > 0;
    }

    @Override
    public void btb$setCookTime(int cookTime){
        this.cookTime = cookTime;
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;cookTime:I",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            ))
    private static void netherFurnaceBoost(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        if (world != null && world.getRegistryKey() == World.NETHER) {
            // Only boost if it's burning & actively cooking
            if(blockEntity instanceof FurnaceAccessor accessor){
                if (accessor.btb$getIsBurning() && accessor.btb$getCookTime() > 0) {
                    accessor.btb$setCookTime(Math.min(accessor.btb$getCookTime() + 8, accessor.btb$getCookTimeTotal()));
                }
            }

        }
    }
}

