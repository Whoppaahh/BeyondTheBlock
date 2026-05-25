package net.ryan.beyond_the_block.client.render.layer;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.client.render.blockentity.HangingSignModelLayers;
import net.ryan.beyond_the_block.client.render.blockentity.HangingSignModels;
import net.ryan.beyond_the_block.client.render.entity.model.RaftChestEntityModel;
import net.ryan.beyond_the_block.client.render.entity.model.RaftEntityModel;
import net.ryan.beyond_the_block.client.render.entity.model.WitherZombieModel;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class ModModels {

    public static final EntityModelLayer WITHER_ZOMBIE_MODEL = new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID, "wither_zombie"), "main");
    public static final EntityModelLayer RAFT = new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID, "raft"), "main");
    public static final EntityModelLayer CHEST_RAFT = new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID, "chest_raft"), "main");

    public static void registerModels() {
        EntityModelLayerRegistry.registerModelLayer(WITHER_ZOMBIE_MODEL, WitherZombieModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(RAFT, RaftEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(CHEST_RAFT, RaftChestEntityModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(
                HangingSignModelLayers.HANGING_SIGN_CEILING,
                () -> HangingSignModels.getTexturedModelData(HangingSignModels.AttachmentType.CEILING)
        );

        EntityModelLayerRegistry.registerModelLayer(
                HangingSignModelLayers.HANGING_SIGN_CEILING_MIDDLE,
                () -> HangingSignModels.getTexturedModelData(HangingSignModels.AttachmentType.CEILING_MIDDLE)
        );

        EntityModelLayerRegistry.registerModelLayer(
                HangingSignModelLayers.HANGING_SIGN_WALL,
                () -> HangingSignModels.getTexturedModelData(HangingSignModels.AttachmentType.WALL)
        );
    }
}
