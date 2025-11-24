package net.ryan.beyond_the_block.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CobwebProjectileEntity extends ThrownItemEntity {

    private final boolean isArcAttack;

    public CobwebProjectileEntity(World world, LivingEntity owner, boolean isArcAttack) {
        super(ModEntities.COBWEB_PROJECTILE, owner, world); // Use snowball as placeholder type
        this.isArcAttack = isArcAttack;
    }

    public CobwebProjectileEntity(EntityType<CobwebProjectileEntity> type, World world) {
        super(type, world);
        this.isArcAttack = false;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AIR; // No actual item drop
    }

    @Override
    public void tick() {
        super.tick();

        // Spawn web particles
        if (world.isClient) {
            int particleCount = isArcAttack ? 5 : 1;
            for (int i = 0; i < particleCount; i++) {
                world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.COBWEB.getDefaultState()),
                        getX() + (random.nextDouble() - 0.5) * 0.3,
                        getY() + (random.nextDouble() - 0.5) * 0.3,
                        getZ() + (random.nextDouble() - 0.5) * 0.3,
                        0, 0, 0);
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockPos pos = blockHitResult.getBlockPos().up();
        if (world.isAir(pos)) {
            world.setBlockState(pos, Blocks.COBWEB.getDefaultState());
            synchronized (SpiderCobwebTrailGoal.spiderPlacedCobwebs) {
                SpiderCobwebTrailGoal.spiderPlacedCobwebs.add(pos);
            }
        }
        this.discard();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if(entityHitResult.getEntity() instanceof LivingEntity le) {
            le.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1));
        }
        this.discard();
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}

