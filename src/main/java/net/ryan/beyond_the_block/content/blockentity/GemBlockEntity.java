package net.ryan.beyond_the_block.content.blockentity;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.screen.handler.GemScreenHandler;
import net.ryan.beyond_the_block.utils.visual.ImplementedInventory;
import net.ryan.beyond_the_block.utils.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GemBlockEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
    public boolean hasLoaded = false;
    public boolean hasChanged = false;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> hasLoaded ? 1 : 0;
                case 1 -> hasChanged ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> hasLoaded = value != 0;
                case 1 -> hasChanged = value != 0;
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

    public GemBlockEntity(BlockPos pos, BlockState state) {

        super(ModBlockEntities.GEM_BLOCK_ENTITY, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Gem Station");
    }


    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putBoolean("hasLoaded", hasLoaded);
        nbt.putBoolean("hasChanged", hasChanged);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        hasLoaded = nbt.getBoolean("hasLoaded");
        hasChanged = nbt.getBoolean("hasChanged");
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeBlockPos(this.pos);
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeBlockPos(this.pos);
        return new GemScreenHandler(syncId, inv, this, this.getPropertyDelegate());
    }


    private boolean isValidTool(ItemStack stack) {
        return stack.getItem().getDefaultStack().isIn(ModTags.Items.GEM_UPGRADABLE_TOOLS);
    }


    // Helper method to match gem items to name strings
    private String getGemName(ItemStack stack) {
        if (stack.isOf(ModItems.MIRANITE_ITEM)) return "Miranite";
        if (stack.isOf(ModItems.CHROMITE_ITEM)) return "Chromite";
        if (stack.isOf(ModItems.NOCTURNITE_ITEM)) return "Nocturnite";
        if (stack.isOf(ModItems.AMBERINE_ITEM)) return "Amberine";
        if (stack.isOf(ModItems.AZUROS_ITEM)) return "Azuros";
        if (stack.isOf(ModItems.INDIGRA_ITEM)) return "Indigra";
        if (stack.isOf(ModItems.ROSETTE_ITEM)) return "Rosette";
        return null;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ImplementedInventory.super.setStack(slot, stack);
        if (slot != 4) {
            onInventoryChanged(slot);
        }
    }

    private void onInventoryChanged(int slot) {
        if (world == null || world.isClient) return;

        //Return early if no tool present, or it's an invalid tool/shouldn't be the case as insert stack prevents no mod swords from being entered
        int TOOL_INPUT_SLOT = 0;
        int TOOL_OUTPUT_SLOT = 4;

        ItemStack tool = inventory.get(TOOL_INPUT_SLOT);
        if (tool.isEmpty() || !isValidTool(tool)) {
            inventory.set(TOOL_OUTPUT_SLOT, ItemStack.EMPTY);
            return;
        }

        List<ItemStack> gems = getGemsFromNBT(); //Load existing gems on the tool from NBT

        if(!hasLoaded && slot == TOOL_INPUT_SLOT) {
            for (int i = 0; i < gems.size(); i++) {
                inventory.set(i + 1, gems.get(i)); // Set them to the gem slots in GUI
            }
            hasLoaded = true; //Prevents duplicates, would automatically read and refill slots on gem removal without this
        }

        List<ItemStack> gemsSlots = getGemsFromSlots(); //Once existing gems have been loaded, get and store them from the GUI slots
        ItemStack result = tool.copy(); //Create copy ready for output
        result.setCount(1);

        if(gemsSlots.isEmpty() && slot != TOOL_INPUT_SLOT){ //Gem Slots are empty
            BeyondTheBlock.LOGGER.info("No Gems to Apply");
            result.setNbt(null);
            inventory.set(TOOL_OUTPUT_SLOT, result);
            return;
        }

        BeyondTheBlock.LOGGER.info("Gem Slots: {}", gemsSlots);
        BeyondTheBlock.LOGGER.info("Gem NBT: {}", gems);

        if(areGemsEqual(gemsSlots, gems)){ //Gem Slots are the same as NBT data
            BeyondTheBlock.LOGGER.info("No Changes");
            hasChanged = false;
            inventory.set(TOOL_OUTPUT_SLOT, ItemStack.EMPTY);
            return;
        }

        BeyondTheBlock.LOGGER.info("Gem Change Detected - Creating New Tool");
        hasChanged = true;
        // Build new NBT list for gems
        NbtCompound nbt = result.getOrCreateNbt();
        NbtList gemList = nbt.contains("GemList", NbtElement.STRING_TYPE)
                ? nbt.getList("GemList", NbtElement.STRING_TYPE)
                : new NbtList();

        //Assign new NBT data
        for (int i = 1; i <= 3; i++) {
            ItemStack gem = inventory.get(i);
            if (gem.isEmpty()) continue;

            String gemName = getGemName(gem);
            if (gemName == null) continue;

            for (NbtElement element : gemList) {
                if (element.asString().equals(gemName)) {
                    break;
                }
            }

            if ( gemList.size() < 3) {
                gemList.add(NbtString.of(gemName));
                if (gem.isEmpty()){
                    inventory.set(i, ItemStack.EMPTY);
                }
            }
        }

        nbt.put("GemList", gemList);
        result.setNbt(nbt);
        inventory.set(TOOL_OUTPUT_SLOT, result);
        markDirty();
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
    }

    private boolean areGemsEqual(List<ItemStack> a, List<ItemStack> b) {
        if (a.size() != b.size()) return false;

        for (int i = 0; i < a.size(); i++) {
            if (!ItemStack.canCombine(a.get(i), b.get(i))) return false;
        }
        return true;
    }


    private List<ItemStack> getGemsFromSlots(){
        ItemStack tool = inventory.get(0);
        List<ItemStack> gems = new ArrayList<>();
        if (tool.isEmpty() || !tool.hasNbt()) return gems;

        for(int i = 1; i <= 3; i++){
            if(!inventory.get(i).isEmpty()){
                gems.add(inventory.get(i));
            }
        }
        return gems;
    }

    private List<ItemStack> getGemsFromNBT() {
        ItemStack tool = inventory.get(0);
        List<ItemStack> gems = new ArrayList<>();

        if (tool.isEmpty() || !tool.hasNbt()) return gems;

        NbtCompound nbt = tool.getNbt();
        if (!(nbt != null && nbt.contains("GemList", NbtElement.LIST_TYPE))) return gems;

        NbtList gemList = nbt.getList("GemList", NbtElement.STRING_TYPE);

        for (NbtElement element : gemList) {
            String gemName = element.asString();
            Item gemItem = switch (gemName) {
                case "Miranite" -> ModItems.MIRANITE_ITEM;
                case "Chromite" -> ModItems.CHROMITE_ITEM;
                case "Nocturnite" -> ModItems.NOCTURNITE_ITEM;
                case "Amberine" -> ModItems.AMBERINE_ITEM;
                case "Azuros" -> ModItems.AZUROS_ITEM;
                case "Indigra" -> ModItems.INDIGRA_ITEM;
                case "Rosette" -> ModItems.ROSETTE_ITEM;
                default -> null;
            };
            if (gemItem != null) {
                gems.add(new ItemStack(gemItem));
            }
        }

        return gems;
    }

}
