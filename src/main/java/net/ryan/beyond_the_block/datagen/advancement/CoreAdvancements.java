package net.ryan.beyond_the_block.datagen.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.function.Consumer;

public final class CoreAdvancements {

    private CoreAdvancements() {
    }

    public static Advancement generateRoot(Consumer<Advancement> consumer) {
        return Advancement.Builder.create()
                .display(
                        new ItemStack(ModItems.RUBY_ITEM),
                        Text.translatable("advancements.beyond_the_block.core.beyond_the_block.title"),
                        Text.translatable("advancements.beyond_the_block.core.beyond_the_block.description"),
                        new Identifier("minecraft:textures/gui/advancements/backgrounds/stone.png"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        false
                )
                .criterion(
                        "has_mod_item",
                        InventoryChangedCriterion.Conditions.items(
                                ModItems.RUBY_ITEM,
                                ModItems.MIRANITE_ITEM,
                                ModItems.AMBERINE_ITEM,
                                ModItems.AZUROS_ITEM,
                                ModItems.CHROMITE_ITEM,
                                ModItems.ROSETTE_ITEM,
                                ModItems.INDIGRA_ITEM,
                                ModItems.NOCTURNITE_ITEM,
                                ModItems.XIRION_ITEM
                        )
                )
                .build(consumer, BeyondTheBlock.MOD_ID + ":core/beyond_the_block");
    }
}