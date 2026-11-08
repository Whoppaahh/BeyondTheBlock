package net.ryan.beyond_the_block.utils.accessors;

import net.minecraft.enchantment.Enchantment;

public interface EnchantmentCacheAccessor {
    int btb$getCachedLevel(Enchantment enchantment);
    void btb$updateEnchantmentCache();
}
