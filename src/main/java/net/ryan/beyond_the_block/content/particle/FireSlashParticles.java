package net.ryan.beyond_the_block.content.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class FireSlashParticles extends SpriteBillboardParticle {

    protected FireSlashParticles(ClientWorld world, double x, double y, double z,
                                 double velocityX, double velocityY, double velocityZ,
                                 SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale *= 0.5F + world.random.nextFloat() * 0.5F;
        this.maxAge = 10 + world.random.nextInt(5);
        this.setSprite(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 1.0F - ((float) age / maxAge); // fade out
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType type, ClientWorld world,
                                       double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            return new FireSlashParticles(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);
        }
    }
}

