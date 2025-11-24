package net.ryan.beyond_the_block.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.world.World;

public class WitherZombieHorse extends ZombieHorseEntity {
    public WitherZombieHorse(EntityType<? extends ZombieHorseEntity> entityType, World world) {
        super(entityType, world);
        this.setTame(true);
    }

    @Override
    public boolean isUndead() {
        return true;
    }

    public static DefaultAttributeContainer.Builder createWitherZombieHorseAttributes() {
        return ZombieHorseEntity.createZombieHorseAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D);
    }
}
