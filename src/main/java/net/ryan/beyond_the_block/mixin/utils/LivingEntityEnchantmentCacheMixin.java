package net.ryan.beyond_the_block.mixin.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;
import net.ryan.beyond_the_block.feature.horses.HorseEquipmentAccessor;
import net.ryan.beyond_the_block.utils.accessors.EnchantmentCacheAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityEnchantmentCacheMixin implements EnchantmentCacheAccessor {

    @Unique
    private final Map<Enchantment, Integer> btb$enchantmentCache = new HashMap<>();

    @Unique
    private boolean btb$cacheInitialized = false;

    @Override
    public int btb$getCachedLevel(Enchantment enchantment) {
        if (!btb$cacheInitialized) {
            btb$updateEnchantmentCache();
        }
        return btb$enchantmentCache.getOrDefault(enchantment, 0);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void btb$tickCache(CallbackInfo ci) {
        if (!Configs.server().features.performance.enableEnchantmentCaching) return;
        
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.age % 20 == 0) {
            btb$updateEnchantmentCache();
        }
    }

    @Override
    public void btb$updateEnchantmentCache() {
        LivingEntity self = (LivingEntity) (Object) this;
        
        // Optimize for players/mobs with standard armor
        btb$enchantmentCache.put(ModEnchantments.GRACEFUL_MOVEMENT, EnchantmentHelper.getEquipmentLevel(ModEnchantments.GRACEFUL_MOVEMENT, self));
        btb$enchantmentCache.put(ModEnchantments.MIND_WARD, EnchantmentHelper.getEquipmentLevel(ModEnchantments.MIND_WARD, self));
        btb$enchantmentCache.put(ModEnchantments.FROZEN_MOMENTUM, EnchantmentHelper.getEquipmentLevel(ModEnchantments.FROZEN_MOMENTUM, self));
        btb$enchantmentCache.put(ModEnchantments.NIGHT_STRIDE, EnchantmentHelper.getEquipmentLevel(ModEnchantments.NIGHT_STRIDE, self));
        btb$enchantmentCache.put(ModEnchantments.IRON_CLAD_VISION, EnchantmentHelper.getEquipmentLevel(ModEnchantments.IRON_CLAD_VISION, self));
        btb$enchantmentCache.put(ModEnchantments.SHADOWS_VEIL, EnchantmentHelper.getEquipmentLevel(ModEnchantments.SHADOWS_VEIL, self));
        
        // Horse specific if it's a horse
        if (self instanceof AbstractHorseEntity horse && horse instanceof HorseEquipmentAccessor accessor) {
            ItemStack shoes = accessor.btb$getHorseshoes();
            if (!shoes.isEmpty()) {
                btb$enchantmentCache.put(ModEnchantments.HORSE_FLEET_HOOVES, EnchantmentHelper.getLevel(ModEnchantments.HORSE_FLEET_HOOVES, shoes));
            } else {
                btb$enchantmentCache.put(ModEnchantments.HORSE_FLEET_HOOVES, 0);
            }
        }

        btb$cacheInitialized = true;
    }
}
