package net.ryan.beyond_the_block.mixin.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.utils.ProjectileHelpers.HomingTrackedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @Inject(method = "onStoppedUsing", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", shift = At.Shift.AFTER))
    private void onShootHomingArrow(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        if (!(user instanceof PlayerEntity player)) return;

        boolean hasHoming = EnchantmentHelper.getLevel(ModEnchantments.HOMING, stack) > 0;

        if (hasHoming) {
            List<ArrowEntity> arrows = serverWorld.getEntitiesByClass(ArrowEntity.class,
                    player.getBoundingBox().expand(5), // Nearby entities
                    arrow -> arrow.getOwner() == player && arrow.age <= 5 // Just spawned arrows
            );


            for (ArrowEntity arrow : arrows) {
                arrow.getDataTracker().set(HomingTrackedData.HAS_HOMING, true);
            }
        }
    }

    @ModifyVariable(
            method = "onStoppedUsing",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private ItemStack preventArrowConsumption(ItemStack arrowStack, ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) return arrowStack;

        int infinityLevel = EnchantmentHelper.getLevel(Enchantments.INFINITY, stack);
        if (infinityLevel == 0 || arrowStack.isEmpty()) return arrowStack;

        // Infinity II+: normal arrows not consumed/ vanilla handles this already
        //this level simply allows you to use infinity without any arrows to begin with

        // Infinity III+: tipped/spectral arrows not consumed
        if (infinityLevel == 3 && (arrowStack.isOf(Items.TIPPED_ARROW) || arrowStack.isOf(Items.SPECTRAL_ARROW))) {
            return arrowStack.copy();
        }

        return arrowStack; // vanilla behavior otherwise
    }
}
