package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.config.Configs;
import net.ryan.beyond_the_block.utils.Naming.NameEngine;
import net.ryan.beyond_the_block.utils.Naming.NameableMob;
import net.ryan.beyond_the_block.utils.Naming.TitleResolvers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin {

    @Inject(method = "setOwner", at = @At("TAIL"))
    private void beyondTheBlock$onTamed(PlayerEntity owner, CallbackInfo ci) {
        TameableEntity entity = (TameableEntity)(Object)this;

        if (entity.getWorld().isClient()) return;


        if (!Configs.client().visuals.names.nameTamed) return;
        if (entity.hasCustomName()) return;
        if (!(entity instanceof NameableMob nameable)) return;

        // Ask the resolver what this entity *should* be called
        TitleResolvers.Title title = TitleResolvers.resolve(entity);
        if (title == null) return;

        NameEngine.assignName(
                entity,
                nameable,
                title.title(),
                title.colour(),
                Configs.client(),
                Configs.client().visuals.names.alliteration
        );
    }

}
