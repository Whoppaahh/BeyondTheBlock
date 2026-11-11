package net.ryan.beyond_the_block.mixin.network;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PacketByteBuf.class)
public abstract class PacketByteBufMixin {

    /**
     * Writes an ItemStack using a VarInt for its count instead of a byte.
     */
    @Inject(
            method = "writeItemStack",
            at = @At("HEAD"),
            cancellable = true
    )
    private void writeLargeItemStack(ItemStack stack, CallbackInfoReturnable<PacketByteBuf> cir) {
        PacketByteBuf buf = (PacketByteBuf) (Object) this;

        buf.writeBoolean(!stack.isEmpty());

        if (!stack.isEmpty()) {
            if (stack.getCount() > 64) {
                System.out.println(
                        "[PACKET ITEM WRITE] Item: " + stack.getItem()
                                + " | Count: " + stack.getCount()
                );
            }
            buf.writeVarInt(Registry.ITEM.getRawId(stack.getItem()));
            buf.writeVarInt(stack.getCount());
            buf.writeNbt(stack.getNbt());
        }

        cir.setReturnValue(buf);
    }

    /**
     * Reads an ItemStack using a VarInt for its count instead of a byte.
     */
    @Inject(
            method = "readItemStack",
            at = @At("HEAD"),
            cancellable = true
    )
    private void readLargeItemStack(CallbackInfoReturnable<ItemStack> cir) {
        PacketByteBuf buf = (PacketByteBuf) (Object) this;

        if (!buf.readBoolean()) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }

        int rawId = buf.readVarInt();
        int count = buf.readVarInt();
        System.out.println(
                "[PACKET ITEM READ] Item: " + rawId
                        + " | Count: " + count
        );
        NbtCompound nbt = buf.readNbt();

        Item item = Registry.ITEM.get(rawId);

        if (item == Items.AIR || count <= 0) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }

        ItemStack stack = new ItemStack(item, count);
        stack.setNbt(nbt);

        cir.setReturnValue(stack);
    }
}