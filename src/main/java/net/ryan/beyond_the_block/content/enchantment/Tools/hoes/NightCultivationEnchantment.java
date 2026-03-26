package net.ryan.beyond_the_block.content.enchantment.Tools.hoes;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class NightCultivationEnchantment extends Enchantment {
    public NightCultivationEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof HoeItem || stack.isOf(Items.BOOK);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {


        super.onTargetDamaged(user, target, level);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
