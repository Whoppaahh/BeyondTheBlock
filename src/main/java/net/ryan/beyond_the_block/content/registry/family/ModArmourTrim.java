package net.ryan.beyond_the_block.content.registry.family;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.Optional;

public final class ModArmourTrim {
    public static final String TRIM_KEY = "Trim";
    public static final String PATTERN_KEY = "pattern";
    public static final String MATERIAL_KEY = "material";

    private ModArmourTrim() {
    }

    public record Data(Identifier patternId, Identifier materialId) {
    }

    public static Optional<Data> getTrim(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.hasNbt()) {
            return Optional.empty();
        }

        NbtCompound root = stack.getNbt();
        if (root == null || !root.contains(TRIM_KEY, NbtCompound.COMPOUND_TYPE)) {
            return Optional.empty();
        }

        NbtCompound trimNbt = root.getCompound(TRIM_KEY);
        if (!trimNbt.contains(PATTERN_KEY, NbtCompound.STRING_TYPE) || !trimNbt.contains(MATERIAL_KEY, NbtCompound.STRING_TYPE)) {
            return Optional.empty();
        }

        try {
            Identifier patternId = new Identifier(trimNbt.getString(PATTERN_KEY));
            Identifier materialId = new Identifier(trimNbt.getString(MATERIAL_KEY));
            return Optional.of(new Data(patternId, materialId));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    public static void setTrim(ItemStack stack, Identifier patternId, Identifier materialId) {
        if (stack == null || stack.isEmpty()) {
            return;
        }

        NbtCompound trimNbt = new NbtCompound();
        trimNbt.putString(PATTERN_KEY, patternId.toString());
        trimNbt.putString(MATERIAL_KEY, materialId.toString());

        stack.getOrCreateNbt().put(TRIM_KEY, trimNbt);
    }

    public static boolean isTrimmable(ItemStack stack) {
        return stack != null
                && !stack.isEmpty()
                && stack.getItem() instanceof ArmorItem armorItem
                && armorItem.getSlotType().getType() == net.minecraft.entity.EquipmentSlot.Type.ARMOR;
    }
}