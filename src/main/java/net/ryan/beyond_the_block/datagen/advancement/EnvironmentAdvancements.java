package net.ryan.beyond_the_block.datagen.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ChangedDimensionCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.function.Consumer;

public final class EnvironmentAdvancements {

    private EnvironmentAdvancements() {
    }

    public static Advancement generate(Advancement parent, Consumer<Advancement> consumer) {
        Advancement compressedSponge = Advancement.Builder.create()
                .parent(parent)
                .display(
                        new ItemStack(ModBlocks.COMPRESSED_SPONGE.asItem()),
                        Text.translatable("advancements.beyond_the_block.environment.compressed_sponge.title"),
                        Text.translatable("advancements.beyond_the_block.environment.compressed_sponge.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(
                        "has_compressed_sponge",
                        InventoryChangedCriterion.Conditions.items(ModBlocks.COMPRESSED_SPONGE)
                )
                .build(consumer, BeyondTheBlock.MOD_ID + ":environment/compressed_sponge");

        return Advancement.Builder.create()
                .parent(compressedSponge)
                .display(
                        new ItemStack(ModBlocks.WET_COMPRESSED_SPONGE.asItem()),
                        Text.translatable("advancements.beyond_the_block.environment.dry_sponge_in_nether.title"),
                        Text.translatable("advancements.beyond_the_block.environment.dry_sponge_in_nether.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        true
                )
                .criterion(
                        "entered_nether",
                        ChangedDimensionCriterion.Conditions.to(World.NETHER)
                )
                .criterion(
                        "has_wet_sponge",
                        InventoryChangedCriterion.Conditions.items(
                                ModBlocks.WET_COMPRESSED_SPONGE,
                                ModBlocks.WET_DOUBLE_COMPRESSED_SPONGE,
                                ModBlocks.WET_TRIPLE_COMPRESSED_SPONGE
                        )
                )
                .requirements(new String[][]{
                        {"entered_nether", "has_wet_sponge"}
                })
                .build(consumer, BeyondTheBlock.MOD_ID + ":environment/dry_sponge_in_nether");
    }
}