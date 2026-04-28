package net.ryan.beyond_the_block.content.item.armour;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class WolfArmourItem extends Item {
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

}
