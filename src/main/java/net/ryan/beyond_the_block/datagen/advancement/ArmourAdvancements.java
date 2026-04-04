package net.ryan.beyond_the_block.datagen.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.function.Consumer;

public final class ArmourAdvancements {

    private ArmourAdvancements() {
    }

    public static Advancement generate(Advancement parent, Consumer<Advancement> consumer) {
        Advancement suitedUp = Advancement.Builder.create()
                .parent(parent)
                .display(
                        new ItemStack(ModItems.RUBY_CHESTPLATE),
                        Text.translatable("advancements.beyond_the_block.armour.suited_up.title"),
                        Text.translatable("advancements.beyond_the_block.armour.suited_up.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(
                        "has_mod_armour",
                        InventoryChangedCriterion.Conditions.items(
                                ModItems.RUBY_HELMET,
                                ModItems.RUBY_CHESTPLATE,
                                ModItems.RUBY_LEGGINGS,
                                ModItems.RUBY_BOOTS,

                                ModItems.AMBERINE_HELMET,
                                ModItems.AMBERINE_CHESTPLATE,
                                ModItems.AMBERINE_LEGGINGS,
                                ModItems.AMBERINE_BOOTS,

                                ModItems.AZUROS_HELMET,
                                ModItems.AZUROS_CHESTPLATE,
                                ModItems.AZUROS_LEGGINGS,
                                ModItems.AZUROS_BOOTS,

                                ModItems.CHROMITE_HELMET,
                                ModItems.CHROMITE_CHESTPLATE,
                                ModItems.CHROMITE_LEGGINGS,
                                ModItems.CHROMITE_BOOTS,

                                ModItems.MIRANITE_HELMET,
                                ModItems.MIRANITE_CHESTPLATE,
                                ModItems.MIRANITE_LEGGINGS,
                                ModItems.MIRANITE_BOOTS,

                                ModItems.ROSETTE_HELMET,
                                ModItems.ROSETTE_CHESTPLATE,
                                ModItems.ROSETTE_LEGGINGS,
                                ModItems.ROSETTE_BOOTS,

                                ModItems.XIRION_HELMET,
                                ModItems.XIRION_CHESTPLATE,
                                ModItems.XIRION_LEGGINGS,
                                ModItems.XIRION_BOOTS
                        )
                )
                .build(consumer, BeyondTheBlock.MOD_ID + ":armour/suited_up");

        return Advancement.Builder.create()
                .parent(suitedUp)
                .display(
                        new ItemStack(ModItems.XIRION_CHESTPLATE),
                        Text.translatable("advancements.beyond_the_block.armour.full_set.title"),
                        Text.translatable("advancements.beyond_the_block.armour.full_set.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("helmet", InventoryChangedCriterion.Conditions.items(
                        ModItems.RUBY_HELMET,
                        ModItems.AMBERINE_HELMET,
                        ModItems.AZUROS_HELMET,
                        ModItems.CHROMITE_HELMET,
                        ModItems.MIRANITE_HELMET,
                        ModItems.ROSETTE_HELMET,
                        ModItems.XIRION_HELMET
                ))
                .criterion("chestplate", InventoryChangedCriterion.Conditions.items(
                        ModItems.RUBY_CHESTPLATE,
                        ModItems.AMBERINE_CHESTPLATE,
                        ModItems.AZUROS_CHESTPLATE,
                        ModItems.CHROMITE_CHESTPLATE,
                        ModItems.MIRANITE_CHESTPLATE,
                        ModItems.ROSETTE_CHESTPLATE,
                        ModItems.XIRION_CHESTPLATE
                ))
                .criterion("leggings", InventoryChangedCriterion.Conditions.items(
                        ModItems.RUBY_LEGGINGS,
                        ModItems.AMBERINE_LEGGINGS,
                        ModItems.AZUROS_LEGGINGS,
                        ModItems.CHROMITE_LEGGINGS,
                        ModItems.MIRANITE_LEGGINGS,
                        ModItems.ROSETTE_LEGGINGS,
                        ModItems.XIRION_LEGGINGS
                ))
                .criterion("boots", InventoryChangedCriterion.Conditions.items(
                        ModItems.RUBY_BOOTS,
                        ModItems.AMBERINE_BOOTS,
                        ModItems.AZUROS_BOOTS,
                        ModItems.CHROMITE_BOOTS,
                        ModItems.MIRANITE_BOOTS,
                        ModItems.ROSETTE_BOOTS,
                        ModItems.XIRION_BOOTS
                ))
                .requirements(new String[][]{
                        {"helmet", "chestplate", "leggings", "boots"}
                })
                .build(consumer, BeyondTheBlock.MOD_ID + ":armour/full_set");
    }
}