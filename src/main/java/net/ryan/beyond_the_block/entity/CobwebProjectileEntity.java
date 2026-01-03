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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.utils.Helpers.CobwebDecayScheduler;

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
    protected void onBlockHit(BlockHitResult hit) {
        if (!(world instanceof ServerWorld sw)) return;

        BlockPos pos = hit.getBlockPos().up();
        if (sw.isAir(pos)) {
            sw.setBlockState(pos, Blocks.COBWEB.getDefaultState());
            CobwebDecayScheduler.schedule(sw, pos);
        }
        discard();
    }


    @Override
    protected void onEntityHit(EntityHitResult hit) {
        if (!(world instanceof ServerWorld)) {
            discard();
            return;
        }
        if (hit.getEntity() instanceof LivingEntity le) {
            le.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1));
        }
        discard();
    }


    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}

