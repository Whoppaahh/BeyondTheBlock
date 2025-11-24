package net.ryan.beyond_the_block.mixin.Items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.enchantment.ModEnchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {


    @Shadow
    @Nullable
    public abstract PlayerEntity getPlayerOwner();
    @Unique
    PlayerEntity player = this.getPlayerOwner();

    @Inject(method = "use", at = @At("HEAD"))
    private void btb$cookFish(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
    }

    @Inject(method = "pullHookedEntity", at = @At("HEAD"))
    private void btb$cookFishingLoot(Entity hookedEntity, CallbackInfo ci) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
        if (serverPlayer.getMainHandStack().isEmpty()) return;

        ItemStack rod = serverPlayer.getMainHandStack();
        int level = EnchantmentHelper.getLevel(ModEnchantments.FISHING_COOKING, rod);

        if (level <= 0) return; // no Cooking enchantment

        // When fishing loot spawns as an item entity, cook it
        if (hookedEntity instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getStack();
            ItemStack cooked = cookable(stack, level);

            if (cooked != null) {
                itemEntity.setStack(cooked);
            }
        }
    }

    @Unique
    private ItemStack cookable(ItemStack stack, int level) {
        Item item = stack.getItem();

        // Basic fish
        if (item == Items.COD) {
            return new ItemStack(Items.COOKED_COD, stack.getCount());
        }
        if (item == Items.SALMON) {
            return new ItemStack(Items.COOKED_SALMON, stack.getCount());
        }

        // OPTIONAL expansions for levels > 1:
        // Example: level 2: smelt kelp → dried kelp
        if (level >= 2 && item == Items.KELP) {
            return new ItemStack(Items.DRIED_KELP, stack.getCount());
        }

        // Example: level 3: turn pufferfish → cooked tropical fish
        if (level >= 3 && item == Items.PUFFERFISH) {
            return new ItemStack(Items.COOKED_COD, stack.getCount());
        }

        // Not cookable
        return null;
    }
}

