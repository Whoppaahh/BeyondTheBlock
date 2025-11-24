package net.ryan.beyond_the_block.enchantment.Armour.boots;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class GroundedResistanceEnchantment extends Enchantment {
    public GroundedResistanceEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armour && armour.getSlotType() == EquipmentSlot.FEET || stack.isOf(Items.BOOK);
    }
    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        if (!user.getWorld().isClient && user.isOnGround()) {
            float reducedDamage = 0.1f * level; // Reduce damage by a factor based on the enchantment level
            user.setHealth(user.getHealth() - reducedDamage);
        }

        super.onUserDamaged(user, attacker, level);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

}
