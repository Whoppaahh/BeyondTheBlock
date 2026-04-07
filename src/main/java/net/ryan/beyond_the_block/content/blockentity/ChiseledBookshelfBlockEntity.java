package net.ryan.beyond_the_block.content.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.ryan.beyond_the_block.content.block.ChiseledBookshelfBlock;
import net.ryan.beyond_the_block.content.registry.ModBlockEntities;

public class ChiseledBookshelfBlockEntity extends BlockEntity implements SidedInventory {
    public static final int SIZE = 6;
    private static final int[] AVAILABLE_SLOTS = new int[]{0, 1, 2, 3, 4, 5};

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
    private int lastInteractedSlot = -1;

    public ChiseledBookshelfBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHISELED_BOOKSHELF_BLOCK_ENTITY, pos, state);
    }

    public int getComparatorOutput() {
        return this.getOccupiedSlotCount();
        //return this.lastInteractedSlot >= 0 ? this.lastInteractedSlot + 1 : 0;
    }

    public int getOccupiedSlotCount(){
        int count = 0;
        for(ItemStack stack : items){
            if(!stack.isEmpty()){
                count++;
            }
        }
        return count;
    }

    public int getEnchantingPower() {
        return getOccupiedSlotCount();
    }

    public int getLastInteractedSlot() {
        return lastInteractedSlot;
    }

    public void setLastInteractedSlot(int slot) {
        this.lastInteractedSlot = slot;
        this.markDirtyAndSync();
    }

    public void markSlotInteracted(int slot) {
        this.lastInteractedSlot = slot;
        this.markDirtyAndSync();
    }

    private void markDirtyAndSync() {
        this.markDirty();
        if (this.world != null && !this.world.isClient) {
            syncBlockState();
            this.world.updateComparators(this.pos, this.getCachedState().getBlock());
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
        }
    }

    private void syncBlockState() {
        if (this.world == null || this.world.isClient) {
            return;
        }

        BlockState current = this.world.getBlockState(this.pos);
        BlockState updated = current;

        for (int i = 0; i < SIZE; i++) {
            updated = updated.with(ChiseledBookshelfBlock.getSlotProperty(i), !this.items.get(i).isEmpty());
        }

        if (updated != current) {
            this.world.setBlockState(this.pos, updated, 3);
        }
    }

    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack removed = Inventories.splitStack(this.items, slot, amount);
        if (!removed.isEmpty()) {
            this.lastInteractedSlot = slot;
            this.markDirtyAndSync();
        }
        return removed;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack removed = Inventories.removeStack(this.items, slot);
        if (!removed.isEmpty()) {
            this.lastInteractedSlot = slot;
            this.markDirtyAndSync();
        }
        return removed;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (!stack.isEmpty() && !ChiseledBookshelfBlock.isValidBook(stack)) {
            return;
        }

        if (!stack.isEmpty()) {
            stack = stack.copy();
            stack.setCount(1);
        }

        this.items.set(slot, stack);
        this.lastInteractedSlot = slot;
        this.markDirtyAndSync();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world == null) return false;
        if (this.world.getBlockEntity(this.pos) != this) return false;
        return player.squaredDistanceTo(
                this.pos.getX() + 0.5D,
                this.pos.getY() + 0.5D,
                this.pos.getZ() + 0.5D
        ) <= 64.0D;
    }

    @Override
    public void clear() {
        this.items.clear();
        this.lastInteractedSlot = -1;
        this.markDirtyAndSync();
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return ChiseledBookshelfBlock.isValidBook(stack);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return AVAILABLE_SLOTS;
    }

    /**
     * Hopper/dropper insertion parity:
     * insert into first empty slot.
     */
    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return this.items.get(slot).isEmpty() && ChiseledBookshelfBlock.isValidBook(stack);
    }

    /**
     * Hopper extraction parity:
     * any occupied slot may be extracted, but vanilla effectively pulls from
     * the first occupied slot the hopper can take.
     */
    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return !this.items.get(slot).isEmpty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.items.clear();
        Inventories.readNbt(nbt, this.items);
        this.lastInteractedSlot = nbt.contains("last_interacted_slot") ? nbt.getInt("last_interacted_slot") : -1;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.items);
        nbt.putInt("last_interacted_slot", this.lastInteractedSlot);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}