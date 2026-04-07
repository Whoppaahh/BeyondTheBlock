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
import net.ryan.beyond_the_block.content.registry.ModEnchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(
            method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void beyond_the_block$applyDurabilityBoost(int amount,
                                                       Random random,
                                                       @Nullable ServerPlayerEntity player,
                                                       CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (player == null || amount <= 0) return;
        if (!(stack.getItem() instanceof ArmorItem)) return;


        int level = EnchantmentHelper.getLevel(ModEnchantments.DURABILITY_BOOST, player.getEquippedStack(EquipmentSlot.CHEST));
        if (level <= 0) return;


        float ignoreChance = switch (level) {
            case 1 -> 0.25f;
            case 2 -> 0.50f;
            default -> 0.75f;
        };

        if (random.nextFloat() >= ignoreChance) return;


        // Cancel durability loss entirely for this call.
        cir.setReturnValue(false);
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
