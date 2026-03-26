package net.ryan.beyond_the_block.mixin.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.feature.xp_orbs.XPUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperienceBottleItem.class)
public abstract class ExperienceBottleMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void storeOrReleaseXp(World world, PlayerEntity player, Hand hand,
                                  CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {

        ItemStack stack = player.getStackInHand(hand);
        NbtCompound nbt = stack.getOrCreateNbt();

        // Sneak = store ALL XP
        if (player.isSneaking()) {
            int xp = XPUtils.getTotalXp(player);
            if (xp <= 0) {
                cir.setReturnValue(TypedActionResult.pass(stack));
                return;
            }

            nbt.putInt("StoredXP", nbt.getInt("StoredXP") + xp);

            if (!world.isClient) {
                player.addExperience(-xp);
            }

            cir.setReturnValue(TypedActionResult.success(stack, world.isClient));
            return;
        }

        // Normal use = release XP
        if (nbt.contains("StoredXP")) {
            int xp = nbt.getInt("StoredXP");

            if (!world.isClient) {
                player.addExperience(xp);
            }

            stack.decrement(1);
            cir.setReturnValue(TypedActionResult.success(stack, world.isClient));
        }
    }
}
