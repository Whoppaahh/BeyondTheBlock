package net.ryan.beyond_the_block.content.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.ryan.beyond_the_block.feature.horses.HorseEquipmentAccessor;

public class HorseshoesItem extends Item {

    public HorseshoesItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (!(entity instanceof AbstractHorseEntity horse)) {
            return ActionResult.PASS;
        }

        if (!horse.isTame() || !horse.getOwnerUuid().equals(player.getUuid())) {
            return ActionResult.FAIL;
        }

        if (!(horse instanceof HorseEquipmentAccessor accessor)) {
            return ActionResult.PASS;
        }

        if (player.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }

        ItemStack oldShoes = accessor.btb$getHorseshoes();

        ItemStack newShoes = stack.copy();
        newShoes.setCount(1);
        accessor.btb$setHorseshoes(newShoes);

        stack.decrement(1);

        if (!oldShoes.isEmpty()) {
            if (!player.getInventory().insertStack(oldShoes)) {
                player.dropItem(oldShoes, false);
            }
        }

        player.sendMessage(Text.literal("Horseshoes equipped."), true);
        return ActionResult.CONSUME;
    }
}