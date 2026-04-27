package net.ryan.beyond_the_block.feature.horses;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;

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
        for (var entry : getEnchantments(horse).entrySet()) {
            if (entry.getKey() instanceof HorseEnchantHooks hooks) {
                hooks.onTick(horse, entry.getValue());
            }
        }
    }
}
