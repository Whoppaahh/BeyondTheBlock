package net.ryan.beyond_the_block.datagen.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.function.Consumer;

public final class OreAdvancements {

    private OreAdvancements() {
    }

    public static Advancement generate(Advancement parent, Consumer<Advancement> consumer) {
        Advancement discoverRuby = Advancement.Builder.create()
                .parent(parent)
                .display(
                        new ItemStack(ModBlocks.RUBY_ORE),
                        Text.translatable("advancements.beyond_the_block.ores.discover_ruby.title"),
                        Text.translatable("advancements.beyond_the_block.ores.discover_ruby.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(
                        "has_ruby_related",
                        InventoryChangedCriterion.Conditions.items(
                                ModItems.RUBY_ITEM,
                                ModBlocks.RUBY_ORE,
                                ModBlocks.DEEPSLATE_RUBY_ORE
                        )
                )
                .build(consumer, BeyondTheBlock.MOD_ID + ":ores/discover_ruby");

        Advancement discoverMiranite = Advancement.Builder.create()
                .parent(discoverRuby)
                .display(
                        new ItemStack(ModBlocks.MIRANITE_ORE),
                        Text.translatable("advancements.beyond_the_block.ores.discover_miranite.title"),
                        Text.translatable("advancements.beyond_the_block.ores.discover_miranite.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(
                        "has_miranite_family",
                        InventoryChangedCriterion.Conditions.items(
                                ModItems.RAW_MIRANITE_ITEM,
                                ModItems.MIRANITE_ITEM,
                                ModBlocks.MIRANITE_ORE,
                                ModBlocks.DEEPSLATE_MIRANITE_ORE,
                                ModBlocks.NETHER_MIRANITE_ORE,
                                ModBlocks.END_MIRANITE_ORE
                        )
                )
                .build(consumer, BeyondTheBlock.MOD_ID + ":ores/discover_miranite");

        Advancement refinedTastes = Advancement.Builder.create()
                .parent(discoverMiranite)
                .display(
                        new ItemStack(ModItems.MIRANITE_ITEM),
                        Text.translatable("advancements.beyond_the_block.ores.refined_tastes.title"),
                        Text.translatable("advancements.beyond_the_block.ores.refined_tastes.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion(
                        "has_refined_material",
                        InventoryChangedCriterion.Conditions.items(
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
                .build(consumer, BeyondTheBlock.MOD_ID + ":ores/refined_tastes");

        Advancement preciousStorage = Advancement.Builder.create()
                .parent(refinedTastes)
                .display(
                        new ItemStack(ModBlocks.RUBY_BLOCK),
                        Text.translatable("advancements.beyond_the_block.ores.precious_storage.title"),
                        Text.translatable("advancements.beyond_the_block.ores.precious_storage.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(
                        "has_storage_block",
                        InventoryChangedCriterion.Conditions.items(
                                ModBlocks.RUBY_BLOCK,
                                ModBlocks.MIRANITE_BLOCK,
                                ModBlocks.CHROMITE_BLOCK,
                                ModBlocks.NOCTURNITE_BLOCK,
                                ModBlocks.AMBERINE_BLOCK,
                                ModBlocks.ROSETTE_BLOCK,
                                ModBlocks.AZUROS_BLOCK,
                                ModBlocks.INDIGRA_BLOCK,
                                ModBlocks.XIRION_BLOCK
                        )
                )
                .build(consumer, BeyondTheBlock.MOD_ID + ":ores/precious_storage");

        return Advancement.Builder.create()
                .parent(preciousStorage)
                .display(
                        new ItemStack(ModBlocks.END_MIRANITE_ORE),
                        Text.translatable("advancements.beyond_the_block.ores.distant_deposits.title"),
                        Text.translatable("advancements.beyond_the_block.ores.distant_deposits.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion(
                        "has_nether_or_end_ore",
                        InventoryChangedCriterion.Conditions.items(
                                ModBlocks.NETHER_MIRANITE_ORE,
                                ModBlocks.END_MIRANITE_ORE,
                                ModBlocks.NETHER_CHROMITE_ORE,
                                ModBlocks.END_CHROMITE_ORE,
                                ModBlocks.NETHER_NOCTURNITE_ORE,
                                ModBlocks.END_NOCTURNITE_ORE,
                                ModBlocks.NETHER_ROSETTE_ORE,
                                ModBlocks.END_ROSETTE_ORE,
                                ModBlocks.NETHER_AZUROS_ORE,
                                ModBlocks.END_AZUROS_ORE,
                                ModBlocks.NETHER_INDIGRA_ORE,
                                ModBlocks.END_INDIGRA_ORE,
                                ModBlocks.NETHER_XIRION_ORE,
                                ModBlocks.END_XIRION_ORE
                        )
                )
                .build(consumer, BeyondTheBlock.MOD_ID + ":ores/distant_deposits");
    }
}