package net.ryan.beyond_the_block.content.recipes;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.item.ModItems;

public class GemSmithingRecipe extends SmithingRecipe {
    private final Ingredient base;
    private final Ingredient addition;

    public GemSmithingRecipe(Identifier id, Ingredient base, Ingredient addition, ItemStack result) {
        super(id, base, addition, result);
        this.base = base;
        this.addition = addition;
    }

    public Ingredient getBase() {
        return base;
    }

    public Ingredient getAddition() {
        return addition;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        ItemStack baseStack = inventory.getStack(0); // base item (sword)
        ItemStack addition = inventory.getStack(1);  // gem being added

        if (!getBase().test(baseStack) || !getAddition().test(addition)) return false;

        NbtCompound nbt = baseStack.getNbt();
        if (nbt != null && nbt.contains("GemList", NbtElement.LIST_TYPE)) {
            NbtList gemList = nbt.getList("GemList", NbtElement.STRING_TYPE);

            // Don't allow more than 3 gems
            if (gemList.size() >= 3) {
                boolean isDuplicate = false;
                for (NbtElement gem : gemList) {
                    if (gem.asString().equals(addition.getItem().getName().getString())) {
                        isDuplicate = true;
                        break;
                    }
                }

                // Allow replacing existing gem
                return isDuplicate;
            }
        }

        return true;
    }


    @Override
    public ItemStack craft(Inventory inventory) {
        ItemStack base = inventory.getStack(0);  // base item (e.g., Ruby Sword)
        ItemStack addition = inventory.getStack(1);  // gem
        ItemStack result = base.copy(); // base item as output
        result.setCount(1);

        NbtCompound gemNbt = result.getOrCreateNbt();

        // Get or create the gem list
        NbtList gemList = gemNbt.contains("GemList", NbtElement.LIST_TYPE)
                ? gemNbt.getList("GemList", NbtElement.STRING_TYPE)
                : new NbtList();

        // Don't add duplicates, and limit to 3 gems
        if (gemList.size() < 3) {
            String gemName = getGemName(addition);
            boolean alreadyPresent = false;

            for (int i = 0; i < gemList.size(); i++) {
                if (gemList.getString(i).equals(gemName)) {
                    alreadyPresent = true;
                    break;
                }
            }

            if (gemName != null && !alreadyPresent) {
                gemList.add(NbtString.of(gemName));
                gemNbt.put("GemList", gemList);
                //EmeraldEmpire.LOGGER.info("Gem added: " + gemName);
            }
        }

        return result;
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


}
