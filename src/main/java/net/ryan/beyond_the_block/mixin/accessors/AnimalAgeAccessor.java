package net.ryan.beyond_the_block.mixin.accessors;

import net.minecraft.entity.passive.PassiveEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PassiveEntity.class)
public interface AnimalAgeAccessor {
    // This exposes the real 'age' field on the server
    @Accessor("breedingAge")
    int btb$getAge();

}
