package net.ryan.beyond_the_block.mixin.feature.horses;

import net.minecraft.entity.passive.AbstractHorseEntity;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.feature.naming.NameEngine;
import net.ryan.beyond_the_block.feature.naming.NameableMob;
import net.ryan.beyond_the_block.feature.naming.TitleResolvers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorseEntity.class)
public class HorseEntityNamingMixin implements NameableMob {
    @Inject(method = "setTame", at = @At("TAIL"))
    private void beyond$onHorseTamed(boolean tame, CallbackInfo ci) {
        if (!tame) return;

        AbstractHorseEntity horse = (AbstractHorseEntity)(Object)this;
        if (horse.getWorld().isClient()) return;
        if (horse.hasCustomName()) return;

        if (!Configs.client().visuals.names.enabled || !Configs.client().visuals.names.nameTamed) return;

        TitleResolvers.Title title = TitleResolvers.resolve(horse);
        if (title == null) return;

        NameEngine.assignName(
                horse,
                this,
                title.title(),
                title.colour(),
                Configs.client(),
                Configs.client().visuals.names.alliteration
        );
    }


    @Override
    public void beyondTheBlock$setBaseName(String name) {

    }

    @Override
    public String beyondTheBlock$getBaseName() {
        return "";
    }
}
