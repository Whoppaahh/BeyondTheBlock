package net.ryan.beyond_the_block.mixin.accessors;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ArrowEntity.class)
public interface ArrowEntityAccessor {
    @Accessor("effects")
    Set<StatusEffectInstance> getEffects();
}

