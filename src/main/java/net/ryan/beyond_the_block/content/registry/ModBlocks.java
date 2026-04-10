package net.ryan.beyond_the_block.content.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SignItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.content.block.*;
import net.ryan.beyond_the_block.content.block.cauldrons.DyedWaterCauldronBlock;
import net.ryan.beyond_the_block.content.block.cauldrons.ModdedFluidCauldronBlock;
import net.ryan.beyond_the_block.content.block.shrine.PlayerInputBlocks.DoubleInputBlock;
import net.ryan.beyond_the_block.content.block.shrine.PlayerInputBlocks.SingleInputBlock;
import net.ryan.beyond_the_block.content.block.shrine.ShrineCoreBlock;
import net.ryan.beyond_the_block.content.block.shrine.ShrineDecorBlock;
import net.ryan.beyond_the_block.content.block.shrine.ShrineHeadsBlock;
import net.ryan.beyond_the_block.content.block.sponges.CompressedSpongeBlock;
import net.ryan.beyond_the_block.content.block.sponges.DoubleCompressedSpongeBlock;
import net.ryan.beyond_the_block.content.block.sponges.TripleCompressedSpongeBlock;
import net.ryan.beyond_the_block.content.block.sponges.WetCompressedSpongeBlock;
import net.ryan.beyond_the_block.content.item.HangingSignItem;
import net.ryan.beyond_the_block.content.item.ModItemGroup;
import net.ryan.beyond_the_block.content.registry.family.*;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class ModBlocks {

    //GENERAL / UTILITY BLOCKS
    public static final Block OAK_HANGING_SIGN = registerBlockWithoutBlockItem(
            "oak_hanging_sign",
            new HangingSignBlock(FabricBlockSettings.of(Material.WOOD), SignType.OAK)
    );

    public static final Block OAK_WALL_HANGING_SIGN = registerBlockWithoutBlockItem(
            "oak_wall_hanging_sign",
            new WallHangingSignBlock(FabricBlockSettings.of(Material.WOOD), SignType.OAK)
    );



    public static final Block WOODCUTTER_BLOCK = registerBlock(
            "woodcutter",
            new WoodcutterBlock(FabricBlockSettings.copy(Blocks.STONECUTTER)),
            ModItemGroup.ModBlocksTab
    );

    public static final Block CHISELED_BOOKSHELF = registerBlock(
            "chiseled_bookshelf",
            new ChiseledBookshelfBlock(FabricBlockSettings.of(Material.WOOD)),
            ModItemGroup.ModBlocksTab
    );

    public static final Block GEM_BLOCK = registerBlock(
            "gem_block",
            new GemBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).requiresTool().nonOpaque()),
            ModItemGroup.ModBlocksTab
    );

    public static final Block PLAYER_VAULT_BLOCK = registerBlock(
            "player_vault",
            new PlayerVaultBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).requiresTool().nonOpaque()),
            ModItemGroup.ModBlocksTab
    );

    public static final Block INFI_FURNACE_BLOCK = registerBlock(
            "infi_furnace",
            new InfiFurnaceBlock(
                    FabricBlockSettings.of(Material.STONE)
                            .strength(2f, 2f)
                            .requiresTool()
                            .luminance(state -> state.get(InfiFurnaceBlock.LIT) ? 15 : 0)
                            .nonOpaque()
            ),
            ModItemGroup.ModBlocksTab
    );

    public static final Block DECRAFTER_BLOCK = registerBlock(
            "decrafter_block",
            new DecrafterBlock(FabricBlockSettings.of(Material.WOOD).strength(2f, 2f)),
            ModItemGroup.ModBlocksTab
    );

    public static final Block LAVA_LAMP_BLOCK = registerBlock(
            "lava_lamp_block",
            new Block(FabricBlockSettings.of(Material.STONE).strength(2f, 2f).requiresTool().nonOpaque().luminance(15)),
            ModItemGroup.ModBlocksTab
    );
     //SHELVES

    public static final ShelfSet OAK_SHELF_SET = registerShelfSet("oak", ModItemGroup.ModBlocksTab);
    public static final ShelfSet SPRUCE_SHELF_SET = registerShelfSet("spruce", ModItemGroup.ModBlocksTab);
    public static final ShelfSet BIRCH_SHELF_SET = registerShelfSet("birch", ModItemGroup.ModBlocksTab);
    public static final ShelfSet JUNGLE_SHELF_SET = registerShelfSet("jungle", ModItemGroup.ModBlocksTab);
    public static final ShelfSet ACACIA_SHELF_SET = registerShelfSet("acacia", ModItemGroup.ModBlocksTab);
    public static final ShelfSet DARK_OAK_SHELF_SET = registerShelfSet("dark_oak", ModItemGroup.ModBlocksTab);
    public static final ShelfSet MANGROVE_SHELF_SET = registerShelfSet("mangrove", ModItemGroup.ModBlocksTab);
    public static final ShelfSet BAMBOO_SHELF_SET = registerShelfSet("bamboo", ModItemGroup.ModBlocksTab);
    public static final ShelfSet CRIMSON_SHELF_SET = registerShelfSet("crimson", ModItemGroup.ModBlocksTab);
    public static final ShelfSet WARPED_SHELF_SET = registerShelfSet("warped", ModItemGroup.ModBlocksTab);
    public static final ShelfSet CHERRY_SHELF_SET = registerShelfSet("cherry", ModItemGroup.ModBlocksTab);
    public static final ShelfSet PALE_OAK_SHELF_SET = registerShelfSet("pale_oak", ModItemGroup.ModBlocksTab);

    public static final Block OAK_SHELF_BLOCK = OAK_SHELF_SET.shelf();
    public static final Block SPRUCE_SHELF_BLOCK = SPRUCE_SHELF_SET.shelf();
    public static final Block BIRCH_SHELF_BLOCK = BIRCH_SHELF_SET.shelf();
    public static final Block JUNGLE_SHELF_BLOCK = JUNGLE_SHELF_SET.shelf();
    public static final Block ACACIA_SHELF_BLOCK = ACACIA_SHELF_SET.shelf();
    public static final Block DARK_OAK_SHELF_BLOCK = DARK_OAK_SHELF_SET.shelf();
    public static final Block MANGROVE_SHELF_BLOCK = MANGROVE_SHELF_SET.shelf();
    public static final Block BAMBOO_SHELF_BLOCK = BAMBOO_SHELF_SET.shelf();
    public static final Block CRIMSON_SHELF_BLOCK = CRIMSON_SHELF_SET.shelf();
    public static final Block WARPED_SHELF_BLOCK = WARPED_SHELF_SET.shelf();
    public static final Block CHERRY_SHELF_BLOCK = CHERRY_SHELF_SET.shelf();
    public static final Block PALE_OAK_SHELF_BLOCK = PALE_OAK_SHELF_SET.shelf();

     //BACKPORTED / CUSTOM WOOD FAMILIES

    public static final WoodSet CHERRY_SET = registerWoodSet("cherry", ModSignTypes.CHERRY, ModItemGroup.ModBlocksTab);
    public static final WoodSet PALE_OAK_SET = registerWoodSet("pale_oak", ModSignTypes.PALE_OAK, ModItemGroup.ModBlocksTab);
    public static final BambooWoodSet BAMBOO_WOOD_SET = registerBambooWoodSet("bamboo", ModSignTypes.BAMBOO, ModItemGroup.ModBlocksTab);

    public static final Block CHERRY_LOG = CHERRY_SET.log();
    public static final Block CHERRY_WOOD = CHERRY_SET.wood();
    public static final Block STRIPPED_CHERRY_LOG = CHERRY_SET.strippedLog();
    public static final Block STRIPPED_CHERRY_WOOD = CHERRY_SET.strippedWood();
    public static final Block CHERRY_PLANKS = CHERRY_SET.planks();
    public static final Block CHERRY_SLAB = CHERRY_SET.slab();
    public static final Block CHERRY_STAIRS = CHERRY_SET.stairs();
    public static final Block CHERRY_FENCE = CHERRY_SET.fence();
    public static final Block CHERRY_FENCE_GATE = CHERRY_SET.fenceGate();
    public static final Block CHERRY_DOOR = CHERRY_SET.door();
    public static final Block CHERRY_TRAPDOOR = CHERRY_SET.trapdoor();
    public static final Block CHERRY_BUTTON = CHERRY_SET.button();
    public static final Block CHERRY_PRESSURE_PLATE = CHERRY_SET.pressurePlate();
    public static final Block CHERRY_SIGN = CHERRY_SET.sign();
    public static final Block CHERRY_WALL_SIGN = CHERRY_SET.wallSign();
    public static final Item CHERRY_SIGN_ITEM = CHERRY_SET.signItem();

    public static final Block PALE_OAK_LOG = PALE_OAK_SET.log();
    public static final Block PALE_OAK_WOOD = PALE_OAK_SET.wood();
    public static final Block STRIPPED_PALE_OAK_LOG = PALE_OAK_SET.strippedLog();
    public static final Block STRIPPED_PALE_OAK_WOOD = PALE_OAK_SET.strippedWood();
    public static final Block PALE_OAK_PLANKS = PALE_OAK_SET.planks();
    public static final Block PALE_OAK_SLAB = PALE_OAK_SET.slab();
    public static final Block PALE_OAK_STAIRS = PALE_OAK_SET.stairs();
    public static final Block PALE_OAK_FENCE = PALE_OAK_SET.fence();
    public static final Block PALE_OAK_FENCE_GATE = PALE_OAK_SET.fenceGate();
    public static final Block PALE_OAK_DOOR = PALE_OAK_SET.door();
    public static final Block PALE_OAK_TRAPDOOR = PALE_OAK_SET.trapdoor();
    public static final Block PALE_OAK_BUTTON = PALE_OAK_SET.button();
    public static final Block PALE_OAK_PRESSURE_PLATE = PALE_OAK_SET.pressurePlate();
    public static final Block PALE_OAK_SIGN = PALE_OAK_SET.sign();
    public static final Block PALE_OAK_WALL_SIGN = PALE_OAK_SET.wallSign();
    public static final Item PALE_OAK_SIGN_ITEM = PALE_OAK_SET.signItem();

    public static final Block BAMBOO_BLOCK = BAMBOO_WOOD_SET.bambooBlock();
    public static final Block STRIPPED_BAMBOO_BLOCK = BAMBOO_WOOD_SET.strippedBambooBlock();
    public static final Block BAMBOO_PLANKS = BAMBOO_WOOD_SET.planks();
    public static final Block BAMBOO_SLAB = BAMBOO_WOOD_SET.slab();
    public static final Block BAMBOO_STAIRS = BAMBOO_WOOD_SET.stairs();
    public static final Block BAMBOO_FENCE = BAMBOO_WOOD_SET.fence();
    public static final Block BAMBOO_FENCE_GATE = BAMBOO_WOOD_SET.fenceGate();
    public static final Block BAMBOO_DOOR = BAMBOO_WOOD_SET.door();
    public static final Block BAMBOO_TRAPDOOR = BAMBOO_WOOD_SET.trapdoor();
    public static final Block BAMBOO_BUTTON = BAMBOO_WOOD_SET.button();
    public static final Block BAMBOO_PRESSURE_PLATE = BAMBOO_WOOD_SET.pressurePlate();
    public static final Block BAMBOO_MOSAIC = BAMBOO_WOOD_SET.mosaic();
    public static final Block BAMBOO_MOSAIC_SLAB = BAMBOO_WOOD_SET.mosaicSlab();
    public static final Block BAMBOO_MOSAIC_STAIRS = BAMBOO_WOOD_SET.mosaicStairs();
    public static final Block BAMBOO_SIGN = BAMBOO_WOOD_SET.sign();
    public static final Block BAMBOO_WALL_SIGN = BAMBOO_WOOD_SET.wallSign();
    public static final Item BAMBOO_SIGN_ITEM = BAMBOO_WOOD_SET.signItem();

     //TORCHES / RAILS

    public static final Block WATER_TORCH_BLOCK = registerBlock(
            "water_torch",
            new WaterTorchBlock(FabricBlockSettings.copyOf(Blocks.TORCH)),
            ModItemGroup.ModToolTab
    );

    public static final Block WALL_WATER_TORCH_BLOCK = registerBlockWithoutBlockItem(
            "wall_water_torch",
            new WallWaterTorchBlock(FabricBlockSettings.copyOf(Blocks.TORCH).dropsLike(WATER_TORCH_BLOCK))
    );

    public static final Block SPEED_RAIL_BLOCK = registerBlock(
            "speed_rail_block",
            new SpeedRailBlock(FabricBlockSettings.copyOf(Blocks.POWERED_RAIL)),
            ModItemGroup.ModBlocksTab
    );


     //SPONGES

    public static final SpongeTierSet COMPRESSED_SPONGE_SET = registerSpongeTier(
            "compressed_sponge",
            new CompressedSpongeBlock(FabricBlockSettings.of(Material.SPONGE).strength(0.6F)),
            0.6F,
            ModItemGroup.ModBlocksTab
    );

    public static final SpongeTierSet DOUBLE_COMPRESSED_SPONGE_SET = registerSpongeTier(
            "double_compressed_sponge",
            new DoubleCompressedSpongeBlock(FabricBlockSettings.of(Material.SPONGE).strength(0.8F)),
            0.8F,
            ModItemGroup.ModBlocksTab
    );

    public static final SpongeTierSet TRIPLE_COMPRESSED_SPONGE_SET = registerSpongeTier(
            "triple_compressed_sponge",
            new TripleCompressedSpongeBlock(FabricBlockSettings.of(Material.SPONGE).strength(1.0F)),
            1.0F,
            ModItemGroup.ModBlocksTab
    );

    public static final Block COMPRESSED_SPONGE = COMPRESSED_SPONGE_SET.dry();
    public static final Block WET_COMPRESSED_SPONGE = COMPRESSED_SPONGE_SET.wet();

    public static final Block DOUBLE_COMPRESSED_SPONGE = DOUBLE_COMPRESSED_SPONGE_SET.dry();
    public static final Block WET_DOUBLE_COMPRESSED_SPONGE = DOUBLE_COMPRESSED_SPONGE_SET.wet();

    public static final Block TRIPLE_COMPRESSED_SPONGE = TRIPLE_COMPRESSED_SPONGE_SET.dry();
    public static final Block WET_TRIPLE_COMPRESSED_SPONGE = TRIPLE_COMPRESSED_SPONGE_SET.wet();


     //CAULDRONS / HAT

    public static final Block DYED_WATER_CAULDRON_BLOCK = registerBlock(
            "dyed_water_cauldron",
            new DyedWaterCauldronBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON).nonOpaque()),
            ModItemGroup.ModBlocksTab
    );

    public static final Block MODDED_FLUID_CAULDRON_BLOCK = registerBlock(
            "modded_fluid_cauldron",
            new ModdedFluidCauldronBlock(
                    FabricBlockSettings.copyOf(Blocks.CAULDRON)
                            .nonOpaque()
                            .luminance(state -> {
                                if (!(state.getBlock() instanceof ModdedFluidCauldronBlock)) return 0;
                                return switch (state.get(ModdedFluidCauldronBlock.CONTENT)) {
                                    case MAGMA -> 15;
                                    case HONEY -> 4;
                                    case SLIME, ICE -> 0;
                                };
                            })
            ),
            ModItemGroup.ModBlocksTab
    );

    public static final Block SANTA_HAT = registerBlockWithoutBlockItem(
            "santa_hat",
            new SantaHat(FabricBlockSettings.of(Material.WOOL).strength(1f, 1f))
    );

    public static final Item SANTA_HAT_ITEM = Registry.register(
            Registry.ITEM,
            new Identifier(BeyondTheBlock.MOD_ID, "santa_hat"),
            new BlockItem(
                    SANTA_HAT,
                    new FabricItemSettings()
                            .group(ModItemGroup.ModArmourTab)
                            .maxCount(1)
                            .equipmentSlot(stack -> EquipmentSlot.HEAD)
            )
    );


     //ORES / STORAGE BLOCKS


    public static final Block MIRANITE_ORE = registerOre("miranite_ore", 2f, 2f, 3, 7);
    public static final Block DEEPSLATE_MIRANITE_ORE = registerOre("deepslate_miranite_ore", 2f, 2f, 3, 7);
    public static final Block NETHER_MIRANITE_ORE = registerOre("nether_miranite_ore", 2f, 2f, 3, 7);
    public static final Block END_MIRANITE_ORE = registerOre("end_miranite_ore", 2f, 2f, 3, 7);
    public static final Block MIRANITE_BLOCK = registerStorageBlock("miranite_block", Material.AMETHYST, 4f, 2f);
    public static final Block RAW_MIRANITE_BLOCK = registerStorageBlock("raw_miranite_block", Material.AMETHYST, 4f, 2f);

    public static final Block CHROMITE_ORE = registerOre("chromite_ore", 2f, 2f, 3, 7);
    public static final Block DEEPSLATE_CHROMITE_ORE = registerOre("deepslate_chromite_ore", 2f, 2f, 3, 7);
    public static final Block NETHER_CHROMITE_ORE = registerOre("nether_chromite_ore", 2f, 2f, 3, 7);
    public static final Block END_CHROMITE_ORE = registerOre("end_chromite_ore", 2f, 2f, 3, 7);
    public static final Block CHROMITE_BLOCK = registerStorageBlock("chromite_block", Material.AMETHYST, 4f, 2f);

    public static final Block NOCTURNITE_ORE = registerOre("nocturnite_ore", 5f, 4f, 3, 7);
    public static final Block DEEPSLATE_NOCTURNITE_ORE = registerOre("deepslate_nocturnite_ore", 5f, 4f, 3, 7);
    public static final Block NETHER_NOCTURNITE_ORE = registerOre("nether_nocturnite_ore", 5f, 4f, 3, 7);
    public static final Block END_NOCTURNITE_ORE = registerOre("end_nocturnite_ore", 5f, 4f, 3, 7);
    public static final Block NOCTURNITE_BLOCK = registerStorageBlock("nocturnite_block", Material.AMETHYST, 5f, 4f);

    public static final Block AMBERINE_ORE = registerLuminousOre("amberine_ore", 2f, 2f, 3, 7, 5);
    public static final Block DEEPSLATE_AMBERINE_ORE = registerLuminousOre("deepslate_amberine_ore", 2f, 2f, 3, 7, 5);
    public static final Block AMBERINE_BLOCK = registerLuminousStorageBlock("amberine_block", Material.AMETHYST, 2f, 2f, 15);

    public static final Block ROSETTE_ORE = registerOre("rosette_ore", 2f, 2f, 3, 7);
    public static final Block DEEPSLATE_ROSETTE_ORE = registerOre("deepslate_rosette_ore", 2f, 2f, 3, 7);
    public static final Block NETHER_ROSETTE_ORE = registerOre("nether_rosette_ore", 2f, 2f, 3, 7);
    public static final Block END_ROSETTE_ORE = registerOre("end_rosette_ore", 2f, 2f, 3, 7);
    public static final Block ROSETTE_BLOCK = registerStorageBlock("rosette_block", Material.AMETHYST, 4f, 2f);
    public static final Block RAW_ROSETTE_BLOCK = registerStorageBlock("raw_rosette_block", Material.AMETHYST, 4f, 2f);

    public static final Block AZUROS_ORE = registerOre("azuros_ore", 3f, 3f, 3, 7);
    public static final Block DEEPSLATE_AZUROS_ORE = registerOre("deepslate_azuros_ore", 3f, 3f, 3, 7);
    public static final Block NETHER_AZUROS_ORE = registerOre("nether_azuros_ore", 3f, 3f, 3, 7);
    public static final Block END_AZUROS_ORE = registerOre("end_azuros_ore", 3f, 3f, 3, 7);
    public static final Block AZUROS_BLOCK = registerStorageBlock("azuros_block", Material.AMETHYST, 4f, 3f);
    public static final Block RAW_AZUROS_BLOCK = registerStorageBlock("raw_azuros_block", Material.AMETHYST, 4f, 3f);

    public static final Block INDIGRA_ORE = registerOre("indigra_ore", 2f, 2f, 3, 7);
    public static final Block DEEPSLATE_INDIGRA_ORE = registerOre("deepslate_indigra_ore", 2f, 2f, 3, 7);
    public static final Block NETHER_INDIGRA_ORE = registerOre("nether_indigra_ore", 2f, 2f, 3, 7);
    public static final Block END_INDIGRA_ORE = registerOre("end_indigra_ore", 2f, 3f, 3, 7);
    public static final Block INDIGRA_BLOCK = registerStorageBlock("indigra_block", Material.AMETHYST, 4f, 3f);

    public static final Block XIRION_ORE = registerOre("xirion_ore", 4f, 4f, 3, 7);
    public static final Block DEEPSLATE_XIRION_ORE = registerOre("deepslate_xirion_ore", 4f, 4f, 3, 7);
    public static final Block NETHER_XIRION_ORE = registerOre("nether_xirion_ore", 4f, 4f, 3, 7);
    public static final Block END_XIRION_ORE = registerOre("end_xirion_ore", 4f, 4f, 3, 7);
    public static final Block XIRION_BLOCK = registerStorageBlock("xirion_block", Material.AMETHYST, 4f, 4f);

    public static final Block RUBY_BLOCK = registerStorageBlock("ruby_block", Material.AMETHYST, 4f, 2f);
    public static final Block RUBY_ORE = registerOre("ruby_ore", 2f, 2f, 3, 7);
    public static final Block DEEPSLATE_RUBY_ORE = registerOre("deepslate_ruby_ore", 4f, 4f, 7, 15);


     //SPECIAL BLOCKS

    public static final Block ANIMATED_BLOCK = registerBlockWithoutBlockItem(
            "animated_block",
            new AnimatedBlock(FabricBlockSettings.of(Material.METAL).nonOpaque())
    );

    public static final Block EXPERIENCE_BLOCK = registerBlock(
            "experience_block",
            new ExperienceBlock(FabricBlockSettings.of(Material.AMETHYST).strength(3f, 3f).requiresTool()),
            ModItemGroup.ModBlocksTab
    );


     //SHRINE FAMILY

    public static final ShrineBlockSet SHRINE_SET = registerShrineBlockSet(ModItemGroup.ModBlocksTab);

    public static final Block PEDESTAL_BLOCK = SHRINE_SET.pedestal();
    public static final Block SHRINE_CORE_BLOCK = SHRINE_SET.core();
    public static final Block SHRINE_HEADS_BLOCK = SHRINE_SET.heads();
    public static final Block SHRINE_DECOR_BLOCK = SHRINE_SET.decor();
    public static final Block DOUBLE_INPUT_BLOCK = SHRINE_SET.doubleInput();
    public static final Block SINGLE_INPUT_BLOCK = SHRINE_SET.singleInput();

    /*
     * ============================================================
     *  PUBLIC INIT
     * ============================================================
     */

    public static void registerModBlocks() {
        BeyondTheBlock.LOGGER.info("Registering Mod Blocks for " + BeyondTheBlock.MOD_ID);
    }

    /*
     * ============================================================
     *  GENERIC REGISTRY HELPERS
     * ============================================================
     */

    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(BeyondTheBlock.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        return Registry.register(
                Registry.ITEM,
                new Identifier(BeyondTheBlock.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab))
        );
    }

    private static Block registerBlockWithoutBlockItem(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(BeyondTheBlock.MOD_ID, name), block);
    }

    /*
     * ============================================================
     *  SMALL FEATURE HELPERS
     * ============================================================
     */

    private static ShelfSet registerShelfSet(String woodName, ItemGroup tab) {
        Block shelf = registerBlock(
                woodName + "_shelf_block",
                new ShelfBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)),
                tab
        );
        return new ShelfSet(shelf);
    }

    private static SpongeTierSet registerSpongeTier(
            String dryName,
            Block dryBlock,
            float wetStrength,
            ItemGroup tab
    ) {
        Block dry = registerBlock(dryName, dryBlock, tab);

        Block wet = registerBlock(
                "wet_" + dryName,
                new WetCompressedSpongeBlock(
                        FabricBlockSettings.of(Material.SPONGE).strength(wetStrength),
                        () -> dry
                ),
                tab
        );

        return new SpongeTierSet(dry, wet);
    }

    private static ShrineBlockSet registerShrineBlockSet(ItemGroup tab) {
        Block pedestal = registerBlock(
                "pedestal_block",
                new PedestalBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).nonOpaque()),
                tab
        );

        Block core = registerBlock(
                "shrine_core_block",
                new ShrineCoreBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).nonOpaque()),
                tab
        );

        Block heads = registerBlock(
                "shrine_heads_block",
                new ShrineHeadsBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).nonOpaque()),
                tab
        );

        Block decor = registerBlock(
                "shrine_decor_block",
                new ShrineDecorBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).nonOpaque()),
                tab
        );

        Block doubleInput = registerBlock(
                "double_input_block",
                new DoubleInputBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).nonOpaque()),
                tab
        );

        Block singleInput = registerBlock(
                "single_input_block",
                new SingleInputBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).nonOpaque()),
                tab
        );

        return new ShrineBlockSet(pedestal, core, heads, decor, doubleInput, singleInput);
    }

    private static Block registerOre(String name, float hardness, float resistance, int minXp, int maxXp) {
        return registerBlock(
                name,
                new OreBlock(
                        FabricBlockSettings.of(Material.STONE).strength(hardness, resistance).requiresTool(),
                        UniformIntProvider.create(minXp, maxXp)
                ),
                ModItemGroup.ModBlocksTab
        );
    }

    private static Block registerLuminousOre(String name, float hardness, float resistance, int minXp, int maxXp, int luminance) {
        return registerBlock(
                name,
                new OreBlock(
                        FabricBlockSettings.of(Material.STONE).strength(hardness, resistance).luminance(luminance).requiresTool(),
                        UniformIntProvider.create(minXp, maxXp)
                ),
                ModItemGroup.ModBlocksTab
        );
    }

    private static Block registerStorageBlock(String name, Material material, float hardness, float resistance) {
        return registerBlock(
                name,
                new Block(FabricBlockSettings.of(material).strength(hardness, resistance).requiresTool()),
                ModItemGroup.ModBlocksTab
        );
    }

    private static Block registerLuminousStorageBlock(String name, Material material, float hardness, float resistance, int luminance) {
        return registerBlock(
                name,
                new Block(FabricBlockSettings.of(material).strength(hardness, resistance).luminance(luminance).requiresTool()),
                ModItemGroup.ModBlocksTab
        );
    }

    /*
     * ============================================================
     *  WOOD FAMILY REGISTRATION
     * ============================================================
     */

    private static WoodSet registerWoodSet(String name, SignType signType, ItemGroup tab) {
        Block log = registerBlock(name + "_log", createLogBlock(), tab);
        Block wood = registerBlock(name + "_wood", createWoodBlock(), tab);
        Block strippedLog = registerBlock("stripped_" + name + "_log", createStrippedLogBlock(), tab);
        Block strippedWood = registerBlock("stripped_" + name + "_wood", createStrippedWoodBlock(), tab);

        Block planks = registerBlock(name + "_planks", createPlanksBlock(), tab);
        Block slab = registerBlock(name + "_slab", createSlabBlock(), tab);
        Block stairs = registerBlock(name + "_stairs", createStairsBlock(planks), tab);
        Block fence = registerBlock(name + "_fence", createFenceBlock(), tab);
        Block fenceGate = registerBlock(name + "_fence_gate", createFenceGateBlock(), tab);
        Block door = registerBlock(name + "_door", createDoorBlock(), tab);
        Block trapdoor = registerBlock(name + "_trapdoor", createTrapdoorBlock(), tab);
        Block button = registerBlock(name + "_button", createButtonBlock(), tab);
        Block pressurePlate = registerBlock(name + "_pressure_plate", createPressurePlateBlock(), tab);

        Block sign = registerBlockWithoutBlockItem(name + "_sign", createSignBlock(signType));
        Block wallSign = registerBlockWithoutBlockItem(name + "_wall_sign", createWallSignBlock(signType));
        Item signItem = registerSignItem(name + "_sign", sign, wallSign, tab);

        return new WoodSet(
                log,
                wood,
                strippedLog,
                strippedWood,
                planks,
                slab,
                stairs,
                fence,
                fenceGate,
                door,
                trapdoor,
                button,
                pressurePlate,
                sign, wallSign, signItem
        );
    }

    private static BambooWoodSet registerBambooWoodSet(String name, SignType signType, ItemGroup tab) {
        Block bambooBlock = registerBlock(name + "_block", createLogBlock(), tab);
        Block strippedBambooBlock = registerBlock("stripped_" + name + "_block", createStrippedLogBlock(), tab);

        Block planks = registerBlock(name + "_planks", createPlanksBlock(), tab);
        Block slab = registerBlock(name + "_slab", createSlabBlock(), tab);
        Block stairs = registerBlock(name + "_stairs", createStairsBlock(planks), tab);
        Block fence = registerBlock(name + "_fence", createFenceBlock(), tab);
        Block fenceGate = registerBlock(name + "_fence_gate", createFenceGateBlock(), tab);
        Block door = registerBlock(name + "_door", createDoorBlock(), tab);
        Block trapdoor = registerBlock(name + "_trapdoor", createTrapdoorBlock(), tab);
        Block button = registerBlock(name + "_button", createButtonBlock(), tab);
        Block pressurePlate = registerBlock(name + "_pressure_plate", createPressurePlateBlock(), tab);

        Block mosaic = registerBlock(name + "_mosaic", createPlanksBlock(), tab);
        Block mosaicSlab = registerBlock(name + "_mosaic_slab", createSlabBlock(), tab);
        Block mosaicStairs = registerBlock(name + "_mosaic_stairs", createStairsBlock(mosaic), tab);

        Block sign = registerBlockWithoutBlockItem(name + "_sign", createSignBlock(signType));
        Block wallSign = registerBlockWithoutBlockItem(name + "_wall_sign", createWallSignBlock(signType));
        Item signItem = registerSignItem(name + "_sign", sign, wallSign, tab);

        return new BambooWoodSet(
                bambooBlock,
                strippedBambooBlock,
                planks,
                slab,
                stairs,
                fence,
                fenceGate,
                door,
                trapdoor,
                button,
                pressurePlate,
                mosaic,
                mosaicSlab,
                mosaicStairs,
                sign, wallSign, signItem
        );
    }

    /*
     * ============================================================
     *  WOOD BLOCK FACTORIES
     * ============================================================
     */


    private static Block createSignBlock(SignType type) {
        return new ModSignBlock(FabricBlockSettings.copyOf(Blocks.OAK_SIGN), type);
    }

    private static Block createWallSignBlock(SignType type) {
        return new ModWallSignBlock(FabricBlockSettings.copyOf(Blocks.OAK_WALL_SIGN), type);
    }

    private static Item registerSignItem(String name, Block sign, Block wallSign, ItemGroup tab) {
        return Registry.register(
                Registry.ITEM,
                new Identifier(BeyondTheBlock.MOD_ID, name),
                new SignItem(new FabricItemSettings().group(tab).maxCount(16), sign, wallSign)
        );
    }
    private static Block createLogBlock() {
        return new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    }

    private static Block createWoodBlock() {
        return new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
    }

    private static Block createStrippedLogBlock() {
        return new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG));
    }

    private static Block createStrippedWoodBlock() {
        return new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_WOOD));
    }

    private static Block createPlanksBlock() {
        return new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    }

    private static Block createSlabBlock() {
        return new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
    }

    private static Block createStairsBlock(Block base) {
        return new StairsBlock(base.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    }

    private static Block createFenceBlock() {
        return new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    }

    private static Block createFenceGateBlock() {
        return new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE));
    }

    private static Block createDoorBlock() {
        return new DoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_DOOR));
    }

    private static Block createTrapdoorBlock() {
        return new TrapdoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_TRAPDOOR));
    }

    private static Block createButtonBlock() {
        return new WoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON));
    }

    private static Block createPressurePlateBlock() {
        return new PressurePlateBlock(
                PressurePlateBlock.ActivationRule.EVERYTHING,
                FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE)
        );
    }
}