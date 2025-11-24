package net.ryan.beyond_the_block.item.Armour;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.ryan.beyond_the_block.item.ModItems;

import java.util.function.Supplier;

public enum ModArmourMaterials implements ArmorMaterial {


    RUBY("ruby", 30, new int[]{3, 6, 8, 3}, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            3.0F, 0.1F, () -> Ingredient.ofItems(ModItems.RUBY_ITEM)),
    AZUROS("azuros", 30, new int[]{3, 6, 8, 3}, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            3.0F, 0.1F, () -> Ingredient.ofItems(ModItems.AZUROS_ITEM)),
    ROSETTE("rosette", 20, new int[]{3, 4, 6, 3}, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            3.0F, 0.1F, () -> Ingredient.ofItems(ModItems.ROSETTE_ITEM)),
    AMBERINE("amberine", 10, new int[]{1, 3, 5, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
            3.0F, 0F, () -> Ingredient.ofItems(ModItems.AMBERINE_ITEM)),
    CHROMITE("chromite", 40, new int[]{5, 8, 10, 5}, 30, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            3.0F, 0.3F, () -> Ingredient.ofItems(ModItems.CHROMITE_ITEM)),
    MIRANITE("miranite", 40, new int[]{5, 8, 10, 5}, 30, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            3.0F, 0.3F, () -> Ingredient.ofItems(ModItems.MIRANITE_ITEM)),
    XIRION("xirion", 30, new int[]{3, 6, 8, 3}, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            3.0F, 0.1F, () -> Ingredient.ofItems(ModItems.XIRION_ITEM)),
    SANTA("santa", 0, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            0F, 0F, () -> Ingredient.ofItems(Items.RED_WOOL));


    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredientSupplier;

     ModArmourMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredientSupplier) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredientSupplier = repairIngredientSupplier;
    }

    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
    }

    public int getProtectionAmount(EquipmentSlot slot) {
        return this.protectionAmounts[slot.getEntitySlotId()];
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredientSupplier.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
