package net.ryan.beyond_the_block.mixin.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;
import net.ryan.beyond_the_block.feature.player.ranged.InfinityArrowRules;
import net.ryan.beyond_the_block.feature.projectile.HomingTrackedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @Inject(
            method = "shootAll",
            at = @At(value = "TAIL")
    )
    private static void beyond_the_block$markCrossbowArrowsHoming(
            World world,
            LivingEntity shooter,
            net.minecraft.util.Hand hand,
            ItemStack stack,
            float speed,
            float divergence,
            CallbackInfo ci
    ) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        if (!(shooter instanceof PlayerEntity player)) return;

        boolean hasHoming = EnchantmentHelper.getLevel(ModEnchantments.HOMING, stack) > 0;
        if (!hasHoming) return;

        List<ArrowEntity> arrows = serverWorld.getEntitiesByClass(
                ArrowEntity.class,
                player.getBoundingBox().expand(5.0),
                arrow -> arrow.getOwner() == player && arrow.age <= 5
        );

        for (ArrowEntity arrow : arrows) {
            arrow.getDataTracker().set(HomingTrackedData.HAS_HOMING, true);
        }
    }

    /**
     * Stops Infinity crossbows from consuming normal arrows,
     * but still consumes tipped/spectral arrows.
     */
    @ModifyVariable(
            method = "loadProjectile",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private static ItemStack preventConsumption(ItemStack original, LivingEntity shooter, ItemStack crossbow) {
        return InfinityArrowRules.maybeReturnProtectedCopy(crossbow, original);
    }
}


