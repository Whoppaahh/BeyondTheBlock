package net.ryan.beyond_the_block.content.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import net.ryan.beyond_the_block.content.item.ModItems;

public class CupidArrowEntity extends PersistentProjectileEntity{

    private boolean cupidHit = false;

    public CupidArrowEntity(EntityType<? extends CupidArrowEntity> type, World world) {
        super(type, world);
    }

    public CupidArrowEntity(World world, LivingEntity owner) {
        super(ModEntities.CUPID_ARROW, owner, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {

        if (!this.world.isClient) {
            Entity target = hitResult.getEntity();
            LivingEntity shooter = (LivingEntity) this.getOwner();
            cupidHit = true;
            // Animals first
            if (target instanceof AnimalEntity animal && shooter instanceof PlayerEntity player) {
                if(animal.getBreedingAge() == 0 && !animal.isInLove()) {
                    animal.lovePlayer(player); // triggers hearts and love mode
                }
                return; // skip calling super.onEntityHit -> prevents damage
            }

            // Hostile mobs
            if (target instanceof MobEntity mob) {
                mob.addStatusEffect(new StatusEffectInstance(ModEffects.CUPID, 20*60, 0));
            }
            super.onEntityHit(hitResult);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (world.isClient && !this.inGround && this.getOwner() != null && !cupidHit) {
            world.addParticle(ParticleTypes.HEART, getX(), getY() + 0.2, getZ(), 0, 0, 0);
        }
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.CUPID_ARROW_ITEM);
    }

    public static boolean onDamage(LivingEntity target, DamageSource source, float amount) {
        if (!(target instanceof PlayerEntity player)) return true; // allow by default

        // get direct attacker (maybe null for some sources)
        var attacker = source.getAttacker();
        if (!(attacker instanceof MobEntity mob)) return true;

        // YOUR CHECK: is this mob "cupid-marked"? replace with your marker check
        if (!mob.hasStatusEffect(ModEffects.CUPID)) return true;

        // Cancel the damage and heal the player instead.
        // amount is the incoming amount before armour/etc; choose whether you want that or something else.
        player.heal(amount);
        mob.heal(amount);

        // optional: spawn happy particles so it feels like "healing / charm"
//        if (player.world instanceof ServerWorld serverWorld) {
//            serverWorld.spawnParticles(ParticleTypes.HEART,
//                    player.getX(), player.getY() + 1.0, player.getZ(),
//                    6, 0.5, 0.6, 0.5, 0.02);
//        }

        return false; // <- returning false prevents the damage from being applied

    }
}

