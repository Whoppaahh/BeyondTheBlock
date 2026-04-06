package net.ryan.beyond_the_block.content.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
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
import net.ryan.beyond_the_block.content.item.ModItemGroup;
import net.ryan.beyond_the_block.core.BeyondTheBlock;


public class ModBlocks {

    public static final Block WOODCUTTER_BLOCK = registerBlock("woodcutter", new WoodcutterBlock(FabricBlockSettings.copy(Blocks.STONECUTTER)), ModItemGroup.ModBlocksTab);
    public static final Block CHISELED_BOOKSHELF = registerBlock("chiseled_bookshelf", new ChiseledBookshelfBlock(FabricBlockSettings.of(Material.WOOD)), ModItemGroup.ModBlocksTab);

    public static final Block OAK_SHELF_BLOCK = registerBlock("oak_shelf_block", new ShelfBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)), ModItemGroup.ModBlocksTab);
    public static final Block SPRUCE_SHELF_BLOCK = registerBlock("spruce_shelf_block", new ShelfBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)), ModItemGroup.ModBlocksTab);
    public static final Block BIRCH_SHELF_BLOCK = registerBlock("birch_shelf_block", new ShelfBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)), ModItemGroup.ModBlocksTab);
    public static final Block JUNGLE_SHELF_BLOCK = registerBlock("jungle_shelf_block", new ShelfBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)), ModItemGroup.ModBlocksTab);
    public static final Block ACACIA_SHELF_BLOCK = registerBlock("acacia_shelf_block", new ShelfBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)), ModItemGroup.ModBlocksTab);
    public static final Block DARK_OAK_SHELF_BLOCK = registerBlock("dark_oak_shelf_block", new ShelfBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)), ModItemGroup.ModBlocksTab);
    public static final Block MANGROVE_SHELF_BLOCK = registerBlock("mangrove_shelf_block", new ShelfBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)), ModItemGroup.ModBlocksTab);
    public static final Block BAMBOO_SHELF_BLOCK = registerBlock("bamboo_shelf_block", new ShelfBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)), ModItemGroup.ModBlocksTab);
    public static final Block CRIMSON_SHELF_BLOCK = registerBlock("crimson_shelf_block", new ShelfBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)), ModItemGroup.ModBlocksTab);
    public static final Block WARPED_SHELF_BLOCK = registerBlock("warped_shelf_block", new ShelfBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)), ModItemGroup.ModBlocksTab);



    public static final Block WATER_TORCH_BLOCK = registerBlock("water_torch", new WaterTorchBlock(FabricBlockSettings.copyOf(Blocks.TORCH)), ModItemGroup.ModToolTab);
    public static final Block WALL_WATER_TORCH_BLOCK = registerBlockWithoutBlockItem("wall_water_torch", new WallWaterTorchBlock(FabricBlockSettings.copyOf(Blocks.TORCH).dropsLike(ModBlocks.WATER_TORCH_BLOCK)), ModItemGroup.ModToolTab);

    public static final Block SPEED_RAIL_BLOCK = registerBlock("speed_rail_block", new SpeedRailBlock(FabricBlockSettings.copyOf(Blocks.POWERED_RAIL)), ModItemGroup.ModBlocksTab);

    public static final Block COMPRESSED_SPONGE = registerBlock("compressed_sponge", new CompressedSpongeBlock(FabricBlockSettings.of(Material.SPONGE).strength(0.6F)), ModItemGroup.ModBlocksTab);
    public static final Block DOUBLE_COMPRESSED_SPONGE = registerBlock("double_compressed_sponge", new DoubleCompressedSpongeBlock(FabricBlockSettings.of(Material.SPONGE).strength(0.8F)), ModItemGroup.ModBlocksTab);
    public static final Block TRIPLE_COMPRESSED_SPONGE = registerBlock("triple_compressed_sponge", new TripleCompressedSpongeBlock(FabricBlockSettings.of(Material.SPONGE).strength(1.0F)), ModItemGroup.ModBlocksTab);

    public static final Block WET_COMPRESSED_SPONGE = registerBlock("wet_compressed_sponge", new WetCompressedSpongeBlock(FabricBlockSettings.of(Material.SPONGE).strength(0.6F), () -> COMPRESSED_SPONGE),  ModItemGroup.ModBlocksTab);
    public static final Block WET_DOUBLE_COMPRESSED_SPONGE = registerBlock("wet_double_compressed_sponge", new WetCompressedSpongeBlock(FabricBlockSettings.of(Material.SPONGE).strength(0.8F), () -> DOUBLE_COMPRESSED_SPONGE), ModItemGroup.ModBlocksTab);
    public static final Block WET_TRIPLE_COMPRESSED_SPONGE = registerBlock("wet_triple_compressed_sponge", new WetCompressedSpongeBlock(FabricBlockSettings.of(Material.SPONGE).strength(1.0F), () -> TRIPLE_COMPRESSED_SPONGE), ModItemGroup.ModBlocksTab);

    public static final Block DYED_WATER_CAULDRON_BLOCK = registerBlock("dyed_water_cauldron", new DyedWaterCauldronBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON).nonOpaque()), ModItemGroup.ModBlocksTab);
    public static final Block MODDED_FLUID_CAULDRON_BLOCK = registerBlock("modded_fluid_cauldron", new ModdedFluidCauldronBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON).nonOpaque().luminance(state -> {
        if(!(state.getBlock() instanceof ModdedFluidCauldronBlock block)) return 0;
        return switch (state.get(ModdedFluidCauldronBlock.CONTENT)){
            case MAGMA -> 15;
            case HONEY -> 4;
            case SLIME, ICE -> 0;
        };
    })), ModItemGroup.ModBlocksTab);
    public static final Block SANTA_HAT = registerBlockWithoutBlockItem("santa_hat",
            new SantaHat(FabricBlockSettings.of(Material.WOOL).strength(1f,1f)), ModItemGroup.ModArmourTab);
    public static final Item SANTA_HAT_ITEM = Registry.register(Registry.ITEM, new Identifier(BeyondTheBlock.MOD_ID, "santa_hat"), new BlockItem(SANTA_HAT, new FabricItemSettings().group(ModItemGroup.ModArmourTab)
            .maxCount(1)
            .equipmentSlot(stack -> EquipmentSlot.HEAD))); // This makes it wearable!

    public static final Block GEM_BLOCK = registerBlock("gem_block",
            new GemBlock(FabricBlockSettings.of(Material.METAL).strength(2f,2f).requiresTool().nonOpaque()), ModItemGroup.ModBlocksTab);

    public static final Block PLAYER_VAULT_BLOCK = registerBlock("player_vault",
            new PlayerVaultBlock(FabricBlockSettings.of(Material.METAL).strength(2f,2f).requiresTool().nonOpaque()), ModItemGroup.ModBlocksTab);

    public static final Block INFI_FURNACE_BLOCK = registerBlock("infi_furnace",
            new InfiFurnaceBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool().luminance(state -> state.get(InfiFurnaceBlock.LIT) ? 15 : 0).nonOpaque()), ModItemGroup.ModBlocksTab);

    public static final Block DECRAFTER_BLOCK = registerBlock("decrafter_block",
            new DecrafterBlock(FabricBlockSettings.of(Material.WOOD).strength(2f,2f)), ModItemGroup.ModBlocksTab);

    public static final Block LAVA_LAMP_BLOCK = registerBlock("lava_lamp_block",
            new Block(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool().nonOpaque().luminance(15)), ModItemGroup.ModBlocksTab);

    //region Miranite
       public static final Block MIRANITE_ORE = registerBlock("miranite_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block DEEPSLATE_MIRANITE_ORE = registerBlock("deepslate_miranite_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block NETHER_MIRANITE_ORE = registerBlock("nether_miranite_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block END_MIRANITE_ORE = registerBlock("end_miranite_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block MIRANITE_BLOCK = registerBlock("miranite_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(4f,2f).requiresTool()), ModItemGroup.ModBlocksTab);
    public static final Block RAW_MIRANITE_BLOCK = registerBlock("raw_miranite_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(4f,2f).requiresTool()), ModItemGroup.ModBlocksTab);
//endregion
    //region Chromite
    public static final Block CHROMITE_ORE = registerBlock("chromite_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block DEEPSLATE_CHROMITE_ORE = registerBlock("deepslate_chromite_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block NETHER_CHROMITE_ORE = registerBlock("nether_chromite_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block END_CHROMITE_ORE = registerBlock("end_chromite_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block CHROMITE_BLOCK = registerBlock("chromite_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(4f,2f).requiresTool()), ModItemGroup.ModBlocksTab);
    //endregion
    //region Nocturnite
    public static final Block NOCTURNITE_ORE = registerBlock("nocturnite_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(5f,4f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block DEEPSLATE_NOCTURNITE_ORE = registerBlock("deepslate_nocturnite_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(5f,4f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block NETHER_NOCTURNITE_ORE = registerBlock("nether_nocturnite_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(5f,4f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block END_NOCTURNITE_ORE = registerBlock("end_nocturnite_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(5f,4f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block NOCTURNITE_BLOCK = registerBlock("nocturnite_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(5f,4f).requiresTool()), ModItemGroup.ModBlocksTab);
    //endregion
    //region Amberine
    public static final Block AMBERINE_ORE = registerBlock("amberine_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).luminance(5).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block DEEPSLATE_AMBERINE_ORE = registerBlock("deepslate_amberine_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).luminance(5).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block AMBERINE_BLOCK = registerBlock("amberine_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(2f,2f).luminance(15).requiresTool()), ModItemGroup.ModBlocksTab);
    //endregion
    //region Rosette
    public static final Block ROSETTE_ORE = registerBlock("rosette_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block DEEPSLATE_ROSETTE_ORE = registerBlock("deepslate_rosette_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block NETHER_ROSETTE_ORE = registerBlock("nether_rosette_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block END_ROSETTE_ORE = registerBlock("end_rosette_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block ROSETTE_BLOCK = registerBlock("rosette_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(4f,2f).requiresTool()), ModItemGroup.ModBlocksTab);
    public static final Block RAW_ROSETTE_BLOCK = registerBlock("raw_rosette_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(4f,2f).requiresTool()), ModItemGroup.ModBlocksTab);
//endregion
    //region Azuros
    public static final Block AZUROS_ORE = registerBlock("azuros_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(3f,3f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block DEEPSLATE_AZUROS_ORE = registerBlock("deepslate_azuros_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(3f,3f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block NETHER_AZUROS_ORE = registerBlock("nether_azuros_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(3f,3f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block END_AZUROS_ORE = registerBlock("end_azuros_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(3f,3f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block AZUROS_BLOCK = registerBlock("azuros_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(4f,3f).requiresTool()), ModItemGroup.ModBlocksTab);
    public static final Block RAW_AZUROS_BLOCK = registerBlock("raw_azuros_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(4f,3f).requiresTool()), ModItemGroup.ModBlocksTab);
    //endregion
    //region Indigra
    public static final Block INDIGRA_ORE = registerBlock("indigra_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block DEEPSLATE_INDIGRA_ORE = registerBlock("deepslate_indigra_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block NETHER_INDIGRA_ORE = registerBlock("nether_indigra_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block END_INDIGRA_ORE = registerBlock("end_indigra_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,3f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block INDIGRA_BLOCK = registerBlock("indigra_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(4f,3f).requiresTool()), ModItemGroup.ModBlocksTab);
    //endregion
    //region Xirion
    public static final Block XIRION_ORE = registerBlock("xirion_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(4f,4f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block DEEPSLATE_XIRION_ORE = registerBlock("deepslate_xirion_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(4f,4f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block NETHER_XIRION_ORE = registerBlock("nether_xirion_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(4f,4f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block END_XIRION_ORE = registerBlock("end_xirion_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(4f,4f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block XIRION_BLOCK = registerBlock("xirion_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(4f,4f).requiresTool()), ModItemGroup.ModBlocksTab);
    //endregion
    //region Ruby
    public static final Block RUBY_BLOCK = registerBlock("ruby_block",
            new Block(FabricBlockSettings.of(Material.AMETHYST).strength(4f,2f).requiresTool()), ModItemGroup.ModBlocksTab);
    public static final Block RUBY_ORE = registerBlock("ruby_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f,2f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroup.ModBlocksTab);
    public static final Block DEEPSLATE_RUBY_ORE = registerBlock("deepslate_ruby_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(4f,4f).requiresTool(), UniformIntProvider.create(7, 15)), ModItemGroup.ModBlocksTab);
    //endregion

    public static final Block ANIMATED_BLOCK = registerBlockWithoutBlockItem("animated_block",
            new AnimatedBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()), ModItemGroup.ModBlocksTab);

    public static final Block EXPERIENCE_BLOCK = registerBlock("experience_block",
            new ExperienceBlock(FabricBlockSettings.of(Material.AMETHYST).strength(3f,3f).requiresTool()), ModItemGroup.ModBlocksTab);

    public static final Block PEDESTAL_BLOCK = registerBlock("pedestal_block",
            new PedestalBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).nonOpaque()), ModItemGroup.ModBlocksTab);
    public static final Block SHRINE_CORE_BLOCK = registerBlock("shrine_core_block",
            new ShrineCoreBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).nonOpaque()), ModItemGroup.ModBlocksTab);
    public static final Block SHRINE_HEADS_BLOCK = registerBlock("shrine_heads_block",
            new ShrineHeadsBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).nonOpaque()), ModItemGroup.ModBlocksTab);
    public static final Block SHRINE_DECOR_BLOCK = registerBlock("shrine_decor_block",
            new ShrineDecorBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).nonOpaque()), ModItemGroup.ModBlocksTab);

       public static final Block DOUBLE_INPUT_BLOCK = registerBlock("double_input_block",
            new DoubleInputBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).nonOpaque()), ModItemGroup.ModBlocksTab);
    public static final Block SINGLE_INPUT_BLOCK = registerBlock("single_input_block",
            new SingleInputBlock(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).nonOpaque()), ModItemGroup.ModBlocksTab);

    private static Block registerBlock(String name, Block block, ItemGroup tab){
      registerBlockItem(name, block, tab);
       return Registry.register(Registry.BLOCK, new Identifier(BeyondTheBlock.MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block, ItemGroup tab){
        return Registry.register(Registry.ITEM, new Identifier(BeyondTheBlock.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }
    private static Block registerBlockWithoutBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.BLOCK, new Identifier(BeyondTheBlock.MOD_ID, name), block);
    }

    public static void registerModBlocks(){
        BeyondTheBlock.LOGGER.info("Registering Mod Blocks for " + BeyondTheBlock.MOD_ID);

    }
}
