package net.ryan.beyond_the_block.block.Entity.ShrineEntity.PlayerInputEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.block.Entity.ModBlockEntities;
import net.ryan.beyond_the_block.utils.GUI.ImplementedInventory;
import org.jetbrains.annotations.Nullable;

public class SingleInputBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private float rotation = 0;

    private Item requiredItem;

    public void setRequiredItem(Item item) {
        this.requiredItem = item;
    }

    public Item getRequiredItem() {
        return requiredItem;
    }


    public SingleInputBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SINGLE_INPUT_BLOCK_ENTITY, pos, state);
    }


    @Override
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
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        if(requiredItem != null){
            nbt.putString("RequiredItem", Registry.ITEM.getId(requiredItem).toString());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        if (nbt.contains("RequiredItem")) {
            requiredItem = Registry.ITEM.get(new Identifier(nbt.getString("RequiredItem")));
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
