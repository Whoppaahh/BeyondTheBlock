package net.ryan.beyond_the_block.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.utils.Helpers.ServerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true)
    private void beyond$insertRespectingNetherRules(
            ItemStack stack, int startIndex, int endIndex, boolean fromLast,
            CallbackInfoReturnable<Boolean> cir) {

        MinecraftServer server = ServerContext.getServer();
        if (server == null) return;

        ScreenHandler handler = (ScreenHandler)(Object)this;

        // Determine the world (usually from first player inventory slot)
        World world = null;
        for (Slot slot : handler.slots) {
            Inventory inv = slot.inventory;
            if (inv instanceof PlayerInventory playerInv) {
                world = playerInv.player.getWorld();
                break;
            }
        }

        if (!(world instanceof ServerWorld serverWorld)) return;
        boolean isInNether = serverWorld.getRegistryKey() == World.NETHER;

        if (!isInNether) return; // let vanilla logic run

        boolean changed = false;
        int direction = fromLast ? -1 : 1;
        int i = fromLast ? endIndex - 1 : startIndex;

        // Merge pass
        while (stack.getCount() > 0 && i >= startIndex && i < endIndex) {
            Slot slot = handler.slots.get(i);
            ItemStack existing = slot.getStack();
            Inventory targetInv = slot.inventory;

            // Determine max per destination
            int baseMax = stack.getItem().getMaxCount();
            int targetMax = (targetInv instanceof PlayerInventory)
                    ? baseMax // Vanilla limit in player inventory
                    : baseMax * 8; // Expanded in Nether containers

            if (!existing.isEmpty() && ItemStack.canCombine(stack, existing)) {
                int combined = existing.getCount() + stack.getCount();

                if (combined <= targetMax) {
                    existing.setCount(combined);
                    stack.setCount(0);
                    slot.markDirty();
                    changed = true;
                    break;
                } else if (existing.getCount() < targetMax) {
                    int toAdd = targetMax - existing.getCount();
                    existing.increment(toAdd);
                    stack.decrement(toAdd);
                    slot.markDirty();
                    changed = true;
                }
            }

            i += direction;
        }

        // Fill empty slots pass
        i = fromLast ? endIndex - 1 : startIndex;
        while (stack.getCount() > 0 && i >= startIndex && i < endIndex) {
            Slot slot = handler.slots.get(i);
            if (!slot.hasStack()) {
                Inventory targetInv = slot.inventory;
                int baseMax = stack.getItem().getMaxCount();
                int targetMax = (targetInv instanceof PlayerInventory)
                        ? baseMax
                        : baseMax * 8;

                int moveAmount = Math.min(stack.getCount(), targetMax);
                ItemStack newStack = stack.copy();
                newStack.setCount(moveAmount);
                slot.setStack(newStack);
                slot.markDirty();
                stack.decrement(moveAmount);
                changed = true;

                if (stack.isEmpty()) break;
            }
            i += direction;
        }

        if (changed) {
            cir.setReturnValue(true);
        }
    }



    @Inject(method = "onSlotClick", at = @At("TAIL"))
    private void beyond$handleNetherStackRules(
            int slotId, int button, SlotActionType actionType,
            PlayerEntity player, CallbackInfo ci) {

        MinecraftServer server = ServerContext.getServer();
        if (server == null) return;

        ScreenHandler handler = player.currentScreenHandler;

        // Ensure slot index is valid
        Slot slot = (slotId >= 0 && slotId < handler.slots.size()) ? handler.getSlot(slotId) : null;
        ItemStack cursor = handler.getCursorStack();

        boolean isInNether = player.getWorld().getRegistryKey() == World.NETHER;

        // --- 1️⃣ Handle middle-click cloning in Nether ---
        if (isInNether && actionType == SlotActionType.CLONE && slot != null && slot.hasStack()) {
            ItemStack cloned = slot.getStack().copy();

            int base = cloned.getItem().getMaxCount();
            int netherMax = base > 1 ? base * 8 : base;

            cloned.setCount(netherMax);
            handler.setCursorStack(cloned);
            return;
        }

        // --- 2️⃣ Clamp player inventory stack sizes to vanilla limits ---
        if (slot != null && slot.inventory instanceof PlayerInventory) {
            ItemStack stack = slot.getStack();
            if (stack.isEmpty()) return;

            int vanillaMax = stack.getItem().getMaxCount();
            int currentCount = stack.getCount();

            if (currentCount > vanillaMax) {
                int overflow = currentCount - vanillaMax;
                stack.setCount(vanillaMax);

                ItemStack overflowStack = stack.copy();
                overflowStack.setCount(overflow);

                // Merge into cursor or drop
                if (cursor.isEmpty()) {
                    handler.setCursorStack(overflowStack);
                } else if (ItemStack.areItemsEqual(cursor, overflowStack)
                        && cursor.getCount() < cursor.getMaxCount()) {
                    int mergeAmount = Math.min(cursor.getMaxCount() - cursor.getCount(), overflow);
                    cursor.increment(mergeAmount);
                    overflowStack.decrement(mergeAmount);

                    if (!overflowStack.isEmpty()) {
                        player.dropItem(overflowStack, false);
                    }
                } else {
                    player.dropItem(overflowStack, false);
                }
            }
        }
    }
}
