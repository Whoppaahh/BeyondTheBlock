package net.ryan.beyond_the_block.mixin.Items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.utils.Helpers.XPUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GlassBottleItem.class)
public abstract class GlassBottleItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void convertBottle(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!player.isSneaking()) return;

        int xp = XPUtils.getTotalXp(player);
        if (xp <= 0) return;

        ItemStack bottle = new ItemStack(Items.EXPERIENCE_BOTTLE);
        bottle.getOrCreateNbt().putInt("StoredXP", xp);

        if (!world.isClient) {
            player.addExperience(-xp);
            player.setStackInHand(hand, bottle.copy());
        }

        cir.setReturnValue(TypedActionResult.success(bottle, world.isClient));
    }
}

