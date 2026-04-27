package net.ryan.beyond_the_block.content.enchantment.horses;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.content.registry.ModItems;

public abstract class HorseEnchantment extends Enchantment {

    protected HorseEnchantment(Rarity rarity) {
        super(rarity, EnchantmentTarget.BREAKABLE, new EquipmentSlot[]{});
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isOf(ModItems.HORSESHOES);
    }
}
