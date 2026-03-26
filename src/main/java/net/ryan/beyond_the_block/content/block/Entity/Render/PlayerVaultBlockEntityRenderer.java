package net.ryan.beyond_the_block.content.block.Entity.Render;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.block.Entity.PlayerVaultBlockEntity;

import java.util.Objects;

public class PlayerVaultBlockEntityRenderer implements BlockEntityRenderer<PlayerVaultBlockEntity> {

    public PlayerVaultBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}


    @Override
    public void render(PlayerVaultBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
//        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
//        ItemStack stack = entity.getDisplayItem().get(0);
//
//        matrices.push();
//        matrices.translate(0.5f, 1.35f, 0.5f);
//        matrices.scale(0.5f, 0.5f, 0.5f);
//
//        matrices.multiply(new Quaternion(Vec3f.POSITIVE_Y, entity.getRenderingRotation(), true));
//
//        BakedModel model = itemRenderer.getModel(stack, entity.getWorld(), null, 0); // Get the item model
//        itemRenderer.renderItem(stack, ModelTransformation.Mode.HEAD, false, matrices, vertexConsumers,
//                getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos()),
//                OverlayTexture.DEFAULT_UV, model);
//
//
//        matrices.pop();
        ItemStack stack = entity.getDisplayItem().get(0);
        if (stack.isEmpty()) return;
        if (!stack.isOf(Items.PLAYER_HEAD)) return;

        GameProfile profile = entity.getSkullProfile();
        if (profile == null) return;

        // Set up transformation
        matrices.push();
        matrices.translate(0.25f, 0.8f, 0.25f);
        matrices.scale(0.5f, 0.5f, 0.5f);

        float yaw = entity.getRenderingRotation();

        // Get model
        SkullBlockEntityModel model = SkullBlockEntityRenderer.getModels(MinecraftClient.getInstance().getEntityModelLoader())
                .get(SkullBlock.Type.PLAYER);

        // Get render layer
        RenderLayer layer = SkullBlockEntityRenderer.getRenderLayer(SkullBlock.Type.PLAYER, profile);

        // Finally render the skull
        SkullBlockEntityRenderer.renderSkull(
                Direction.UP,
                yaw,
                tickDelta,
                matrices,
                vertexConsumers,
                getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos()),
                model,
                layer
        );

        matrices.pop();
        }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }

}

