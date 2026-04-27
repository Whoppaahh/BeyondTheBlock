package net.ryan.beyond_the_block.mixin.feature.pets;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class PetEnchantPickupMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void btb$linkedInventoryPickup(CallbackInfo ci) {
        if (!((Object) this instanceof TameableEntity pet)) return;
        if (!pet.isTamed()) return;
        if (pet.getWorld().isClient) return;
        if (!(pet.getOwner() instanceof PlayerEntity owner)) return;

        if (pet.age % 5 != 0) return;

        List<ItemEntity> items = pet.getWorld().getEntitiesByClass(
                ItemEntity.class,
                pet.getBoundingBox().expand(1.5D),
                item -> item.isAlive()
                        && !item.getStack().isEmpty()
                        && !item.cannotPickup()
        );

        for (ItemEntity item : items) {
            if (PetEnchantDispatcher.itemPickup(pet, owner, item.getStack())) {
                if (item.getStack().isEmpty()) {
                    item.discard();
                }
            }
        }
    }
}