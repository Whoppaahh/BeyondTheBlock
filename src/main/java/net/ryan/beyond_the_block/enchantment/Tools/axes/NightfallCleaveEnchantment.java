package net.ryan.beyond_the_block.enchantment.Tools.axes;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class NightfallCleaveEnchantment extends Enchantment {
    public NightfallCleaveEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || stack.isOf(Items.BOOK);
    }
    //Logic handled in Mixin
    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {

        super.onTargetDamaged(user, target, level);
    }

}
