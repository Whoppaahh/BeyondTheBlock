package net.ryan.beyond_the_block.event.entity;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.ryan.beyond_the_block.content.effect.FreezeEffectEvents;
import net.ryan.beyond_the_block.content.entity.CupidArrowEntity;
import net.ryan.beyond_the_block.feature.projectile.ArrowRecoveryHandler;

public class EntityEventRegistrar {

    public static void register(){
        registerEntityEvents();
    }
    private static void registerEntityEvents() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(ArrowRecoveryHandler::dropArrows);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(CupidArrowEntity::onDamage);
        ServerLivingEntityEvents.AFTER_DEATH.register(FreezeEffectEvents::onDeath);
    }


}
