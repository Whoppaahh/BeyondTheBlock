package net.ryan.beyond_the_block.mixin.Blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public class FarmLandBlockMixin {
    @Inject(method = "onLandedUpon", at = @At("HEAD"), cancellable = true)
    private void preventTrample(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
        if (!(entity instanceof PlayerEntity player)) return;

        // Get boots
        ItemStack boots = player.getInventory().getArmorStack(0); // 0 = boots slot
        int featherFallingLevel = EnchantmentHelper.getLevel(Enchantments.FEATHER_FALLING, boots);

        if (featherFallingLevel > 0) {
            // Prevent farmland from turning to dirt
            ci.cancel();
        }
    }
}
