package net.ryan.beyond_the_block.block.Entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.block.Entity.ShrineEntity.PlayerInputEntity.DoubleInputBlockEntity;
import net.ryan.beyond_the_block.block.Entity.ShrineEntity.PlayerInputEntity.SingleInputBlockEntity;
import net.ryan.beyond_the_block.block.Entity.ShrineEntity.ShrineCoreBlockEntity;
import net.ryan.beyond_the_block.block.Entity.ShrineEntity.ShrineDecorBlockEntity;
import net.ryan.beyond_the_block.block.Entity.ShrineEntity.ShrineHeadsBlockEntity;
import net.ryan.beyond_the_block.block.ModBlocks;

public class ModBlockEntities {

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
        BeyondTheBlock.LOGGER.info("Registering Block Entities for " + BeyondTheBlock.MOD_ID);
    }
}