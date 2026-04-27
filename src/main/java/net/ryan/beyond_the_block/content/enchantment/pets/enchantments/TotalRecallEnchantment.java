package net.ryan.beyond_the_block.content.enchantment.pets.enchantments;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.particle.ParticleTypes;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantHooks;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantment;

public class TotalRecallEnchantment extends PetEnchantment implements PetEnchantHooks {

    public TotalRecallEnchantment() {
        super(Rarity.RARE);
    }

    @Override
    public void onOwnerTooFar(LivingEntity pet, PlayerEntity owner, int level) {
        if (pet.getWorld().isClient) return;
        if(!(pet instanceof TameableEntity tamed))return;
        if (!(pet.getWorld() instanceof ServerWorld world)) return;
        if (pet.hasVehicle() || tamed.isLeashed()) return;

        double range = 2.0D + level;

        double x = owner.getX() + (pet.getRandom().nextDouble() - 0.5D) * range;
        double y = owner.getY();
        double z = owner.getZ() + (pet.getRandom().nextDouble() - 0.5D) * range;

        world.spawnParticles(
                ParticleTypes.PORTAL,
                pet.getX(), pet.getY() + 0.5D, pet.getZ(),
                24,
                0.35D, 0.35D, 0.35D,
                0.05D
        );

        pet.refreshPositionAndAngles(x, y, z, pet.getYaw(), pet.getPitch());
        tamed.getNavigation().stop();

        world.spawnParticles(
                ParticleTypes.PORTAL,
                pet.getX(), pet.getY() + 0.5D, pet.getZ(),
                24,
                0.35D, 0.35D, 0.35D,
                0.05D
        );

        world.playSound(
                null,
                pet.getBlockPos(),
                SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                pet.getSoundCategory(),
                0.7F,
                1.4F
        );
    }
}