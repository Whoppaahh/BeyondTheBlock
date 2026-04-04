package net.ryan.beyond_the_block.datagen.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.function.Consumer;

public class OreAdvancements {

    public static void generate(Consumer<Advancement> consumer) {

        Advancement root = Advancement.Builder.create()
                .criterion("dummy",
                        InventoryChangedCriterion.Conditions.items(ModItems.RUBY_ITEM))
                .build(consumer, BeyondTheBlock.MOD_ID + ":core/beyond_the_block");

        Advancement.Builder.create()
                .parent(root)
                .display(
                        new ItemStack(ModItems.MIRANITE_ITEM),
                        Text.literal("Refined Tastes"),
                        Text.literal("Obtain refined material."),
                        null,
                        AdvancementFrame.GOAL,
                        true, true, false
                )
                .criterion("has_miranite",
                        InventoryChangedCriterion.Conditions.items(ModItems.MIRANITE_ITEM))
                .build(consumer, BeyondTheBlock.MOD_ID + ":ores/refined_tastes");
    }
}