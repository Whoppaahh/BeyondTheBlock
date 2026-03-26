package net.ryan.beyond_the_block.content.block.Entity.ShrineEntity.PlayerInputEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.content.block.Entity.ModBlockEntities;
import net.ryan.beyond_the_block.utils.GUI.ImplementedInventory;
import org.jetbrains.annotations.Nullable;

public class DoubleInputBlockEntity extends BlockEntity implements ImplementedInventory {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private float rotation = 0;
    private Item requiredItem0, requiredItem1;

    public void setRequiredItems(Item item0, Item item1) {
        this.requiredItem0 = item0;
        this.requiredItem1 = item1;
    }

    public boolean validateInputs() {
        if (requiredItem0 == null || requiredItem1 == null) return false;
        ItemStack stack0 = inventory.get(0);
        ItemStack stack1 = inventory.get(1);

        if (stack0.isEmpty() || stack1.isEmpty()) return false;

        Item input0 = stack0.getItem();
        Item input1 = stack1.getItem();

        // Accept either order
        return (input0 == requiredItem0 && input1 == requiredItem1) ||
                (input0 == requiredItem1 && input1 == requiredItem0);
    }


    public DoubleInputBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DOUBLE_INPUT_BLOCK_ENTITY, pos, state);
    }

    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public float getRenderingRotation() {
        rotation += 0.5f;
        if(rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }
    public void sync() {
        if (world != null && !world.isClient) {
            world.markDirty(pos);
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
            if(world instanceof ServerWorld serverWorld){
                serverWorld.getChunkManager().markForUpdate(pos);
            }
        //    System.out.println("Syncing block entity, Slot 0: " + getStack(0) + ", Slot 1: " + getStack(1));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
        Inventories.readNbt(nbt, inventory);

      //  EmeraldEmpire.LOGGER.info("readNBT called on client. Slot 0: " + inventory.get(0) + ", Slot 1: " + inventory.get(1));
    }
    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (!stack.isEmpty() && world != null && !world.isClient) {
            sync(); // auto-sync
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
