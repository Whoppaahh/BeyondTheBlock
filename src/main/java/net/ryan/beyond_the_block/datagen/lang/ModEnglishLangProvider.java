package net.ryan.beyond_the_block.datagen.lang;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public class ModEnglishLangProvider implements DataProvider {


    private final DataGenerator generator;

    public ModEnglishLangProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(DataWriter writer) throws IOException {
        Map<String, String> translations = new TreeMap<>();

        generateItemTranslations(translations);
        generateBlockTranslations(translations);
        generateEnchantmentTranslations(translations);
        generateEffectTranslations(translations);
        generateMiscTranslations(translations);

        JsonObject json = new JsonObject();
        for (Map.Entry<String, String> entry : translations.entrySet()) {
            json.addProperty(entry.getKey(), entry.getValue());
        }

        Path path = generator.getOutput().resolve("assets/" + BeyondTheBlock.MOD_ID + "/lang/en_us.json");
        DataProvider.writeToPath(writer, json, path);
    }

    @Override
    public String getName() {
        return "Beyond The Block English Lang";
    }

    private void generateItemTranslations(Map<String, String> translations) {
        addItem(translations, ModItems.RUBY_ITEM);
        addItem(translations, ModItems.MIRANITE_ITEM);
        addItem(translations, ModItems.RAW_MIRANITE_ITEM);
        addItem(translations, ModItems.CHROMITE_ITEM);
        addItem(translations, ModItems.RAW_CHROMITE_ITEM);
        addItem(translations, ModItems.NOCTURNITE_ITEM);
        addItem(translations, ModItems.RAW_NOCTURNITE_ITEM);
        addItem(translations, ModItems.AMBERINE_ITEM);
        addItem(translations, ModItems.RAW_AMBERINE_ITEM);
        addItem(translations, ModItems.ROSETTE_ITEM);
        addItem(translations, ModItems.RAW_ROSETTE_ITEM);
        addItem(translations, ModItems.AZUROS_ITEM);
        addItem(translations, ModItems.RAW_AZUROS_ITEM);
        addItem(translations, ModItems.INDIGRA_ITEM);
        addItem(translations, ModItems.RAW_INDIGRA_ITEM);
        addItem(translations, ModItems.XIRION_ITEM);

        addItem(translations, ModItems.RUBY_SWORD);
        addItem(translations, ModItems.RUBY_AXE);
        addItem(translations, ModItems.RUBY_PICKAXE);
        addItem(translations, ModItems.RUBY_HOE);
        addItem(translations, ModItems.RUBY_SHOVEL);
        addItem(translations, ModItems.RUBY_HELMET);
        addItem(translations, ModItems.RUBY_CHESTPLATE);
        addItem(translations, ModItems.RUBY_LEGGINGS);
        addItem(translations, ModItems.RUBY_BOOTS);

        addItem(translations, ModItems.AMBERINE_SWORD);
        addItem(translations, ModItems.AMBERINE_AXE);
        addItem(translations, ModItems.AMBERINE_PICKAXE);
        addItem(translations, ModItems.AMBERINE_HOE);
        addItem(translations, ModItems.AMBERINE_SHOVEL);
        addItem(translations, ModItems.AMBERINE_HELMET);
        addItem(translations, ModItems.AMBERINE_CHESTPLATE);
        addItem(translations, ModItems.AMBERINE_LEGGINGS);
        addItem(translations, ModItems.AMBERINE_BOOTS);

        addItem(translations, ModItems.AZUROS_SWORD);
        addItem(translations, ModItems.AZUROS_AXE);
        addItem(translations, ModItems.AZUROS_PICKAXE);
        addItem(translations, ModItems.AZUROS_HOE);
        addItem(translations, ModItems.AZUROS_SHOVEL);
        addItem(translations, ModItems.AZUROS_HELMET);
        addItem(translations, ModItems.AZUROS_CHESTPLATE);
        addItem(translations, ModItems.AZUROS_LEGGINGS);
        addItem(translations, ModItems.AZUROS_BOOTS);

        addItem(translations, ModItems.CHROMITE_SWORD);
        addItem(translations, ModItems.CHROMITE_AXE);
        addItem(translations, ModItems.CHROMITE_PICKAXE);
        addItem(translations, ModItems.CHROMITE_HOE);
        addItem(translations, ModItems.CHROMITE_SHOVEL);
        addItem(translations, ModItems.CHROMITE_HELMET);
        addItem(translations, ModItems.CHROMITE_CHESTPLATE);
        addItem(translations, ModItems.CHROMITE_LEGGINGS);
        addItem(translations, ModItems.CHROMITE_BOOTS);

        addItem(translations, ModItems.MIRANITE_SWORD);
        addItem(translations, ModItems.MIRANITE_AXE);
        addItem(translations, ModItems.MIRANITE_PICKAXE);
        addItem(translations, ModItems.MIRANITE_HOE);
        addItem(translations, ModItems.MIRANITE_SHOVEL);
        addItem(translations, ModItems.MIRANITE_HELMET);
        addItem(translations, ModItems.MIRANITE_CHESTPLATE);
        addItem(translations, ModItems.MIRANITE_LEGGINGS);
        addItem(translations, ModItems.MIRANITE_BOOTS);

        addItem(translations, ModItems.ROSETTE_SWORD);
        addItem(translations, ModItems.ROSETTE_AXE);
        addItem(translations, ModItems.ROSETTE_PICKAXE);
        addItem(translations, ModItems.ROSETTE_HOE);
        addItem(translations, ModItems.ROSETTE_SHOVEL);
        addItem(translations, ModItems.ROSETTE_HELMET);
        addItem(translations, ModItems.ROSETTE_CHESTPLATE);
        addItem(translations, ModItems.ROSETTE_LEGGINGS);
        addItem(translations, ModItems.ROSETTE_BOOTS);

        addItem(translations, ModItems.XIRION_SWORD);
        addItem(translations, ModItems.XIRION_AXE);
        addItem(translations, ModItems.XIRION_PICKAXE);
        addItem(translations, ModItems.XIRION_HOE);
        addItem(translations, ModItems.XIRION_SHOVEL);
        addItem(translations, ModItems.XIRION_HELMET);
        addItem(translations, ModItems.XIRION_CHESTPLATE);
        addItem(translations, ModItems.XIRION_LEGGINGS);
        addItem(translations, ModItems.XIRION_BOOTS);

        addItem(translations, ModItems.ASTRACINDER);
        addItem(translations, ModItems.ECLIPSED_BLOOM);
    }

    private void generateBlockTranslations(Map<String, String> translations) {
        addBlock(translations, ModBlocks.RUBY_BLOCK);
        addBlock(translations, ModBlocks.RUBY_ORE);
        addBlock(translations, ModBlocks.DEEPSLATE_RUBY_ORE);

        addBlock(translations, ModBlocks.MIRANITE_BLOCK);
        addBlock(translations, ModBlocks.RAW_MIRANITE_BLOCK);
        addBlock(translations, ModBlocks.MIRANITE_ORE);
        addBlock(translations, ModBlocks.DEEPSLATE_MIRANITE_ORE);
        addBlock(translations, ModBlocks.NETHER_MIRANITE_ORE);
        addBlock(translations, ModBlocks.END_MIRANITE_ORE);

        addBlock(translations, ModBlocks.CHROMITE_BLOCK);
        addBlock(translations, ModBlocks.CHROMITE_ORE);
        addBlock(translations, ModBlocks.DEEPSLATE_CHROMITE_ORE);
        addBlock(translations, ModBlocks.NETHER_CHROMITE_ORE);
        addBlock(translations, ModBlocks.END_CHROMITE_ORE);

        addBlock(translations, ModBlocks.NOCTURNITE_BLOCK);
        addBlock(translations, ModBlocks.NOCTURNITE_ORE);
        addBlock(translations, ModBlocks.DEEPSLATE_NOCTURNITE_ORE);
        addBlock(translations, ModBlocks.NETHER_NOCTURNITE_ORE);
        addBlock(translations, ModBlocks.END_NOCTURNITE_ORE);

        addBlock(translations, ModBlocks.AMBERINE_BLOCK);
        addBlock(translations, ModBlocks.AMBERINE_ORE);
        addBlock(translations, ModBlocks.DEEPSLATE_AMBERINE_ORE);

        addBlock(translations, ModBlocks.ROSETTE_BLOCK);
        addBlock(translations, ModBlocks.RAW_ROSETTE_BLOCK);
        addBlock(translations, ModBlocks.ROSETTE_ORE);
        addBlock(translations, ModBlocks.DEEPSLATE_ROSETTE_ORE);
        addBlock(translations, ModBlocks.NETHER_ROSETTE_ORE);
        addBlock(translations, ModBlocks.END_ROSETTE_ORE);

        addBlock(translations, ModBlocks.AZUROS_BLOCK);
        addBlock(translations, ModBlocks.RAW_AZUROS_BLOCK);
        addBlock(translations, ModBlocks.AZUROS_ORE);
        addBlock(translations, ModBlocks.DEEPSLATE_AZUROS_ORE);
        addBlock(translations, ModBlocks.NETHER_AZUROS_ORE);
        addBlock(translations, ModBlocks.END_AZUROS_ORE);

        addBlock(translations, ModBlocks.INDIGRA_BLOCK);
        addBlock(translations, ModBlocks.INDIGRA_ORE);
        addBlock(translations, ModBlocks.DEEPSLATE_INDIGRA_ORE);
        addBlock(translations, ModBlocks.NETHER_INDIGRA_ORE);
        addBlock(translations, ModBlocks.END_INDIGRA_ORE);

        addBlock(translations, ModBlocks.XIRION_BLOCK);
        addBlock(translations, ModBlocks.XIRION_ORE);
        addBlock(translations, ModBlocks.DEEPSLATE_XIRION_ORE);
        addBlock(translations, ModBlocks.NETHER_XIRION_ORE);
        addBlock(translations, ModBlocks.END_XIRION_ORE);
    }

    private void generateEnchantmentTranslations(Map<String, String> translations) {
        addEnchantment(translations, ModEnchantments.FISHING_COOKING);
        addEnchantment(translations, ModEnchantments.FLAME_SWEEP);
        addEnchantment(translations, ModEnchantments.WRATH_OF_THOR);
        addEnchantment(translations, ModEnchantments.HOMING);
        addEnchantment(translations, ModEnchantments.WARDING_GLYPH);
        addEnchantment(translations, ModEnchantments.LEAP_OF_FAITH);
        addEnchantment(translations, ModEnchantments.GROUNDED_RESISTANCE);
        addEnchantment(translations, ModEnchantments.SILENT_STEPS);
        addEnchantment(translations, ModEnchantments.ECHO_GUARD);
        addEnchantment(translations, ModEnchantments.DURABILITY_BOOST);
        addEnchantment(translations, ModEnchantments.IRON_CLAD_VISION);
        addEnchantment(translations, ModEnchantments.RADIANT_AURA);
        addEnchantment(translations, ModEnchantments.SHADOWS_VEIL);
        addEnchantment(translations, ModEnchantments.MIND_WARD);
        addEnchantment(translations, ModEnchantments.FROZEN_MOMENTUM);
        addEnchantment(translations, ModEnchantments.GRACEFUL_MOVEMENT);
        addEnchantment(translations, ModEnchantments.NIGHT_STRIDE);
        addEnchantment(translations, ModEnchantments.NIGHTFALL_CLEAVE);
        addEnchantment(translations, ModEnchantments.TIMBER_CUT);
        addEnchantment(translations, ModEnchantments.BARKSKIN);
        addEnchantment(translations, ModEnchantments.DEEP_TILL);
        addEnchantment(translations, ModEnchantments.GARDENS_BOUNTY);
        addEnchantment(translations, ModEnchantments.NIGHT_CULTIVATION);
        addEnchantment(translations, ModEnchantments.SHADOW_MINING);
        addEnchantment(translations, ModEnchantments.STONE_BREAKER);
        addEnchantment(translations, ModEnchantments.SMELTING_STRIKE);
        addEnchantment(translations, ModEnchantments.DARK_DIG);
        addEnchantment(translations, ModEnchantments.EARTH_SHATTER);
        addEnchantment(translations, ModEnchantments.FERTILITY);
        addEnchantment(translations, ModEnchantments.LIFE_STEAL);
        addEnchantment(translations, ModEnchantments.PHANTOM_SLASH);
        addEnchantment(translations, ModEnchantments.RESILIENT_STRIKE);
        addEnchantment(translations, ModEnchantments.TEMPORAL_SLICE);
    }

    private void generateEffectTranslations(Map<String, String> translations) {
        addEffect(translations, ModEffects.CLARITY);
        addEffect(translations, ModEffects.FREEZE);
    }

    private void generateMiscTranslations(Map<String, String> translations) {
        translations.put("itemGroup.beyond_the_block.mod_items", "Beyond The Block Items");
        translations.put("itemGroup.beyond_the_block.mod_blocks", "Beyond The Block Blocks");
        translations.put("itemGroup.beyond_the_block.mod_tools", "Beyond The Block Tools");
        translations.put("itemGroup.beyond_the_block.mod_armour", "Beyond The Block Armour");
    }

    private void addItem(Map<String, String> translations, Item item) {
        Identifier id = Registry.ITEM.getId(item);
        if (!BeyondTheBlock.MOD_ID.equals(id.getNamespace())) {
            throw new IllegalStateException("Tried to add translation for non-mod item: " + item);
        }

        translations.put(item.getTranslationKey(), toEnglishName(id.getPath()));
    }

    private void addBlock(Map<String, String> translations, Block block) {
        Identifier id = Registry.BLOCK.getId(block);
        if (!BeyondTheBlock.MOD_ID.equals(id.getNamespace())) {
            throw new IllegalStateException("Tried to add translation for non-mod block: " + block);
        }

        translations.put(block.getTranslationKey(), toEnglishName(id.getPath()));
    }

    private void addEnchantment(Map<String, String> translations, Enchantment enchantment) {
        Identifier id = Registry.ENCHANTMENT.getId(enchantment);
        if (id == null || !BeyondTheBlock.MOD_ID.equals(id.getNamespace())) {
            throw new IllegalStateException("Tried to add translation for non-mod enchantment: " + enchantment);
        }

        translations.put(enchantment.getTranslationKey(), toEnglishName(id.getPath()));
    }

    private void addEffect(Map<String, String> translations, StatusEffect effect) {
        Identifier id = Registry.STATUS_EFFECT.getId(effect);
        if (id == null || !BeyondTheBlock.MOD_ID.equals(id.getNamespace())) {
            throw new IllegalStateException("Tried to add translation for non-mod effect: " + effect);
        }

        translations.put(effect.getTranslationKey(), toEnglishName(id.getPath()));
    }

    private String toEnglishName(String path) {
        String[] parts = path.split("_");
        StringBuilder builder = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            if (!builder.isEmpty()) {
                builder.append(' ');
            }

            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1));
            }
        }

        return builder.toString();
    }
}