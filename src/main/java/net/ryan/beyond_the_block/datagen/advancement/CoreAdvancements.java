package net.ryan.beyond_the_block.datagen.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.function.Consumer;

public class CoreAdvancements {
    public static void generate(Consumer<Advancement> consumer) {

        Advancement root = Advancement.Builder.create()
                .display(
                        new ItemStack(ModItems.RUBY_ITEM),
                        Text.literal("Beyond the Block"),
                        Text.literal("Step beyond vanilla."),
                        new Identifier("minecraft:textures/gui/advancements/backgrounds/stone.png"),
                        AdvancementFrame.TASK,
                        false, false, false
                )
                .criterion("has_item",
                        InventoryChangedCriterion.Conditions.items(ModItems.RUBY_ITEM))
                .build(consumer, BeyondTheBlock.MOD_ID + ":core/beyond_the_block");
    }
}
