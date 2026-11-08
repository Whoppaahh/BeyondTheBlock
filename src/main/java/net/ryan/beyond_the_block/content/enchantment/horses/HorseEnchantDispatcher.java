package net.ryan.beyond_the_block.content.enchantment.horses;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.content.enchantment.horses.enchantments.FleetHoovesEnchantment;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;
import net.ryan.beyond_the_block.feature.horses.HorseEnchantHooks;
import net.ryan.beyond_the_block.feature.horses.HorseEquipmentAccessor;
import net.ryan.beyond_the_block.utils.accessors.EnchantmentCacheAccessor;

import java.util.Map;

public class HorseEnchantDispatcher {

    public static Map<Enchantment, Integer> getEnchantments(AbstractHorseEntity horse) {
        if (!(horse instanceof HorseEquipmentAccessor accessor)) {
            return Map.of();
        }

        ItemStack shoes = accessor.btb$getHorseshoes();
        if (shoes.isEmpty()) {
            return Map.of();
        }

        return EnchantmentHelper.get(shoes);
    }

    public static void tick(AbstractHorseEntity horse) {
        Map<Enchantment, Integer> enchantments = getEnchantments(horse);
        
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();
            
            if (enchantment instanceof HorseEnchantHooks hooks) {
                hooks.onTick(horse, level);
            }
        }
    }
}
