package net.ryan.beyond_the_block.content.item.tools;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.ryan.beyond_the_block.content.registry.ModItems;

import java.util.function.Supplier;

public enum ModToolMaterials implements ToolMaterial {

    RUBY(MiningLevels.DIAMOND, 1200, 10.0F, 5.0F, 25, () -> Ingredient.ofItems(ModItems.RUBY_ITEM)),
    AZUROS(MiningLevels.DIAMOND, 1200, 10.0F, 5.0F, 25, () -> Ingredient.ofItems(ModItems.AZUROS_ITEM)),
    ROSETTE(MiningLevels.IRON, 1200, 7.0F, 1.0F, 20, () -> Ingredient.ofItems(ModItems.ROSETTE_ITEM)),
    AMBERINE(MiningLevels.STONE, 550, 6.0F, 2.0F, 15, () -> Ingredient.ofItems(ModItems.AMBERINE_ITEM)),
    CHROMITE(MiningLevels.NETHERITE, 1200, 15.0F, 10.0F, 30, () -> Ingredient.ofItems(ModItems.CHROMITE_ITEM)),
    MIRANITE(MiningLevels.NETHERITE, 1200, 15.0F, 10.0F, 30, () -> Ingredient.ofItems(ModItems.MIRANITE_ITEM)),
    XIRION(MiningLevels.DIAMOND, 1200, 8.0F, 5.0F, 25, () -> Ingredient.ofItems(ModItems.XIRION_ITEM)),
    NOCTURNITE(MiningLevels.NETHERITE, 2000, 15.0F, 20.0F, 30, () -> Ingredient.ofItems(ModItems.NOCTURNITE_ITEM));

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    public int getDurability() {
        return this.itemDurability;
    }

    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public int getMiningLevel() {
        return this.miningLevel;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
