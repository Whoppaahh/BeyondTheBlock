package net.ryan.beyond_the_block.utils.helpers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

public class ItemStackHelper {
    public static List<ItemStack> mergeItemStacks(List<ItemStack> items) {
        Map<Item, List<ItemStack>> grouped = new HashMap<>();

        for (ItemStack stack : items) {
            if (stack.isEmpty()) continue;
            grouped.computeIfAbsent(stack.getItem(), k -> new ArrayList<>()).add(stack.copy());
        }

        List<ItemStack> merged = new ArrayList<>();

        for (List<ItemStack> group : grouped.values()) {
            while (!group.isEmpty()) {
                ItemStack base = group.remove(0);
                Iterator<ItemStack> iter = group.iterator();

                while (iter.hasNext()) {
                    ItemStack other = iter.next();
                    if (ItemStack.canCombine(base, other)) {
                        int transferable = Math.min(other.getCount(), base.getMaxCount() - base.getCount());
                        base.increment(transferable);
                        other.decrement(transferable);
                        if (other.isEmpty()) iter.remove();
                    }
                }

                merged.add(base);
            }
        }

        return merged;
    }

}
