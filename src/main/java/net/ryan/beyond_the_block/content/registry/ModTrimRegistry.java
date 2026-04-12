package net.ryan.beyond_the_block.content.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.registry.family.ModTrimMaterial;
import net.ryan.beyond_the_block.content.registry.family.ModTrimPattern;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class ModTrimRegistry {
    private static final Map<Identifier, ModTrimMaterial> MATERIALS = new LinkedHashMap<>();
    private static final Map<Identifier, ModTrimPattern> PATTERNS = new LinkedHashMap<>();
    private static final Map<Item, ModTrimMaterial> MATERIALS_BY_INGREDIENT = new LinkedHashMap<>();
    private static final Map<Item, ModTrimPattern> PATTERNS_BY_TEMPLATE = new LinkedHashMap<>();

    private ModTrimRegistry() {
    }

    public static boolean isNetheriteUpgradeTemplate(ItemStack stack) {
        return stack.isOf(ModItems.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
    }

    public static ModTrimMaterial registerMaterial(ModTrimMaterial material) {
        Objects.requireNonNull(material, "material");
        MATERIALS.put(material.id(), material);
        MATERIALS_BY_INGREDIENT.put(material.ingredient(), material);
        return material;
    }

    public static ModTrimPattern registerPattern(ModTrimPattern pattern) {
        Objects.requireNonNull(pattern, "pattern");
        PATTERNS.put(pattern.id(), pattern);
        PATTERNS_BY_TEMPLATE.put(pattern.templateItem(), pattern);
        return pattern;
    }

    public static ModTrimMaterial getMaterial(Identifier id) {
        return MATERIALS.get(id);
    }

    public static ModTrimPattern getPattern(Identifier id) {
        return PATTERNS.get(id);
    }

    public static ModTrimMaterial getMaterialFromIngredient(Item item) {
        return MATERIALS_BY_INGREDIENT.get(item);
    }

    public static ModTrimPattern getPatternFromTemplate(Item item) {
        return PATTERNS_BY_TEMPLATE.get(item);
    }

    public static boolean isValidMaterialIngredient(Item item) {
        return MATERIALS_BY_INGREDIENT.containsKey(item);
    }

    public static boolean isValidTemplate(Item item) {
        return PATTERNS_BY_TEMPLATE.containsKey(item);
    }

    public static Collection<ModTrimMaterial> getMaterials() {
        return Collections.unmodifiableCollection(MATERIALS.values());
    }

    public static Collection<ModTrimPattern> getPatterns() {
        return Collections.unmodifiableCollection(PATTERNS.values());
    }
}