package net.ryan.beyond_the_block.effect.Harmful;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.util.UUID;

public class TimeDilationEffect extends StatusEffect {

    private static final UUID MOVE_SPEED_UUID = UUID.fromString("ba8b31b3-5ecb-45c9-9e45-38f14b3889d1");
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("83bb9f8c-684b-4a26-b9b1-c727f4a84577");


    public TimeDilationEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
        addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, MOVE_SPEED_UUID.toString(), -0.2D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, ATTACK_SPEED_UUID.toString(), -0.3D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {

        //Effect: Slows down movement, mining, and attack speed. Could distort screen (optional client-side)
        //Type: Harmful
        //Twist: While active, the player may see enemies "move faster".

        if (!pLivingEntity.world.isClient()) {
            // Apply a slow effect to the entity
            pLivingEntity.setVelocity(pLivingEntity.getVelocity().multiply(0.5));
            if (!pLivingEntity.hasStatusEffect(StatusEffects.SLOWNESS)) {
                pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, pAmplifier + 1, false, false, false));
            }
        }
        // On the client side, you can apply effects like slowing down the world
        if (pLivingEntity.world.isClient()) {
            MinecraftClient client = MinecraftClient.getInstance();
            // Do client-side specific effects here, such as particle effects or altering time perception
            // Example: Apply a screen effect to simulate the world moving faster.
        }
        super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}
