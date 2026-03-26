package net.ryan.beyond_the_block.mixin.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    /**
     * Stops Infinity crossbows from consuming normal arrows,
     * but still consumes tipped/spectral arrows.
     */
    @ModifyVariable(
            method = "loadProjectile",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private static ItemStack preventConsumption(ItemStack original, LivingEntity shooter, ItemStack crossbow) {
        int infinityLevel = EnchantmentHelper.getLevel(Enchantments.INFINITY, crossbow);

        // Only prevent consumption for Infinity III and non-normal arrows
        if (infinityLevel == 3 && (original.isOf(Items.TIPPED_ARROW) || original.isOf(Items.SPECTRAL_ARROW))) {
            return original.copy(); // dummy stack so actual inventory is untouched
        }

        return original;
    }
}


