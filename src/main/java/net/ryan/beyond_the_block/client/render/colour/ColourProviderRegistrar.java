package net.ryan.beyond_the_block.client.render.colour;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.ryan.beyond_the_block.content.blockentity.DyedWaterCauldronBlockEntity;
import net.ryan.beyond_the_block.content.item.armour.WolfArmourItem;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.content.registry.ModItems;

public class ColourProviderRegistrar {

    public static void register(){
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world != null && pos != null && world.getBlockEntity(pos) instanceof DyedWaterCauldronBlockEntity be) {
                return be.getColor();
            }
            return 0x3F76E4; // default water tint
        }, ModBlocks.DYED_WATER_CAULDRON_BLOCK);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                    if (!(stack.getItem() instanceof WolfArmourItem wolfArmourItem)) {
                        return 0xFFFFFF;
                    }

                    return tintIndex == 0 ? wolfArmourItem.getColor(stack) : 0xFFFFFF;
                },
                ModItems.IRON_WOLF_ARMOUR,
                ModItems.GOLDEN_WOLF_ARMOUR,
                ModItems.DIAMOND_WOLF_ARMOUR);
    }
}
