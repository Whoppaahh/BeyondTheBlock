package net.ryan.beyond_the_block.content.enchantment.Tools.swords;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;

public class TemporalSliceEnchantment extends Enchantment {
    public TemporalSliceEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || stack.isOf(Items.BOOK);
    }

    //Logic handled by EnchantmentHelper and EnchantmentEventListener
    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {

    }

}
