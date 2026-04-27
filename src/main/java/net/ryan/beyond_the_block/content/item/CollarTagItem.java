package net.ryan.beyond_the_block.content.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.ryan.beyond_the_block.feature.pets.PetCollarAccessor;

public class CollarTagItem extends Item {

    public CollarTagItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (!(entity instanceof TameableEntity pet)) {
            return ActionResult.PASS;
        }

        if (!pet.isTamed() || !pet.isOwner(player)) {
            return ActionResult.FAIL;
        }

        if (!(pet instanceof PetCollarAccessor collarAccessor)) {
            return ActionResult.PASS;
        }

        if (player.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }

        ItemStack oldCollar = collarAccessor.btb$getCollar();
        ItemStack newCollar = stack.copy();
        newCollar.setCount(1);

        collarAccessor.btb$setCollar(newCollar);

        if (newCollar.hasCustomName()) {
            pet.setCustomName(newCollar.getName());
            pet.setCustomNameVisible(true);
        }

        stack.decrement(1);

        if (!oldCollar.isEmpty()) {
            if (!player.getInventory().insertStack(oldCollar)) {
                player.dropItem(oldCollar, false);
            }
        }

        return ActionResult.CONSUME;
    }
}