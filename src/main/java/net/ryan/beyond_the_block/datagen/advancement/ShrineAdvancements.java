package net.ryan.beyond_the_block.datagen.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.function.Consumer;

public final class ShrineAdvancements {

    private ShrineAdvancements() {
    }

    public static Advancement generate(Advancement parent, Consumer<Advancement> consumer) {
        return Advancement.Builder.create()
                .parent(parent)
                .display(
                        new ItemStack(ModBlocks.SHRINE_CORE_BLOCK.asItem()),
                        Text.translatable("advancements.beyond_the_block.shrines.ancient_arrangements.title"),
                        Text.translatable("advancements.beyond_the_block.shrines.ancient_arrangements.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(
                        "has_shrine_block",
                        InventoryChangedCriterion.Conditions.items(
                                ModBlocks.SHRINE_CORE_BLOCK,
                                ModBlocks.SHRINE_HEADS_BLOCK,
                                ModBlocks.SHRINE_DECOR_BLOCK,
                                ModBlocks.SINGLE_INPUT_BLOCK,
                                ModBlocks.DOUBLE_INPUT_BLOCK
                        )
                )
                .build(consumer, BeyondTheBlock.MOD_ID + ":shrines/ancient_arrangements");
    }
}