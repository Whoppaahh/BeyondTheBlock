package net.ryan.beyond_the_block.content.item.armour;

import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class WolfArmourItem extends Item implements DyeableItem {
    private final int protection;
    private final Item repairItem;
    private final int tintColor;

    public WolfArmourItem(int protection, Item repairItem, int tintColor, Settings settings) {
        super(settings.maxCount(1).maxDamage(128));
        this.protection = protection;
        this.repairItem = repairItem;
        this.tintColor = tintColor;
    }

    public int getProtection() {
        return this.protection;
    }

    public int getTintColor() {
        return this.tintColor;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(this.repairItem) || super.canRepair(stack, ingredient);
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound display = stack.getSubNbt("display");
        return display != null && display.contains("color", 99)
                ? display.getInt("color")
                : this.tintColor;
    }
}
