package net.ryan.beyond_the_block.utils.ProjectileHelpers;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ArrowEntity;

public class HomingTrackedData {
    public static final TrackedData<Boolean> HAS_HOMING =
            DataTracker.registerData(ArrowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
}

