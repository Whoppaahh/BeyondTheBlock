package net.ryan.beyond_the_block.mixin.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import net.ryan.beyond_the_block.enchantment.ModEnchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void ApplyDurabilityBoost(int amount, Random random, @Nullable ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        // Check if this is a chestplate
        if (stack.getItem() instanceof ArmorItem && player != null) {

            ItemStack chestplate = player.getEquippedStack(EquipmentSlot.CHEST);

            int level = EnchantmentHelper.getLevel(ModEnchantments.DURABILITY_BOOST, chestplate);
            if (level > 0) {
                // Reduce damage taken based on level. Each level reduces 25%, e.g., level 2 = 50% reduction
                float reduction = 1.0f - (0.25f * level);
                int reducedAmount = Math.max(1, Math.round(amount * reduction));

                stack.setDamage(stack.getDamage() + reducedAmount);
                if (stack.getDamage() >= stack.getMaxDamage()) {
                    stack.decrement(1);
                }

                cir.setReturnValue(true); // prevent default logic from running
            }
        }
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeNetherCount(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (self.getCount() > 64) {
            Object holder = self.getHolder();
            if (holder instanceof BlockEntity) {
                nbt.putInt("BeyondCount", self.getCount());
            }
        }
    }

    @Inject(method = "fromNbt", at = @At("TAIL"))
    private static void readNetherCount(NbtCompound nbt, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = cir.getReturnValue();
        if (stack == null) return;
        if (nbt.contains("BeyondCount", NbtElement.INT_TYPE)) {
            stack.setCount(nbt.getInt("BeyondCount"));
        }
    }
//    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
//    private void netherLargeStack(CallbackInfoReturnable<Integer> cir) {
//        ItemStack self = (ItemStack) (Object) this;
//
//        // Determine if the stack belongs to a player currently in the Nether
//        MinecraftServer server = ServerContext.getServer();
//        if (server == null) return;
//
//        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
//            if (player.getWorld().getRegistryKey() == World.NETHER) {
//                // Check if the player has this stack in their inventory or hands
//                if (player.getInventory().contains(self)
//                        || player.getMainHandStack() == self
//                        || player.getOffHandStack() == self) {
//                    int base = self.getItem().getMaxCount();
//                    cir.setReturnValue(base > 1 ? base * 8 : base);
//                    return;
//                }
//            }
//        }
//    }

}
