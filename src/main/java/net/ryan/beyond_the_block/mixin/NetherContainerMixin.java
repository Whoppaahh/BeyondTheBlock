package net.ryan.beyond_the_block.mixin;

import net.minecraft.block.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.mixin.Accessors.*;
import net.ryan.beyond_the_block.utils.Helpers.ServerContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({
        ChestBlockEntity.class,
        HopperBlockEntity.class,
        BarrelBlockEntity.class,
        DispenserBlockEntity.class,
        AbstractFurnaceBlockEntity.class
})
public abstract class NetherContainerMixin {
    @Inject(method = "writeNbt", at = @At("RETURN"))
    private void netherLargeStackSave(NbtCompound nbt, CallbackInfo ci) {
        MinecraftServer server = ServerContext.getServer();
        if (server == null || server.getWorld(World.NETHER) == null) return;

        DefaultedList<ItemStack> stacks = getInventory();
        if (stacks == null) return;

        NbtList list = new NbtList();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            if (!stack.isEmpty()) {
                NbtCompound stackTag = new NbtCompound();
                stack.writeNbt(stackTag);

                // Preserve large counts
                if (stack.getCount() > 64) {
                    stackTag.putInt("BeyondCount", stack.getCount());
                }

                stackTag.putByte("Slot", (byte) i);
                list.add(stackTag);
            }
        }
        nbt.put("Items", list);
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void netherLargeStackLoad(NbtCompound nbt, CallbackInfo ci) {
        MinecraftServer server = ServerContext.getServer();
        if (server == null || server.getWorld(World.NETHER) == null) return;

        DefaultedList<ItemStack> stacks = getInventory();

        if (stacks == null) return;

        if (nbt.contains("Items", 9)) { // 9 = TAG_List
            for (var element : nbt.getList("Items", 10)) { // 10 = TAG_Compound
                NbtCompound stackTag = (NbtCompound) element;
                int slot = stackTag.getByte("Slot") & 0xFF;
                if (slot < stacks.size()) {
                    ItemStack stack = ItemStack.fromNbt(stackTag);
                    // Restore BeyondCount if present
                    if (stackTag.contains("BeyondCount", 3)) {
                        stack.setCount(stackTag.getInt("BeyondCount"));
                    }
                    stacks.set(slot, stack);
                }
            }
        }
    }

    @Unique
    private @Nullable DefaultedList<ItemStack> getInventory() {
        DefaultedList<ItemStack> stacks = null;

        if ((Object)this instanceof ChestBlockEntity c) {
            stacks = ((ChestBlockEntityAccessor) c).getInvStackList();
        } else if ((Object)this instanceof HopperBlockEntity h) {
            stacks = ((HopperBlockEntityAccessor) h).getInvStackList();
        } else if ((Object)this instanceof BarrelBlockEntity b) {
            stacks = ((BarrelBlockEntityAccessor) b).getInvStackList();
        } else if ((Object)this instanceof DispenserBlockEntity d) {
            stacks = ((DispenserBlockEntityAccessor) d).getInvStackList();
        } else if ((Object)this instanceof AbstractFurnaceBlockEntity ab) {
            stacks = ((AbstractFurnaceAccessor) ab).getInvStackList();
        }
        return stacks;
    }
}
