package net.ryan.beyond_the_block.content.effect;

import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FreezeEffectLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {


    public FreezeEffectLayer(FeatureRendererContext<T, M> context, M baseModel) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       T entity, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress,
                       float headYaw, float headPitch) {

        // Only render if the entity has the freeze effect
        if (!entity.hasStatusEffect(ModEffects.FREEZE)) return;

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(FreezeRenderUtil.getFreezeTexture(entity)));

        // Render the copied model (ice overlay)
        this.getContextModel().animateModel(entity, limbAngle, limbDistance, tickDelta);
        this.getContextModel().setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        matrices.push();
        this.getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, FreezeRenderUtil.getFreezeAlpha());
        matrices.pop();
    }

    public static void shatterOnThaw(LivingEntity entity) {
        World world = entity.getWorld();

        // Softer glass crack
        world.playSound(null, entity.getBlockPos(),
                SoundEvents.BLOCK_GLASS_BREAK,
                SoundCategory.PLAYERS,
                0.6f, 1.2f); // higher pitch for subtle feel

        if (world instanceof ServerWorld serverWorld) {
            // Ice fragments
            for (int i = 0; i < 25; i++) {
                double dx = entity.getX() + (world.random.nextDouble() - 0.5) * entity.getWidth() * 2;
                double dy = entity.getY() + world.random.nextDouble() * entity.getHeight();
                double dz = entity.getZ() + (world.random.nextDouble() - 0.5) * entity.getWidth() * 2;

                double vx = (world.random.nextDouble() - 0.5) * 0.3;
                double vy = world.random.nextDouble() * 0.3;
                double vz = (world.random.nextDouble() - 0.5) * 0.3;

                serverWorld.spawnParticles(
                        new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.ICE.getDefaultState()),
                        dx, dy, dz, 3, vx, vy, vz, 0.1
                );
            }

            // Frosty cloud burst
            for (int i = 0; i < 15; i++) {
                double dx = entity.getX();
                double dy = entity.getY() + entity.getHeight() * 0.5;
                double dz = entity.getZ();

                double vx = (world.random.nextDouble() - 0.5) * 0.1;
                double vy = world.random.nextDouble() * 0.1;
                double vz = (world.random.nextDouble() - 0.5) * 0.1;

                serverWorld.spawnParticles(ParticleTypes.CLOUD, dx, dy, dz, 1, vx, vy, vz, 0.0);
                serverWorld.spawnParticles(ParticleTypes.POOF, dx, dy, dz, 1, vx, vy, vz, 0.0);
            }
        }
    }

    public static void onDeath(LivingEntity livingEntity, DamageSource damageSource) {
        if (livingEntity.hasStatusEffect(ModEffects.FREEZE)) {
            World world = livingEntity.getWorld();

            // Play glass break
            world.playSound(null, livingEntity.getBlockPos(),
                    SoundEvents.BLOCK_GLASS_BREAK,
                    SoundCategory.PLAYERS,
                    1.0f, 1.0f);

            // Spawn particle burst
            if (world instanceof ServerWorld serverWorld) {
                for (int i = 0; i < 40; i++) {
                    double dx = livingEntity.getX() + (world.random.nextDouble() - 0.5) * livingEntity.getWidth() * 2;
                    double dy = livingEntity.getY() + world.random.nextDouble() * livingEntity.getHeight();
                    double dz = livingEntity.getZ() + (world.random.nextDouble() - 0.5) * livingEntity.getWidth() * 2;

                    double vx = (world.random.nextDouble() - 0.5) * 0.3;
                    double vy = world.random.nextDouble() * 0.3;
                    double vz = (world.random.nextDouble() - 0.5) * 0.3;

                    serverWorld.spawnParticles(ParticleTypes.SNOWFLAKE, dx, dy, dz, 1, vx, vy, vz, 0.1);
                    serverWorld.spawnParticles(ParticleTypes.ITEM_SNOWBALL, dx, dy, dz, 1, vx, vy, vz, 0.1);
                    serverWorld.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.ICE.getDefaultState()),
                            dx, dy, dz, 1, vx, vy, vz, 0.1);

                }
            }
        }
    }
}
