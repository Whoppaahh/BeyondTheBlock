package net.ryan.beyond_the_block.mixin.Entities;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.ryan.beyond_the_block.config.ModConfig;
import net.ryan.beyond_the_block.utils.Naming.NameEngine;
import net.ryan.beyond_the_block.utils.Naming.NameableMob;
import net.ryan.beyond_the_block.utils.Naming.TitleResolvers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PassiveEntity.class)
public abstract class PassiveEntityMixin {

    @Inject(method = "onGrowUp", at = @At("TAIL"))
    private void beyondTheBlock$onGrowUp(CallbackInfo ci) {
        PassiveEntity entity = (PassiveEntity)(Object)this;

        if (entity.getWorld().isClient()) return;
        if (!(entity instanceof TameableEntity tameable)) return;
        if (!tameable.isTamed()) return;
        if (entity.hasCustomName()) return;
        if (!(entity instanceof NameableMob nameable)) return;

        ModConfig.NamesConfig cfg =
                AutoConfig.getConfigHolder(ModConfig.class).getConfig().mobNames;

        if (!cfg.nameTamedMobs) return;

        TitleResolvers.Title title = TitleResolvers.resolve(entity);
        if (title == null) return;

        NameEngine.assignName(
                entity,
                nameable,
                title.title(),
                title.colour(),
                cfg,
                true
        );
    }
}

