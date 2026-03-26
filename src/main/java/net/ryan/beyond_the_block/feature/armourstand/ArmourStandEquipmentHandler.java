package net.ryan.beyond_the_block.feature.armourstand;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ArmourStandEquipmentHandler {
    public static ActionResult onEntityUsed(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        if (world.isClient) return ActionResult.PASS;

        if (!(entity instanceof ArmorStandEntity armorStand)) return ActionResult.PASS;

        if (player.isSneaking()) {
            transferArmourPartial(player, armorStand, world);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
    private static void transferArmourPartial(PlayerEntity player, ArmorStandEntity armourStand, World world) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;

            ItemStack standItem = armourStand.getEquippedStack(slot);
            ItemStack playerItem = player.getEquippedStack(slot);

            // Armor stand → player (if player slot empty)
            if (!standItem.isEmpty() && playerItem.isEmpty()) {
                player.equipStack(slot, standItem.copy());
                armourStand.equipStack(slot, ItemStack.EMPTY);
            }
            // Player → armor stand (if armor stand slot empty)
            else if (standItem.isEmpty() && !playerItem.isEmpty()) {
                armourStand.equipStack(slot, playerItem.copy());
                player.equipStack(slot, ItemStack.EMPTY);
            }
            // Swap
            else if (!standItem.isEmpty() && !playerItem.isEmpty()) {
                ItemStack temp = playerItem.copy();
                player.equipStack(slot, standItem.copy());
                armourStand.equipStack(slot, temp);
            }
        }

        // Optional feedback sound
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1f, 1f);
    }
}
