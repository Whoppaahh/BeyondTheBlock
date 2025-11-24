package net.ryan.beyond_the_block.block.Entity;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.screen.Handlers.DecrafterScreenHandler;
import org.jetbrains.annotations.Nullable;

public class DecrafterBlockEntity extends BlockEntity implements Inventory, ExtendedScreenHandlerFactory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);


    public DecrafterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DECRAFTER_BLOCK_ENTITY, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Decrafter Table");
    }


    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeBlockPos(this.pos);
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeBlockPos(this.pos);
        return new DecrafterScreenHandler(syncId, inv, this);
    }

    @Override
    public int size() {
        return 10;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stackInSlot = inventory.get(slot);
        if (stackInSlot.isEmpty()) {
            return ItemStack.EMPTY; // Never return null
        }

        ItemStack result;
        if (stackInSlot.getCount() <= amount) {
            // Remove the entire stack
            result = stackInSlot;
            inventory.set(slot, ItemStack.EMPTY);
        } else {
            // Split the stack, removing only 'amount' items
            result = stackInSlot.split(amount);
            if (stackInSlot.getCount() == 0) {
                inventory.set(slot, ItemStack.EMPTY);
            }
        }
        markDirty();
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = inventory.get(slot);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        inventory.set(slot, ItemStack.EMPTY);
        markDirty();
        return stack;
    }


    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (world == null) return false;
        return player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }

    @Override
    public void clear() {
        inventory.clear();
        markDirty();
    }
}
