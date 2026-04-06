package net.ryan.beyond_the_block.content.enchantment.tools.hoes;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.MendingEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class GardensBountyEnchantment extends Enchantment {
    public GardensBountyEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
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

    public boolean canAccept(Enchantment other) {
        return !(other instanceof NightCultivationEnchantment) && super.canAccept(other);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
