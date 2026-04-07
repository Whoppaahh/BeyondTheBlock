package net.ryan.beyond_the_block.datagen.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.function.Consumer;

public final class ToolAdvancements {

    private ToolAdvancements() {
    }

    public static Advancement generate(Advancement parent, Consumer<Advancement> consumer) {
        Advancement forgeATool = Advancement.Builder.create()
                .parent(parent)
                .display(
                        new ItemStack(ModItems.RUBY_PICKAXE),
                        Text.translatable("advancements.beyond_the_block.tools.forge_a_tool.title"),
                        Text.translatable("advancements.beyond_the_block.tools.forge_a_tool.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(
                        "has_first_tool",
                        InventoryChangedCriterion.Conditions.items(
                                ModItems.RUBY_PICKAXE,
                                ModItems.RUBY_AXE,
                                ModItems.RUBY_SHOVEL,
                                ModItems.RUBY_HOE,
                                ModItems.RUBY_SWORD,

                                ModItems.AMBERINE_PICKAXE,
                                ModItems.AMBERINE_AXE,
                                ModItems.AMBERINE_SHOVEL,
                                ModItems.AMBERINE_HOE,
                                ModItems.AMBERINE_SWORD,

                                ModItems.AZUROS_PICKAXE,
                                ModItems.AZUROS_AXE,
                                ModItems.AZUROS_SHOVEL,
                                ModItems.AZUROS_HOE,
                                ModItems.AZUROS_SWORD,

                                ModItems.CHROMITE_PICKAXE,
                                ModItems.CHROMITE_AXE,
                                ModItems.CHROMITE_SHOVEL,
                                ModItems.CHROMITE_HOE,
                                ModItems.CHROMITE_SWORD,

                                ModItems.MIRANITE_PICKAXE,
                                ModItems.MIRANITE_AXE,
                                ModItems.MIRANITE_SHOVEL,
                                ModItems.MIRANITE_HOE,
                                ModItems.MIRANITE_SWORD,

                                ModItems.ROSETTE_PICKAXE,
                                ModItems.ROSETTE_AXE,
                                ModItems.ROSETTE_SHOVEL,
                                ModItems.ROSETTE_HOE,
                                ModItems.ROSETTE_SWORD,

                                ModItems.XIRION_PICKAXE,
                                ModItems.XIRION_AXE,
                                ModItems.XIRION_SHOVEL,
                                ModItems.XIRION_HOE,
                                ModItems.XIRION_SWORD
                        )
                )
                .build(consumer, BeyondTheBlock.MOD_ID + ":tools/forge_a_tool");

        return Advancement.Builder.create()
                .parent(forgeATool)
                .display(
                        new ItemStack(ModItems.XIRION_AXE),
                        Text.translatable("advancements.beyond_the_block.tools.properly_equipped.title"),
                        Text.translatable("advancements.beyond_the_block.tools.properly_equipped.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion(
                        "has_any_mod_tool",
                        InventoryChangedCriterion.Conditions.items(
                                ModItems.RUBY_SWORD,
                                ModItems.RUBY_AXE,
                                ModItems.RUBY_PICKAXE,
                                ModItems.RUBY_HOE,
                                ModItems.RUBY_SHOVEL,
                                ModItems.XIRION_SWORD,
                                ModItems.XIRION_AXE,
                                ModItems.XIRION_PICKAXE,
                                ModItems.XIRION_HOE,
                                ModItems.XIRION_SHOVEL
                        )
                )
                .build(consumer, BeyondTheBlock.MOD_ID + ":tools/properly_equipped");
    }
}