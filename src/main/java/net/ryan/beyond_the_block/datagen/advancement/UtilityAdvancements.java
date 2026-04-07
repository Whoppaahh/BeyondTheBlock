package net.ryan.beyond_the_block.datagen.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.function.Consumer;

public final class UtilityAdvancements {

    private UtilityAdvancements() {
    }

    public static Advancement generate(Advancement parent, Consumer<Advancement> consumer) {
        return Advancement.Builder.create()
                .parent(parent)
                .display(
                        new ItemStack(ModBlocks.PEDESTAL_BLOCK.asItem()),
                        Text.translatable("advancements.beyond_the_block.utility.useful_construction.title"),
                        Text.translatable("advancements.beyond_the_block.utility.useful_construction.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(
                        "has_utility_block",
                        InventoryChangedCriterion.Conditions.items(
                                ModBlocks.PEDESTAL_BLOCK,
                                ModBlocks.LAVA_LAMP_BLOCK,
                                ModBlocks.GEM_BLOCK,
                                ModBlocks.PLAYER_VAULT_BLOCK,
                                ModBlocks.INFI_FURNACE_BLOCK,
                                ModBlocks.DECRAFTER_BLOCK,
                                ModBlocks.SPEED_RAIL_BLOCK
                        )
                )
                .build(consumer, BeyondTheBlock.MOD_ID + ":utility/useful_construction");
    }
}