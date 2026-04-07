package net.ryan.beyond_the_block.content.enchantment.tools.pickaxes;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;
import net.ryan.beyond_the_block.content.enchantment.MyEnchantmentHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SmeltingStrikeEnchantment extends Enchantment {
    public SmeltingStrikeEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);

        PlayerBlockBreakEvents.BEFORE.register(this::onBlockBreak);
    }

    private boolean onBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (!(world instanceof ServerWorld serverWorld)) return true;

        ItemStack tool = player.getMainHandStack();
        if (EnchantmentHelper.getLevel(ModEnchantments.SMELTING_STRIKE, tool) == 0) return true;

        LootContext.Builder lootContextBuilder = new LootContext.Builder(serverWorld)
                .parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
                .parameter(LootContextParameters.TOOL, tool)
                .parameter(LootContextParameters.BLOCK_STATE, state)
                .parameter(LootContextParameters.THIS_ENTITY, player);

        List<ItemStack> originalDrops = state.getBlock().getDroppedStacks(state, lootContextBuilder);

        boolean smeltedAnything = false;
        List<ItemStack> finalDrops = new ArrayList<>();

        for (ItemStack original : originalDrops) {
            Optional<SmeltingRecipe> recipe = world.getRecipeManager().getFirstMatch(
                    RecipeType.SMELTING,
                    new SimpleInventory(original.copy()),
                    world
            );

            if (recipe.isPresent()) {
                ItemStack smelted = recipe.get().getOutput().copy();
                smelted.setCount(smelted.getCount() * original.getCount());
                finalDrops.add(smelted);
                smeltedAnything = true;

                float xpPerItem = recipe.get().getExperience();
                int totalXp = Math.round(xpPerItem * original.getCount());
                if (totalXp > 0) {
                    serverWorld.spawnEntity(new ExperienceOrbEntity(
                            serverWorld,
                            pos.getX() + 0.5,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5,
                            totalXp
                    ));
                }
            } else {
                finalDrops.add(original.copy());
            }
        }

        if (!smeltedAnything) {
            return true;
        }

        world.breakBlock(pos, false, player);
        tool.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
        MyEnchantmentHelper.giveDropsWithMode(world, pos, player, finalDrops);
        return false;
    }


    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof PickaxeItem || stack.isOf(Items.BOOK);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.SILK_TOUCH;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

}
