package net.ryan.beyond_the_block.content.enchantment.armour.chestplates;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class EchoGuardEnchantment extends Enchantment {
    public EchoGuardEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armour && armour.getSlotType() == EquipmentSlot.CHEST || stack.isOf(Items.BOOK);
    }
    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        if (!(user instanceof PlayerEntity)) return;
        if (user.getRandom().nextFloat() < 0.2F * level) { // 20% chance per level
            user.heal(2.0F * level); // heals 2HP per level
            // You could also apply a status effect instead:
             user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, level - 1, false, false, false));
        }
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
