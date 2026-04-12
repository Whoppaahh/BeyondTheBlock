package net.ryan.beyond_the_block.content.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.content.item.*;
import net.ryan.beyond_the_block.content.item.armour.ModArmourItem;
import net.ryan.beyond_the_block.content.item.armour.ModArmourMaterials;
import net.ryan.beyond_the_block.content.item.tools.*;
import net.ryan.beyond_the_block.content.registry.family.ModBoatVariant;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class ModItems {

    public static final Item CHERRY_BOAT = registerItem("cherry_boat",
            new ModBoatItem(ModBoatVariant.CHERRY, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModBlocksTab)));

    public static final Item CHERRY_CHEST_BOAT = registerItem("cherry_chest_boat",
            new ModChestBoatItem(ModBoatVariant.CHERRY, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModBlocksTab)));

    public static final Item PALE_OAK_BOAT = registerItem("pale_oak_boat",
            new ModBoatItem(ModBoatVariant.PALE_OAK, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModBlocksTab)));

    public static final Item PALE_OAK_CHEST_BOAT = registerItem("pale_oak_chest_boat",
            new ModChestBoatItem(ModBoatVariant.PALE_OAK, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModBlocksTab)));

    public static final Item BAMBOO_RAFT = registerItem("bamboo_raft",
            new ModRaftItem(ModBoatVariant.BAMBOO, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModBlocksTab)));

    public static final Item BAMBOO_CHEST_RAFT = registerItem("bamboo_chest_raft",
            new ModChestRaftItem(ModBoatVariant.BAMBOO, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModBlocksTab)));


    public static final Item NETHERITE_UPGRADE_SMITHING_TEMPLATE = registerItem("netherite_upgrade_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("sentry_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item DUNE_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("dune_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item COAST_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("coast_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item WILD_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("wild_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item WARD_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("ward_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item EYE_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("eye_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item VEX_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("vex_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item TIDE_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("tide_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("snout_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item RIB_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("rib_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("spire_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("silence_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("wayfinder_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("shaper_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item HOST_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("host_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item RAISER_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("raiser_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item FLOW_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("flow_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item BOLT_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("bolt_armor_trim_smithing_template",
            new TrimTemplateItem(new FabricItemSettings().group(ModItemGroup.ModArmourTab)));

    public static final Item TROWEL_ITEM = registerItem("trowel", new TrowelItem(new FabricItemSettings().group(ModItemGroup.ModToolTab)));
    public static final Item SANTA_HELMET = registerItem("santa_helmet", new ArmorItem(ModArmourMaterials.SANTA, EquipmentSlot.HEAD, new Item.Settings().group(ModItemGroup.ModArmourTab)));
    public static final Item SANTA_CHESTPLATE = registerItem("santa_chestplate", new ArmorItem(ModArmourMaterials.SANTA, EquipmentSlot.CHEST, new Item.Settings().group(ModItemGroup.ModArmourTab)));
    public static final Item SANTA_LEGGINGS = registerItem("santa_leggings", new ArmorItem(ModArmourMaterials.SANTA, EquipmentSlot.LEGS, new Item.Settings().group(ModItemGroup.ModArmourTab)));
    public static final Item SANTA_BOOTS = registerItem("santa_boots", new ArmorItem(ModArmourMaterials.SANTA, EquipmentSlot.FEET, new Item.Settings().group(ModItemGroup.ModArmourTab)));


    public static final Item CUPID_ARROW_ITEM = registerItem("cupid_arrow", new CupidArrowItem(new FabricItemSettings().group(ItemGroup.COMBAT)));

    public static final Item RUBY_ITEM = registerItem("ruby",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));

    //region Modded Ores
    public static final Item MIRANITE_ITEM = registerItem("miranite",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));
    public static final Item RAW_MIRANITE_ITEM = registerItem("raw_miranite",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));

    public static final Item CHROMITE_ITEM = registerItem("chromite",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));
    public static final Item RAW_CHROMITE_ITEM = registerItem("raw_chromite",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));

    public static final Item NOCTURNITE_ITEM = registerItem("nocturnite",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));
    public static final Item RAW_NOCTURNITE_ITEM = registerItem("raw_nocturnite",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));

    public static final Item AMBERINE_ITEM = registerItem("amberine",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));
    public static final Item RAW_AMBERINE_ITEM = registerItem("raw_amberine",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));

    public static final Item AZUROS_ITEM = registerItem("azuros",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));
    public static final Item RAW_AZUROS_ITEM = registerItem("raw_azuros",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));

    public static final Item INDIGRA_ITEM = registerItem("indigra",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));
    public static final Item RAW_INDIGRA_ITEM = registerItem("raw_indigra",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));

    public static final Item XIRION_ITEM = registerItem("xirion",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));

    public static final Item ROSETTE_ITEM = registerItem("rosette",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));
    public static final Item RAW_ROSETTE_ITEM = registerItem("raw_rosette",
            new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));
    //endregion
    //region Modded Fuels
    public static final Item ASTRACINDER = registerItem("astracinder", new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));
    public static final Item ECLIPSED_BLOOM = registerItem("eclipsed_bloom", new Item(new FabricItemSettings().group(ModItemGroup.ModBlocksTab)));
    //endregion
    //region Modded Tools
    //region Ruby Tools
    public static final Item RUBY_AXE = registerItem("ruby_axe", new ModAxeItem(ModToolMaterials.RUBY, 7, 5, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item RUBY_PICKAXE = registerItem("ruby_pickaxe", new ModPickaxeItem(ModToolMaterials.RUBY, 5, 5, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item RUBY_HOE = registerItem("ruby_hoe", new ModHoeItem(ModToolMaterials.RUBY, 0, 0, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item RUBY_SHOVEL = registerItem("ruby_shovel", new ModShovelItem(ModToolMaterials.RUBY, 5, 5, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item RUBY_SWORD = registerItem("ruby_sword", new ModSwordItem(ModToolMaterials.RUBY, 10, 10, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));

    //endregion
    //region Xirion Tools
    public static final Item XIRION_AXE = registerItem("xirion_axe", new ModAxeItem(ModToolMaterials.XIRION, 5, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item XIRION_PICKAXE = registerItem("xirion_pickaxe", new ModPickaxeItem(ModToolMaterials.XIRION, 3, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item XIRION_HOE = registerItem("xirion_hoe", new ModHoeItem(ModToolMaterials.XIRION, 0, 0, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item XIRION_SHOVEL = registerItem("xirion_shovel", new ModShovelItem(ModToolMaterials.XIRION, 3, 2, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item XIRION_SWORD = registerItem("xirion_sword", new ModSwordItem(ModToolMaterials.XIRION, 5, 5, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
   //endregion
    //region Azuros Tools
    public static final Item AZUROS_AXE = registerItem("azuros_axe", new ModAxeItem(ModToolMaterials.AZUROS, 5, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item AZUROS_PICKAXE = registerItem("azuros_pickaxe", new ModPickaxeItem(ModToolMaterials.AZUROS, 3, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item AZUROS_HOE = registerItem("azuros_hoe", new ModHoeItem(ModToolMaterials.AZUROS, 0, 0, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item AZUROS_SHOVEL = registerItem("azuros_shovel", new ModShovelItem(ModToolMaterials.AZUROS, 3, 2, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item AZUROS_SWORD = registerItem("azuros_sword", new ModSwordItem(ModToolMaterials.AZUROS, 5, 5, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    //endregion
    //region Chromite Tools
    public static final Item CHROMITE_AXE = registerItem("chromite_axe", new ModAxeItem(ModToolMaterials.CHROMITE, 5, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item CHROMITE_PICKAXE = registerItem("chromite_pickaxe", new ModPickaxeItem(ModToolMaterials.CHROMITE, 3, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item CHROMITE_HOE = registerItem("chromite_hoe", new ModHoeItem(ModToolMaterials.CHROMITE, 0, 0, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item CHROMITE_SHOVEL = registerItem("chromite_shovel", new ModShovelItem(ModToolMaterials.CHROMITE, 3, 2, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item CHROMITE_SWORD = registerItem("chromite_sword", new ModSwordItem(ModToolMaterials.CHROMITE, 5, 5, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    //endregion
    //region Miranite Tools
    public static final Item MIRANITE_AXE = registerItem("miranite_axe", new ModAxeItem(ModToolMaterials.MIRANITE, 5, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item MIRANITE_PICKAXE = registerItem("miranite_pickaxe", new ModPickaxeItem(ModToolMaterials.MIRANITE, 3, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item MIRANITE_HOE = registerItem("miranite_hoe", new ModHoeItem(ModToolMaterials.MIRANITE, 0, 0, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item MIRANITE_SHOVEL = registerItem("miranite_shovel", new ModShovelItem(ModToolMaterials.MIRANITE, 3, 2, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item MIRANITE_SWORD = registerItem("miranite_sword", new ModSwordItem(ModToolMaterials.MIRANITE, 5, 5, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    //endregion
    //region Amberine Tools
    public static final Item AMBERINE_AXE = registerItem("amberine_axe", new ModAxeItem(ModToolMaterials.AMBERINE, 5, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item AMBERINE_PICKAXE = registerItem("amberine_pickaxe", new ModPickaxeItem(ModToolMaterials.AMBERINE, 3, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item AMBERINE_HOE = registerItem("amberine_hoe", new ModHoeItem(ModToolMaterials.AMBERINE, 0, 0, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item AMBERINE_SHOVEL = registerItem("amberine_shovel", new ModShovelItem(ModToolMaterials.AMBERINE, 3, 2, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item AMBERINE_SWORD = registerItem("amberine_sword", new ModSwordItem(ModToolMaterials.AMBERINE, 5, 5, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    //endregion
    //region Rosette Tools
    public static final Item ROSETTE_AXE = registerItem("rosette_axe", new ModAxeItem(ModToolMaterials.ROSETTE, 5, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item ROSETTE_PICKAXE = registerItem("rosette_pickaxe", new ModPickaxeItem(ModToolMaterials.ROSETTE, 3, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item ROSETTE_HOE = registerItem("rosette_hoe", new ModHoeItem(ModToolMaterials.ROSETTE, 0, 0, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item ROSETTE_SHOVEL = registerItem("rosette_shovel", new ModShovelItem(ModToolMaterials.ROSETTE, 3, 2, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item ROSETTE_SWORD = registerItem("rosette_sword", new ModSwordItem(ModToolMaterials.ROSETTE, 5, 5, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    //endregion
    //region Nocturnite Tools
    public static final Item NOCTURNITE_AXE = registerItem("nocturnite_axe", new ModAxeItem(ModToolMaterials.NOCTURNITE, 7, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item NOCTURNITE_PICKAXE = registerItem("nocturnite_pickaxe", new ModPickaxeItem(ModToolMaterials.NOCTURNITE, 5, 3, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item NOCTURNITE_HOE = registerItem("nocturnite_hoe", new ModHoeItem(ModToolMaterials.NOCTURNITE, 2, 1, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item NOCTURNITE_SHOVEL = registerItem("nocturnite_shovel", new ModShovelItem(ModToolMaterials.NOCTURNITE, 5, 2, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    public static final Item NOCTURNITE_SWORD = registerItem("nocturnite_sword", new ModSwordItem(ModToolMaterials.NOCTURNITE, 8, 7, new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));
    //endregion
    //endregion
    //region Modded Armour
    //region Ruby
    public static final Item RUBY_HELMET = registerItem("ruby_helmet",
            new ModArmourItem(ModArmourMaterials.RUBY, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item RUBY_CHESTPLATE = registerItem("ruby_chestplate",
            new ModArmourItem(ModArmourMaterials.RUBY, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item RUBY_LEGGINGS = registerItem("ruby_leggings",
            new ModArmourItem(ModArmourMaterials.RUBY, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item RUBY_BOOTS = registerItem("ruby_boots",
            new ModArmourItem(ModArmourMaterials.RUBY, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    //endregion
    //region Nocturnite
    public static final Item NOCTURNITE_HELMET = registerItem("nocturnite_helmet",
            new ModArmourItem(ModArmourMaterials.NOCTURNITE, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item NOCTURNITE_CHESTPLATE = registerItem("nocturnite_chestplate",
            new ModArmourItem(ModArmourMaterials.NOCTURNITE, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item NOCTURNITE_LEGGINGS = registerItem("nocturnite_leggings",
            new ModArmourItem(ModArmourMaterials.NOCTURNITE, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item NOCTURNITE_BOOTS = registerItem("nocturnite_boots",
            new ModArmourItem(ModArmourMaterials.NOCTURNITE, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    //endregion
    //region Azuros
    public static final Item AZUROS_HELMET = registerItem("azuros_helmet",
            new ModArmourItem(ModArmourMaterials.AZUROS, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item AZUROS_CHESTPLATE = registerItem("azuros_chestplate",
            new ModArmourItem(ModArmourMaterials.AZUROS, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item AZUROS_LEGGINGS = registerItem("azuros_leggings",
            new ModArmourItem(ModArmourMaterials.AZUROS, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item AZUROS_BOOTS = registerItem("azuros_boots",
            new ModArmourItem(ModArmourMaterials.AZUROS, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    //endregion
    //region Amberine
    public static final Item AMBERINE_HELMET = registerItem("amberine_helmet",
            new ModArmourItem(ModArmourMaterials.AMBERINE, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item AMBERINE_CHESTPLATE = registerItem("amberine_chestplate",
            new ModArmourItem(ModArmourMaterials.AMBERINE, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item AMBERINE_LEGGINGS = registerItem("amberine_leggings",
            new ModArmourItem(ModArmourMaterials.AMBERINE, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item AMBERINE_BOOTS = registerItem("amberine_boots",
            new ModArmourItem(ModArmourMaterials.AMBERINE, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    //endregion
    //region Miranite
    public static final Item MIRANITE_HELMET = registerItem("miranite_helmet",
            new ModArmourItem(ModArmourMaterials.MIRANITE, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item MIRANITE_CHESTPLATE = registerItem("miranite_chestplate",
            new ModArmourItem(ModArmourMaterials.MIRANITE, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item MIRANITE_LEGGINGS = registerItem("miranite_leggings",
            new ModArmourItem(ModArmourMaterials.MIRANITE, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item MIRANITE_BOOTS = registerItem("miranite_boots",
            new ModArmourItem(ModArmourMaterials.MIRANITE, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    //endregion
    //region Chromite
    public static final Item CHROMITE_HELMET = registerItem("chromite_helmet",
            new ModArmourItem(ModArmourMaterials.CHROMITE, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item CHROMITE_CHESTPLATE = registerItem("chromite_chestplate",
            new ModArmourItem(ModArmourMaterials.CHROMITE, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item CHROMITE_LEGGINGS = registerItem("chromite_leggings",
            new ModArmourItem(ModArmourMaterials.CHROMITE, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item CHROMITE_BOOTS = registerItem("chromite_boots",
            new ModArmourItem(ModArmourMaterials.CHROMITE, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    //endregion
    //region Rosette
    public static final Item ROSETTE_HELMET = registerItem("rosette_helmet",
            new ModArmourItem(ModArmourMaterials.ROSETTE, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item ROSETTE_CHESTPLATE = registerItem("rosette_chestplate",
            new ModArmourItem(ModArmourMaterials.ROSETTE, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item ROSETTE_LEGGINGS = registerItem("rosette_leggings",
            new ModArmourItem(ModArmourMaterials.ROSETTE, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item ROSETTE_BOOTS = registerItem("rosette_boots",
            new ModArmourItem(ModArmourMaterials.ROSETTE, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    //endregion
    //region Xirion
    public static final Item XIRION_HELMET = registerItem("xirion_helmet",
            new ModArmourItem(ModArmourMaterials.XIRION, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item XIRION_CHESTPLATE = registerItem("xirion_chestplate",
            new ModArmourItem(ModArmourMaterials.XIRION, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item XIRION_LEGGINGS = registerItem("xirion_leggings",
            new ModArmourItem(ModArmourMaterials.XIRION, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    public static final Item XIRION_BOOTS = registerItem("xirion_boots",
            new ModArmourItem(ModArmourMaterials.XIRION, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ModItemGroup.ModArmourTab)));
    //endregion
    //endregion

    public static final Item XENOLITH_STAFF = registerItem("xenolith_staff_2d", new XenolithStaffItem(new FabricItemSettings().maxCount(1).group(ModItemGroup.ModToolTab)));

    public static final Item ANIMATED_ITEM = registerItem("animated_item",
            new AnimatedItem(new FabricItemSettings().group(ModItemGroup.ModBlocksTab).maxCount(1)));
    public static final Item ANIMATED_BLOCK_ITEM = registerItem("animated_block",
            new AnimatedBlockItem(ModBlocks.ANIMATED_BLOCK,
                    new FabricItemSettings().group(ModItemGroup.ModBlocksTab).maxCount(1)));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(BeyondTheBlock.MOD_ID, name), item);
    }
    private static void registerCompostables(){
        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(Items.ROTTEN_FLESH, 0.3F);
        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(Items.POISONOUS_POTATO, 0.65f);
        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(Items.SPIDER_EYE, 0.3f);
        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(Items.PUFFERFISH, 0.65f);
        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(Items.CHORUS_FRUIT, 0.65f);
        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(Items.BEETROOT_SOUP, 1.0f);
    }

    public static void registerModItems(){
        registerCompostables();
        BeyondTheBlock.LOGGER.info("Registering Mod Items for " + BeyondTheBlock.MOD_ID);
    }


}
