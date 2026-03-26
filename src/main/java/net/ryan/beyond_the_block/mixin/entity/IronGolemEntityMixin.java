package net.ryan.beyond_the_block.mixin.entity;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Formatting;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.feature.naming.NameEngine;
import net.ryan.beyond_the_block.feature.naming.NameableMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin {

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void beyondTheBlock$assignNameOnLoad(NbtCompound nbt, CallbackInfo ci) {
        IronGolemEntity golem = (IronGolemEntity)(Object)this;

        // Server only
        if (golem.getWorld().isClient()) return;


        if (!Configs.client().visuals.names.nameGolems) return;

        // Already named? Do nothing.
        if (golem.hasCustomName()) return;

        // Must support base-name storage
        if (!(golem instanceof NameableMob nameable)) return;

        NameEngine.assignName(
                golem,
                nameable,
                "the Golem",
                Formatting.GRAY,
                Configs.client(),
                Configs.client().visuals.names.alliteration
        );
    }
}
