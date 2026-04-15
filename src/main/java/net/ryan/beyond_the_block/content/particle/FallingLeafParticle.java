package net.ryan.beyond_the_block.content.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class FallingLeafParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    protected FallingLeafParticle(ClientWorld world, double x, double y, double z,
                                  double velocityX, double velocityY, double velocityZ,
                                  SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;

        this.scale = 0.04F + this.random.nextFloat() * 0.04F;
        this.maxAge = 60 + this.random.nextInt(40);
        this.gravityStrength = 0.0008F;
        this.collidesWithWorld = false;

        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;

        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }

        this.velocityX += (this.random.nextDouble() - 0.5D) * 0.0005D;
        this.velocityZ += (this.random.nextDouble() - 0.5D) * 0.0005D;
        this.velocityY -= this.gravityStrength;

        this.move(this.velocityX, this.velocityY, this.velocityZ);

        this.velocityX *= 0.99D;
        this.velocityY *= 0.98D;
        this.velocityZ *= 0.99D;

        this.setSpriteForAge(this.spriteProvider);
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

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world,
                                       double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            return new FallingLeafParticle(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);
        }
    }
}