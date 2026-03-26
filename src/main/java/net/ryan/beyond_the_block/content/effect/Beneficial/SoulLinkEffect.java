package net.ryan.beyond_the_block.content.effect.Beneficial;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoulLinkEffect extends StatusEffect {
    private static final Map<UUID, UUID> linkMap = new HashMap<>();

    public SoulLinkEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public static void linkEntities(LivingEntity a, LivingEntity b) {
        linkMap.put(a.getUuid(), b.getUuid());
        linkMap.put(b.getUuid(), a.getUuid());
    }

    @Nullable
    public static LivingEntity getLinkedEntityFor(LivingEntity self) {
        UUID linkedId = linkMap.get(self.getUuid());
        if (linkedId == null) return null;

        return ((ServerWorld) self.getWorld()).getEntity(linkedId) instanceof LivingEntity linked
                ? linked : null;
    }

    // Link two entities and apply the effect to both
    public static void applyLink(LivingEntity entityA, LivingEntity entityB, int duration, int amplifier) {
        if (!entityA.isAlive() || !entityB.isAlive()) return;
        if (entityA.getWorld().isClient() || entityB.getWorld().isClient()) return;

        linkMap.put(entityA.getUuid(), entityB.getUuid());
        linkMap.put(entityB.getUuid(), entityA.getUuid());

        StatusEffectInstance effect = new StatusEffectInstance(
                ModEffects.SOUL_LINK, duration, amplifier, false, true, true
        );

        entityA.addStatusEffect(effect);
        entityB.addStatusEffect(effect);
    }

    // Optional: clear link (e.g. on death or effect expire)
    public static void clearLink(LivingEntity entity) {
        UUID selfId = entity.getUuid();
        UUID otherId = linkMap.remove(selfId);
        if (otherId != null) {
            linkMap.remove(otherId);
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        SoulLinkEffect.clearLink(entity);
        super.onRemoved(entity, attributes, amplifier);
    }


    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}
