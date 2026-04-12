package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.client.render.trim.ArmourTrimRenderer;
import net.ryan.beyond_the_block.client.render.trim.ArmourTrimTextureResolver;
import net.ryan.beyond_the_block.content.registry.ModTrimRegistry;
import net.ryan.beyond_the_block.content.registry.family.ModArmourTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmourFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {

    @Unique
    private static final Set<Identifier> beyond_the_block$verifiedTextures = ConcurrentHashMap.newKeySet();

    @Unique
    private static final Set<Identifier> beyond_the_block$missingTextures = ConcurrentHashMap.newKeySet();

    @Inject(method = "renderArmor", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;setAttributes(Lnet/minecraft/client/render/entity/model/BipedEntityModel;)V",
            shift = At.Shift.AFTER
    ))
    private void afterSetAttributes(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            T entity,
            EquipmentSlot slot,
            int light,
            A model,
            CallbackInfo ci
    ) {
        switch (slot) {
            case LEGS, FEET -> {
                model.rightLeg.pivotX = -2.5F;
                model.leftLeg.pivotX = 2.5F;
            }
            case CHEST -> {
                model.rightArm.pivotX = -7F;
                model.leftArm.pivotX = 7F;
            }
        }
    }

    @Inject(method = "renderArmor", at = @At("TAIL"))
    private void beyond_the_block$renderTrim(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            T entity,
            EquipmentSlot slot,
            int light,
            A model,
            CallbackInfo ci
    ) {
        ItemStack stack = entity.getEquippedStack(slot);
        if (stack.isEmpty()) {
            return;
        }

        if (!(stack.getItem() instanceof ArmorItem armorItem)) {
            return;
        }

        if (armorItem.getSlotType() != slot) {
            return;
        }

        Optional<ModArmourTrim.Data> optionalTrim = ModArmourTrim.getTrim(stack);
        if (optionalTrim.isEmpty()) {
            return;
        }

        ModArmourTrim.Data trim = optionalTrim.get();

        if (ModTrimRegistry.getPattern(trim.patternId()) == null || ModTrimRegistry.getMaterial(trim.materialId()) == null) {
            return;
        }

        boolean leggings = slot == EquipmentSlot.LEGS;
        Identifier texture = ArmourTrimTextureResolver.resolve(trim, leggings);

        if (!beyond_the_block$textureExists(texture)) {
            return;
        }

        ArmourTrimRenderer.renderTrim(
                matrices,
                vertexConsumers,
                light,
                model,
                texture,
                stack.hasGlint()
        );
    }

    @Inject(method = "render*", at = @At("HEAD"), cancellable = true)
    private void cancelHeldItemRender(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            LivingEntity entity,
            float limbAngle,
            float limbDistance,
            float tickDelta,
            float customAngle,
            float headYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        if (entity.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            ci.cancel();
        }
    }

    @Unique
    private static boolean beyond_the_block$textureExists(Identifier texture) {
        if (beyond_the_block$verifiedTextures.contains(texture)) {
            return true;
        }

        if (beyond_the_block$missingTextures.contains(texture)) {
            return false;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return false;
        }

        Optional<Resource> resource = client.getResourceManager().getResource(texture);
        if (resource.isPresent()) {
            beyond_the_block$verifiedTextures.add(texture);
            return true;
        }

        beyond_the_block$missingTextures.add(texture);
        return false;
    }
}