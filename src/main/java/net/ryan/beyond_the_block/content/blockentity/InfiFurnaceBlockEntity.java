package net.ryan.beyond_the_block.content.blockentity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.ryan.beyond_the_block.content.block.InfiFurnaceBlock;
import net.ryan.beyond_the_block.screen.handler.InfiFurnaceScreenHandler;
import net.ryan.beyond_the_block.utils.visual.ImplementedInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class InfiFurnaceBlockEntity extends LockableContainerBlockEntity implements SidedInventory, ExtendedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private int cookTime;
    private int cookTimeTotal = 200; // ticks to cook a single item


    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch(index) {
                case 0 -> cookTime;
                case 1 -> cookTimeTotal;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0 -> cookTime = value;
                case 1 -> cookTimeTotal = value;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }


    public InfiFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INFI_FURNACE_BLOCK_ENTITY, pos, state);
    }

    public void tick() {
        if (world == null || world.isClient) return;

        ItemStack input = inventory.get(0);
        ItemStack output = inventory.get(1);

        if (!input.isEmpty()) {
            Optional<SmeltingRecipe> recipeOpt = world.getRecipeManager()
                    .getFirstMatch(RecipeType.SMELTING, new SimpleInventory(input), world);

            if (recipeOpt.isPresent()) {

                SmeltingRecipe recipe = recipeOpt.get();
                ItemStack result = recipe.getOutput();

                if (canAcceptOutput(output, result)) {
                    cookTime++;
                    setLit(true);
                    if (cookTime >= cookTimeTotal) {
                        cookTime = 0;
                        input.decrement(1);
                        if (input.isEmpty()) {
                            inventory.set(0, ItemStack.EMPTY);
                        }
                        if (output.isEmpty()) {
                            inventory.set(1, result.copy());
                        } else {
                            output.increment(result.getCount());
                            inventory.set(1, output);
                        }
                        markDirty();
                    }
                } else {
                    cookTime = 0;
                    setLit(false);
                }
            } else {
                cookTime = 0;
                setLit(false);
            }
        } else {
            cookTime = 0;
            setLit(false);
        }
    }

    private boolean canAcceptOutput(ItemStack output, ItemStack result) {
        return output.isEmpty()
                || (ItemStack.canCombine(output, result)
                && output.getCount() + result.getCount() <= output.getMaxCount());
    }

    private void setLit(boolean lit) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof InfiFurnaceBlock && state.get(InfiFurnaceBlock.LIT) != lit) {
            world.setBlockState(pos, state.with(InfiFurnaceBlock.LIT, lit), 3);
        }
    }

    // Inventory methods for LockableContainerBlockEntity

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(inventory, slot, amount);
        if (!result.isEmpty()) markDirty();
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
        markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (world.getBlockEntity(pos) != this) return false;
        return player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    // NamedScreenHandlerFactory implementation
    @Override
    public Text getDisplayName() {
        return Text.literal("Infi Furnace");
    }

    @Override
    protected Text getContainerName() {
        return null;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new InfiFurnaceScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return null;
    }

    // SidedInventory methods for automation (hoppers)

    private static final int[] TOP_SLOTS = new int[] {0};
    private static final int[] BOTTOM_SLOTS = new int[] {1};

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) return BOTTOM_SLOTS;
        if (side == Direction.UP) return TOP_SLOTS;
        return new int[1];
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot != 1; // no insertion into output slot
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
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

