package net.ryan.beyond_the_block.content.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.content.registry.ModBlockEntities;
import net.ryan.beyond_the_block.utils.visual.ImplementedInventory;
import org.jetbrains.annotations.Nullable;

public class ShrineDecorBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> item = DefaultedList.ofSize(1, ModItems.RUBY_ITEM.getDefaultStack());
    private float rotation = 0;
    private int useCount = 0;


    public ShrineDecorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHRINE_DECOR_BLOCK_ENTITY, pos, state);
    }


    @Override
    public DefaultedList<ItemStack> getItems() {
        return item;
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
        Inventories.writeNbt(nbt, item);
        nbt.putInt("UseCount", useCount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, item);
        useCount = nbt.getInt("UseCount");
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

    public int getUseCount() {
        return useCount;
    }
    public void incrementUseCount(){
        useCount++;
    }
    public void resetUseCount(){
        useCount = 0;
    }
}
