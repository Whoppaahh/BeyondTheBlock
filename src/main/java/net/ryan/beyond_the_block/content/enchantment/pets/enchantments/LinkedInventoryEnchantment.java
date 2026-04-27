package net.ryan.beyond_the_block.content.enchantment.pets.enchantments;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantHooks;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantment;

public class LinkedInventoryEnchantment extends PetEnchantment implements PetEnchantHooks {

    public LinkedInventoryEnchantment() {
        super(Rarity.RARE);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean onItemPickup(LivingEntity pet, PlayerEntity owner, ItemStack stack, int level) {
        if (stack.isEmpty()) return false;

        ItemStack before = stack.copy();
        ItemStack remaining = stack.copy();

        boolean inserted = owner.getInventory().insertStack(remaining);

        if (!inserted && remaining.getCount() == before.getCount()) {
            return false;
        }

        stack.setCount(remaining.getCount());
        return true;
    }
}