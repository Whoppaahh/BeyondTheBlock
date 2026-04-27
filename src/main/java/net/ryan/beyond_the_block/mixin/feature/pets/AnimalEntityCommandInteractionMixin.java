package net.ryan.beyond_the_block.mixin.feature.pets;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.ryan.beyond_the_block.content.item.DeedOfOwnershipItem;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.feature.pets.PetCollarAccessor;
import net.ryan.beyond_the_block.feature.pets.PetCommandAccessor;
import net.ryan.beyond_the_block.feature.pets.PetCommandState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityCommandInteractionMixin {

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void btb$cycleCommandState(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        AnimalEntity self = (AnimalEntity) (Object) this;

        if (!(self instanceof TameableEntity pet)) return;
        if (!(pet instanceof PetCommandAccessor commandAccessor)) return;
        if (!(pet instanceof PetCollarAccessor collarAccessor)) return;
        if (!pet.isTamed() || !pet.isOwner(player)) return;

        ItemStack heldStack = player.getStackInHand(hand);

        if (heldStack.getItem() instanceof DeedOfOwnershipItem deedItem) {
            ActionResult result = deedItem.useOnEntity(heldStack, player, pet, hand);
            cir.setReturnValue(result);
            return;
        }

        // Equip/swap collar
        if (heldStack.isOf(ModItems.COLLAR_TAG)) {
            if (pet.getWorld().isClient) {
                cir.setReturnValue(ActionResult.SUCCESS);
                return;
            }

            ItemStack oldCollar = collarAccessor.btb$getCollar();
            ItemStack newCollar = heldStack.copy();
            newCollar.setCount(1);

            collarAccessor.btb$setCollar(newCollar);

            if (newCollar.hasCustomName()) {
                pet.setCustomName(newCollar.getName());
                pet.setCustomNameVisible(true);
            }

            heldStack.decrement(1);

            if (!oldCollar.isEmpty()) {
                if (!player.getInventory().insertStack(oldCollar)) {
                    player.dropItem(oldCollar, false);
                }
            }

            BeyondTheBlock.LOGGER.info("Collar equipped.");
            player.sendMessage(Text.literal("Collar equipped."), true);
            cir.setReturnValue(ActionResult.CONSUME);
            return;
        }

        // Sneak + empty hand = inspect collar
        if (player.isSneaking() && heldStack.isEmpty()) {
            if (pet.getWorld().isClient) {
                cir.setReturnValue(ActionResult.SUCCESS);
                return;
            }

            ItemStack collar = collarAccessor.btb$getCollar();

            if (collar.isEmpty()) {
                BeyondTheBlock.LOGGER.info("This pet has no collar.");
                player.sendMessage(Text.literal("This pet has no collar."), true);
            } else {
                BeyondTheBlock.LOGGER.info("Collar: {}", collar.getName());
                player.sendMessage(Text.literal("Collar: ").append(collar.getName()), true);

                if (collar.hasEnchantments()) {
                    EnchantmentHelper.get(collar).forEach((enchantment, level) -> {
                        player.sendMessage(
                                Text.literal("- ")
                                        .append(Text.translatable(enchantment.getTranslationKey()))
                                        .append(Text.literal(" " + level)),
                                false
                        );
                    });
                } else {
                    BeyondTheBlock.LOGGER.info("No enchantments.");
                    player.sendMessage(Text.literal("No enchantments."), false);
                }
            }

            cir.setReturnValue(ActionResult.CONSUME);
            return;
        }

        // Sneak + shears = remove collar
        if (player.isSneaking() && heldStack.isOf(Items.SHEARS)) {
            if (pet.getWorld().isClient) {
                cir.setReturnValue(ActionResult.SUCCESS);
                return;
            }

            ItemStack collar = collarAccessor.btb$getCollar();

            if (collar.isEmpty()) {
                BeyondTheBlock.LOGGER.info("This pet has no collar.");
                player.sendMessage(Text.literal("This pet has no collar."), true);
                cir.setReturnValue(ActionResult.CONSUME);
                return;
            }

            if (!player.getInventory().insertStack(collar.copy())) {
                player.dropItem(collar.copy(), false);
            }

            collarAccessor.btb$setCollar(ItemStack.EMPTY);
            BeyondTheBlock.LOGGER.info("Collar removed.");
            player.sendMessage(Text.literal("Collar removed."), true);

            cir.setReturnValue(ActionResult.CONSUME);
        }
    }
}