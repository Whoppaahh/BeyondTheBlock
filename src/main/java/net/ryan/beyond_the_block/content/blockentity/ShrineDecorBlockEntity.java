package net.ryan.beyond_the_block.content.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.block.shrine.AbstractShrineBlockEntity;
import net.ryan.beyond_the_block.content.registry.ModBlockEntities;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.utils.visual.ImplementedInventory;

import java.util.Random;

public class ShrineDecorBlockEntity extends AbstractShrineBlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> item = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private float rotation = 0;
    private boolean initialized = false;


    public ShrineDecorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHRINE_DECOR_BLOCK_ENTITY, pos, state);
    }


    public static void tick(World world, BlockPos pos, BlockState state, ShrineDecorBlockEntity blockEntity) {
        if (!world.isClient && !blockEntity.initialized) {
            blockEntity.initializeRubies();
            blockEntity.initialized = true;
            blockEntity.markDirty();
        }
    }

    private void initializeRubies() {
        Random random = new Random();
        boolean hasRuby = random.nextBoolean();

        if (hasRuby) {
            this.item.set(0, new ItemStack(ModItems.RUBY_ITEM, 1));
        } else {
            this.item.set(0, ItemStack.EMPTY);
        }
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
        nbt.putBoolean("Initialized", initialized);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, item);
        initialized = nbt.getBoolean("Initialized");
    }


    public int getUseCount() {
        return item.get(0).getCount();
    }
    public void incrementUseCount(){
        if (item.get(0).isEmpty()) {
            item.set(0, new ItemStack(ModItems.RUBY_ITEM, 1));
        } else {
            item.get(0).increment(1);
        }
    }
    public void resetUseCount(){
        item.set(0, ItemStack.EMPTY);
    }
}
