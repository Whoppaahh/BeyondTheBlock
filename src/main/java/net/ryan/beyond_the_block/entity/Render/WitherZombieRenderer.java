package net.ryan.beyond_the_block.entity.Render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.entity.ModEntities;
import net.ryan.beyond_the_block.entity.Model.WitherZombieModel;
import net.ryan.beyond_the_block.entity.WitherZombie;

public class WitherZombieRenderer extends MobEntityRenderer<WitherZombie, WitherZombieModel<WitherZombie>> {

    private static final Identifier WITHER_ZOMBIE_TEXTURE = new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/zombie/wither_zombie.png");

    public WitherZombieRenderer(EntityRendererFactory.Context context) {
        super(context, new WitherZombieModel<>(context.getPart(ModEntities.WITHER_ZOMBIE_MODEL)), 0.5f);
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));

    }
    @Override
    public Identifier getTexture(WitherZombie entity) {
        return WITHER_ZOMBIE_TEXTURE;
    }
    @Override
    public void render(WitherZombie entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Render the default villager with custom texture
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

    }
}
