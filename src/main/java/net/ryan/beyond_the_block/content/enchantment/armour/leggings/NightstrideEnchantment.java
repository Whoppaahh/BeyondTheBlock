package net.ryan.beyond_the_block.content.enchantment.armour.leggings;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class NightstrideEnchantment extends Enchantment {

    public NightstrideEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slots) {
        super(weight, type, slots);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armour && armour.getSlotType() == EquipmentSlot.LEGS || stack.isOf(Items.BOOK);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.SWIFT_SNEAK; // Example conflict
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }


}
