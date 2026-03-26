package net.ryan.beyond_the_block.event.player;

import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.client.visual.Glowable;
import net.ryan.beyond_the_block.content.block.PlayerVaultBlock;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.event.ModEvents;
import net.ryan.beyond_the_block.feature.armourstand.ArmourStandEquipmentHandler;
import net.ryan.beyond_the_block.feature.combat.FlameSweepHandler;
import net.ryan.beyond_the_block.feature.restore.RestoreProtectionHandler;
import net.ryan.beyond_the_block.utils.Helpers.BetterLadderPlacement;
import net.ryan.beyond_the_block.utils.Helpers.DoubleOpenablesHandler;

public class PlayerEventRegistrar {
    private static void registerPlayerEvents() {
        AttackBlockCallback.EVENT.register(PlayerEventRegistrar::onBlockMined);
        AttackEntityCallback.EVENT.register(FlameSweepHandler::onEntityAttacked);
        UseBlockCallback.EVENT.register(PlayerEventRegistrar::onBlockUsed);
        UseItemCallback.EVENT.register(RestoreProtectionHandler::onItemUsed);
        UseEntityCallback.EVENT.register(ArmourStandEquipmentHandler::onEntityUsed);


        PlayerBlockBreakEvents.BEFORE.register(ModEvents::onBlockBreak);
        PlayerBlockBreakEvents.BEFORE.register(PlayerVaultBlock::handleBreak);
        UseBlockCallback.EVENT.register(DoubleOpenablesHandler::onUse);

        UseBlockCallback.EVENT.register(BetterLadderPlacement::onUseBlock);

        UseItemCallback.EVENT.register(BetterLadderPlacement::onUseItem);

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);

            if (!(state.getBlock() instanceof BannerBlock)) return ActionResult.PASS;

            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof Glowable glowable) {
                ItemStack stack = player.getStackInHand(hand);

                if (stack.isOf(Items.GLOW_INK_SAC)) {
                    BeyondTheBlock.LOGGER.info("Glowing");
                    glowable.bt$setGlowing(true);
                    if (!player.getAbilities().creativeMode) stack.decrement(1);
                    return ActionResult.SUCCESS;
                } else if (stack.isOf(Items.INK_SAC)) {
                    glowable.bt$setGlowing(false);
                    if (!player.getAbilities().creativeMode) stack.decrement(1);
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.PASS;
        });
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
