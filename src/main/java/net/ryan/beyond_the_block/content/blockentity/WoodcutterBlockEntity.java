package net.ryan.beyond_the_block.content.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.content.recipes.WoodcuttingRecipe;
import net.ryan.beyond_the_block.screen.handler.WoodcutterScreenHandler;
import net.ryan.beyond_the_block.utils.visual.ImplementedInventory;

import java.util.List;

public class WoodcutterBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private List<WoodcuttingRecipe> availableRecipes = List.of();
    private int selectedRecipe = -1;

    public WoodcutterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WOODCUTTER_BLOCK_ENTITY, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.beyond_the_block.woodcutter");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new WoodcutterScreenHandler(syncId, inv, this);
    }

    public List<WoodcuttingRecipe> getAvailableRecipes() {
        return availableRecipes;
    }

    public void setAvailableRecipes(List<WoodcuttingRecipe> recipes) {
        this.availableRecipes = recipes;
    }

    public int getSelectedRecipe() {
        return selectedRecipe;
    }

    public void setSelectedRecipe(int selectedRecipe) {
        this.selectedRecipe = selectedRecipe;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }
}