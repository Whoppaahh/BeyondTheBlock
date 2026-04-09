package net.ryan.beyond_the_block.feature.interaction.handlers;

import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;

public final class FertilityInteractionHandler {

    private FertilityInteractionHandler() {
    }

    public static ActionResult handle(ServerPlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (hand != Hand.MAIN_HAND) {
            return ActionResult.PASS;
        }

        ItemStack tool = player.getMainHandStack();
        if (!(tool.getItem() instanceof ShovelItem)) {
            return ActionResult.PASS;
        }

        int level = EnchantmentHelper.getLevel(ModEnchantments.FERTILITY, tool);
        if (level <= 0) {
            return ActionResult.PASS;
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.PASS;
        }

        if (player.isSneaking()) {
            return ActionResult.PASS;
        }

        BlockPos pos = hitResult.getBlockPos();
        var state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof Fertilizable fertilizable) {
            if (fertilizable.isFertilizable(world, pos, state, world.isClient)) {
                if (fertilizable.canGrow(world, world.random, pos, state)) {
                    fertilizable.grow(serverWorld, serverWorld.random, pos, state);
                    return ActionResult.SUCCESS;
                }
            } else {
                world.spawnEntity(new ItemEntity(
                        world,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        new ItemStack(block.asItem())
                ));
                return ActionResult.SUCCESS;
            }
        } else if (block instanceof PlantBlock
                || block instanceof CactusBlock
                || block instanceof SugarCaneBlock
                || block instanceof GourdBlock
                || block instanceof KelpPlantBlock
                || block instanceof LeavesBlock
                || block instanceof VineBlock
                || block instanceof TallPlantBlock) {
            world.spawnEntity(new ItemEntity(
                    world,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    new ItemStack(block.asItem())
            ));
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}