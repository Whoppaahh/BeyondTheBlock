package net.ryan.beyond_the_block.content.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.content.blockentity.*;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.mixin.accessors.BlockEntityTypeAccessor;

import java.util.HashSet;
import java.util.Set;

import static net.ryan.beyond_the_block.content.registry.ModBlocks.OAK_HANGING_SIGN;
import static net.ryan.beyond_the_block.content.registry.ModBlocks.OAK_WALL_HANGING_SIGN;

public class ModBlockEntities {

    public static final BlockEntityType<HangingSignBlockEntity> HANGING_SIGN =
            Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "hanging_sign"),
                    FabricBlockEntityTypeBuilder.create(
                            HangingSignBlockEntity::new,
                            OAK_HANGING_SIGN,
                            OAK_WALL_HANGING_SIGN,
                            SPRUCE_HANGING_SIGN,
                            SPRUCE_WALL_HANGING_SIGN
                            // ...all others
                    ).build()
            );

    public static final BlockEntityType<SignBlockEntity> MOD_SIGN_BLOCK_ENTITY =
            Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "mod_sign"),
                    FabricBlockEntityTypeBuilder.create(
                            SignBlockEntity::new,
                            ModBlocks.CHERRY_SIGN,
                            ModBlocks.CHERRY_WALL_SIGN,
                            ModBlocks.PALE_OAK_SIGN,
                            ModBlocks.PALE_OAK_WALL_SIGN,
                            ModBlocks.BAMBOO_SIGN,
                            ModBlocks.BAMBOO_WALL_SIGN
                    ).build()
            );

    public static final BlockEntityType<WoodcutterBlockEntity> WOODCUTTER_BLOCK_ENTITY =
            Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "woodcutter_block_entity"),
                    FabricBlockEntityTypeBuilder.create(
                            WoodcutterBlockEntity::new,
                            ModBlocks.WOODCUTTER_BLOCK
                    ).build()
            );

    public static final BlockEntityType<ChiseledBookshelfBlockEntity> CHISELED_BOOKSHELF_BLOCK_ENTITY =
            Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "chiseled_bookshelf_block_entity"),
                    FabricBlockEntityTypeBuilder.create(
                            ChiseledBookshelfBlockEntity::new,
                            ModBlocks.CHISELED_BOOKSHELF
                    ).build()
            );

    public static final BlockEntityType<ShelfBlockEntity> SHELF_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "shelf_block_entity"),
                    FabricBlockEntityTypeBuilder.create(ShelfBlockEntity::new,
                            ModBlocks.OAK_SHELF_BLOCK,
                            ModBlocks.SPRUCE_SHELF_BLOCK,
                            ModBlocks.BIRCH_SHELF_BLOCK,
                            ModBlocks.JUNGLE_SHELF_BLOCK,
                            ModBlocks.ACACIA_SHELF_BLOCK,
                            ModBlocks.DARK_OAK_SHELF_BLOCK,
                            ModBlocks.MANGROVE_SHELF_BLOCK,
                            ModBlocks.BAMBOO_SHELF_BLOCK,
                            ModBlocks.CRIMSON_SHELF_BLOCK,
                            ModBlocks.WARPED_SHELF_BLOCK,
                            ModBlocks.CHERRY_SHELF_BLOCK,
                            ModBlocks.PALE_OAK_SHELF_BLOCK).build());


    public static final BlockEntityType<DyedWaterCauldronBlockEntity> DYED_WATER_CAULDRON_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "dyed_water_cauldron_block_entity"),
                    FabricBlockEntityTypeBuilder.create(DyedWaterCauldronBlockEntity::new, ModBlocks.DYED_WATER_CAULDRON_BLOCK).build());

    public static final BlockEntityType<ModdedFluidCauldronBlockEntity> MODDED_FLUID_CAULDRON_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "modded_fluid_cauldron_block_entity"),
                    FabricBlockEntityTypeBuilder.create(ModdedFluidCauldronBlockEntity::new, ModBlocks.MODDED_FLUID_CAULDRON_BLOCK).build());
    public static final BlockEntityType<GemBlockEntity> GEM_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "gem_block_entity"),
                    FabricBlockEntityTypeBuilder.create(GemBlockEntity::new, ModBlocks.GEM_BLOCK).build());

    public static final BlockEntityType<InfiFurnaceBlockEntity> INFI_FURNACE_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "infi_furnace_block_entity"),
                    FabricBlockEntityTypeBuilder.create(InfiFurnaceBlockEntity::new, ModBlocks.INFI_FURNACE_BLOCK).build());

    public static final BlockEntityType<DecrafterBlockEntity> DECRAFTER_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "decrafter_block_entity"),
                    FabricBlockEntityTypeBuilder.create(DecrafterBlockEntity::new, ModBlocks.DECRAFTER_BLOCK).build());

    public static final BlockEntityType<PlayerVaultBlockEntity> PLAYER_VAULT_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "player_vault_entity"),
                    FabricBlockEntityTypeBuilder.create(PlayerVaultBlockEntity::new, ModBlocks.PLAYER_VAULT_BLOCK).build());

    public static final BlockEntityType<PedestalBlockEntity> PEDESTAL_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "pedestal_block_entity"),
                    FabricBlockEntityTypeBuilder.create(PedestalBlockEntity::new, ModBlocks.PEDESTAL_BLOCK).build());

    public static final BlockEntityType<ShrineCoreBlockEntity> SHRINE_CORE_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "shrine_core_block_entity"),
                    FabricBlockEntityTypeBuilder.create(ShrineCoreBlockEntity::new, ModBlocks.SHRINE_CORE_BLOCK).build());
    public static final BlockEntityType<ShrineHeadsBlockEntity> SHRINE_HEADS_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "shrine_heads_block_entity"),
                    FabricBlockEntityTypeBuilder.create(ShrineHeadsBlockEntity::new, ModBlocks.SHRINE_HEADS_BLOCK).build());
    public static final BlockEntityType<ShrineDecorBlockEntity> SHRINE_DECOR_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "shrine_decor_block_entity"),
                    FabricBlockEntityTypeBuilder.create(ShrineDecorBlockEntity::new, ModBlocks.SHRINE_DECOR_BLOCK).build());

    public static final BlockEntityType<DoubleInputBlockEntity> DOUBLE_INPUT_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "double_lectern_block_entity"),
                    FabricBlockEntityTypeBuilder.create(DoubleInputBlockEntity::new, ModBlocks.DOUBLE_INPUT_BLOCK).build());
    public static final BlockEntityType<SingleInputBlockEntity> SINGLE_INPUT_BLOCK_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "single_lectern_block_entity"),
                    FabricBlockEntityTypeBuilder.create(SingleInputBlockEntity::new, ModBlocks.SINGLE_INPUT_BLOCK).build());

    public static BlockEntityType<AnimatedBlockEntity> ANIMATED_BLOCK_ENTITY =
             Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "animated_block_entity"),
                    FabricBlockEntityTypeBuilder.create(AnimatedBlockEntity::new,
                            ModBlocks.ANIMATED_BLOCK).build());

    public static void registerModBlockEntities() {
        BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor) BlockEntityType.SIGN;

        Set<Block> blocks = new HashSet<>(accessor.btb$getBlocks());
        blocks.add(ModBlocks.CHERRY_SIGN);
        blocks.add(ModBlocks.CHERRY_WALL_SIGN);
        blocks.add(ModBlocks.PALE_OAK_SIGN);
        blocks.add(ModBlocks.PALE_OAK_WALL_SIGN);
        blocks.add(ModBlocks.BAMBOO_SIGN);
        blocks.add(ModBlocks.BAMBOO_WALL_SIGN);

        accessor.btb$setBlocks(blocks);

        BeyondTheBlock.LOGGER.info("Registering Block Entities for " + BeyondTheBlock.MOD_ID);
    }
}