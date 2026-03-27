package net.ryan.beyond_the_block.client.render.light;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.item.armour.ModArmourMaterials;
import net.ryan.beyond_the_block.content.item.ModItems;

public class DynamicLightRegistrar {
    public static void register(){
        registerDynamicLights();
    }
    private static void registerDynamicLights() {
        DynamicLightHandlers.registerDynamicLightHandler(EntityType.PLAYER, player -> {
            int maxLight = 0;

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR && isAmberineArmor(player.getEquippedStack(slot))) {
                    maxLight = 15;
                }
            }

            if (isAmberineItem(player.getMainHandStack()) || isAmberineItem(player.getOffHandStack())) {
                maxLight = 15;
            }

            return maxLight;
        });
    }
    private static boolean isAmberineArmor(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ArmorItem armor
                && armor.getMaterial() == ModArmourMaterials.AMBERINE;
    }

    private static boolean isAmberineToolOrWeapon(ItemStack stack) {
        return stack != null && (stack.isOf(ModItems.AMBERINE_SWORD) ||
                stack.isOf(ModItems.AMBERINE_PICKAXE) ||
                stack.isOf(ModItems.AMBERINE_AXE) ||
                stack.isOf(ModItems.AMBERINE_SHOVEL) ||
                stack.isOf(ModItems.AMBERINE_HOE));
    }

    private static boolean isAmberineItem(ItemStack stack) {
        return stack != null && (isAmberineArmor(stack) || isAmberineToolOrWeapon(stack)
                || stack.isOf(ModBlocks.AMBERINE_BLOCK.asItem()) || stack.isOf(ModItems.AMBERINE_ITEM));
    }
}
