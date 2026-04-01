package net.ryan.beyond_the_block.client.render.blockentity;

import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.ryan.beyond_the_block.content.blockentity.ModBlockEntities;

public class BlockEntityRenderRegistrar {
    public static void register() {
        registerBlockEntities();
    }
    private static void registerBlockEntities() {
        BlockEntityRendererFactories.register(ModBlockEntities.PEDESTAL_BLOCK_ENTITY, PedestalBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHRINE_CORE_BLOCK_ENTITY, ShrineCoreBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHRINE_HEADS_BLOCK_ENTITY, ShrineHeadsBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHRINE_DECOR_BLOCK_ENTITY, ShrineDecorBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.PLAYER_VAULT_BLOCK_ENTITY, PlayerVaultBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SINGLE_INPUT_BLOCK_ENTITY, SingleInputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.DOUBLE_INPUT_BLOCK_ENTITY, DoubleInputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.ANIMATED_BLOCK_ENTITY, AnimatedBlockRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHELF_BLOCK_ENTITY, ShelfBlockEntityRenderer::new);

    }
}
