package net.ryan.beyond_the_block.content.item.Armour;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Map;

public class ModArmourItem extends ArmorItem {
    private static final Map<ArmorMaterial, StatusEffectInstance> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, StatusEffectInstance>())
                    .put(ModArmourMaterials.RUBY,
                            new StatusEffectInstance(StatusEffects.LUCK, Integer.MAX_VALUE, 1, true, false, false)).build();


    public ModArmourItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!world.isClient()) {
            if(entity instanceof PlayerEntity player) {

                if(stack.getItem() instanceof ArmorItem && hasAmberineEquipped(player)){
                    if ((world.isRaining() || world.isThundering()) && world instanceof ServerWorld serverWorld) {
                        serverWorld.setWeather(24000, 0, false, false);
                    }
                }

                if(hasFullSuitOfArmorOn(player)) {
                    evaluateArmorEffects(player);
                }else{
                    player.removeStatusEffect(MATERIAL_TO_EFFECT_MAP.get(ModArmourMaterials.RUBY).getEffectType());
                }
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void evaluateArmorEffects(PlayerEntity player) {
        for (Map.Entry<ArmorMaterial, StatusEffectInstance> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            StatusEffectInstance mapStatusEffect = entry.getValue();

            if(hasCorrectArmorOn(mapArmorMaterial, player)) {
                addStatusEffectForMaterial(player, mapArmorMaterial, mapStatusEffect);
            }else {
                // Remove the effect if the armor is incomplete
                player.removeStatusEffect(mapStatusEffect.getEffectType());
            }
        }
    }

    private void addStatusEffectForMaterial(PlayerEntity player, ArmorMaterial mapArmorMaterial, StatusEffectInstance mapStatusEffect) {
        boolean hasPlayerEffect = player.hasStatusEffect(mapStatusEffect.getEffectType());

        if(hasCorrectArmorOn(mapArmorMaterial, player) && !hasPlayerEffect) {
            player.addStatusEffect(new StatusEffectInstance(mapStatusEffect.getEffectType(),
                    mapStatusEffect.getDuration(),
                    mapStatusEffect.getAmplifier(),
                    mapStatusEffect.isAmbient(),
                    mapStatusEffect.shouldShowParticles(),
                    mapStatusEffect.shouldShowIcon()));

            // if(new Random().nextFloat() > 0.6f) { // 40% of damaging the armor! Possibly!
            //     player.getInventory().damageArmor(DamageSource.MAGIC, 1f, new int[]{0, 1, 2, 3});
            // }
        }
    }

    private boolean hasFullSuitOfArmorOn(PlayerEntity player) {
        for (int i = 0; i < 4; i++) {
            ItemStack stack = player.getInventory().getArmorStack(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof ArmorItem)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasCorrectArmorOn(ArmorMaterial material, PlayerEntity player) {
        for (int i = 0; i < 4; i++) {
            ItemStack stack = player.getInventory().getArmorStack(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof ArmorItem armorItem) || armorItem.getMaterial() != material) {
                return false;
            }
        }
        return true;
    }
    private boolean hasAmberineEquipped(PlayerEntity player) {
        for (int i = 0; i < 4; i++) {
            ItemStack stack = player.getInventory().getArmorStack(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem armorItem) {
                if (armorItem.getMaterial() == ModArmourMaterials.AMBERINE) {
                    return true;
                }
            }
        }
        return false;
    }
}
