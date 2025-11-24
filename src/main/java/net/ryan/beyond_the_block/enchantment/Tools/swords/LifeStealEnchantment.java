package net.ryan.beyond_the_block.enchantment.Tools.swords;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;

public class LifeStealEnchantment extends Enchantment {
    public LifeStealEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || stack.isOf(Items.BOOK);
    }

    //Logic handled by Mixin
    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {

    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
