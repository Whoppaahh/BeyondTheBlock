package net.ryan.beyond_the_block.mixin.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;
import net.ryan.beyond_the_block.content.registry.ModTrimRegistry;
import net.ryan.beyond_the_block.content.registry.family.ModArmourTrim;
import net.ryan.beyond_the_block.content.registry.family.ModTrimMaterial;
import net.ryan.beyond_the_block.content.registry.family.ModTrimPattern;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


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
@Inject(method = "getTooltip", at = @At("TAIL"))
private void beyond_the_block$addTrimTooltip(
        @Nullable PlayerEntity player,
        TooltipContext context,
        CallbackInfoReturnable<List<Text>> cir
) {
    ItemStack stack = (ItemStack)(Object)this;

    if (!(stack.getItem() instanceof ArmorItem)) {
        return;
    }

    ModArmourTrim.getTrim(stack).ifPresent(trim -> {
        ModTrimPattern pattern = ModTrimRegistry.getPattern(trim.patternId());
        ModTrimMaterial material = ModTrimRegistry.getMaterial(trim.materialId());

        if (pattern == null || material == null) {
            return;
        }

        List<Text> tooltip = cir.getReturnValue();

        tooltip.add(Text.empty());

        tooltip.add(Text.translatable("item.minecraft.smithing_template.upgrade")
                .formatted(Formatting.GRAY));

        tooltip.add(Text.literal(" ")
                .append(pattern.displayName())
                .formatted(Formatting.BLUE));

        tooltip.add(Text.literal(" ")
                .append(material.displayName())
                .styled(style -> style.withColor(getMaterialTooltipColor(trim.materialId()))));
    });
}
    @Unique
    private static int getMaterialTooltipColor(Identifier materialId) {
        return switch (materialId.getPath()) {
            case "quartz" -> 0xE3D4C4;
            case "iron" -> 0xECECEC;
            case "netherite" -> 0x625859;
            case "redstone" -> 0xB02E26;
            case "copper" -> 0xB4684D;
            case "gold" -> 0xDEB12D;
            case "emerald" -> 0x47A036;
            case "diamond" -> 0x6EECD2;
            case "lapis" -> 0x416E97;
            case "amethyst" -> 0x9A5CC6;
            default -> 0xFFFFFF;
        };
    }
}
