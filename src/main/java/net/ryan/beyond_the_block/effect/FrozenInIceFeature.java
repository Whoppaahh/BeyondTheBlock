package net.ryan.beyond_the_block.effect;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class FrozenInIceFeature<T extends LivingEntity, M extends EntityModel<T>>
        extends FeatureRenderer<T, M> {

    public FrozenInIceFeature(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       T entity, float limbAngle, float limbDistance, float tickDelta,
                       float animationProgress, float headYaw, float headPitch) {

        if (!entity.hasStatusEffect(ModEffects.FREEZE)) return;

        Box box = entity.getBoundingBox();
        float width = (float) box.getXLength();
        float height = (float) box.getYLength();
        float depth = (float) box.getZLength();

        // Default padding
        float padX = 0.8f;
        float padY = 0.6f;
        float padZ = 0.8f;
        float yOffset = 0.5f;

        if (height > 2.5f) { // e.g. Enderman, Ravager
            padY = 1.5f;
            yOffset = padY;
            if (width > 1.0f) {
                padX = 1.5f;
                padZ = 1.5f;
            }
        } else if (height > 1.5f) { // e.g. Zombies, Skeletons, Slimes
            padY = 1.7f;
            yOffset = 0.8f;
             if(height == 1.6f && width > 1.2f){ //Horse
                padZ = 1.5f;
                yOffset = 1.0f;
            }else if (height == 1.87f && width == 0.9f){ //Llama
                 padY = 1.0f;
                 padZ = 1.5f;
                 yOffset = padY;
             }
        } if(height == 1.3f || height == 1.4f && width == 0.9f){ //Cows and sheep
            padZ = 1.2f;
            yOffset = 0.1f;
        }
        else if (height < 1.0f) { // Spiders, pigs, baby zombies, bats
            padY = 0.8f;
            yOffset = -0.3f;
            if(width == 1.4f){
                padX = 1.0f;
            }
            else if (width == 0.3f) {
                // Baby zombie
                padX = 0.5f;
                padZ = 0.5f;
            }else if (width == 0.5f){
                // Bat
                padZ = 2.2f;
                padX = 2.0f;
                padY = 1.0f;
                yOffset = 0.5f;
            }else if( width == 0.9f){ //Pigs
                padZ = 1.2f;
            }
        }

        width += padX;
        height += padY;
        depth += padZ;

        matrices.push();

        // Position so cube surrounds entity
        matrices.translate(-width / 2.0f, -yOffset, -depth / 2.0f); // -0.1f keeps feet encased
        matrices.scale(width, height, depth);

        // Determine ice cracking level based on health ratio
        float healthRatio = entity.getHealth() / entity.getMaxHealth();
        int crackLevel = (healthRatio < 0.25f) ? 3 :
                (healthRatio < 0.50f) ? 2 :
                        (healthRatio < 0.75f) ? 1 : 0;

        BlockState state = (crackLevel > 0)
                ? Blocks.FROSTED_ICE.getDefaultState().with(FrostedIceBlock.AGE, crackLevel)
                : Blocks.ICE.getDefaultState();

        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                state,
                matrices,
                vertexConsumers,
                light,
                OverlayTexture.DEFAULT_UV
        );

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
