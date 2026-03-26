package net.ryan.beyond_the_block.content.enchantment.Armour.leggings;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class FrozenMomentumEnchantment extends Enchantment {
    public FrozenMomentumEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armour && armour.getSlotType() == EquipmentSlot.LEGS || stack.isOf(Items.BOOK);
    }

    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        World world = user.getWorld();
        BlockPos pos = user.getBlockPos();

        Biome biome = user.getWorld().getBiome(pos).value();
        float temperature = biome.getTemperature();

        if (!world.isClient && temperature < 0.2F) {
            // Considered a cold biome (like snowy tundra or ice spikes)
            user.addVelocity(0, 0.3 + 0.1 * level, 0); // Launch upward a bit
            user.velocityModified = true;
        }
        super.onUserDamaged(user, attacker, level);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
