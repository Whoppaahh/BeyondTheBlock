package net.ryan.beyond_the_block.utils.helpers;


import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.ChiseledBookshelfBlockEntity;
import net.ryan.beyond_the_block.content.registry.ModBlocks;

public final class EnchantingPowerHelper {
    private EnchantingPowerHelper() {
    }

    public static boolean canAccessCustomBookshelf(World world, BlockPos tablePos, BlockPos offset) {
        BlockPos providerPos = tablePos.add(offset);
        BlockState providerState = world.getBlockState(providerPos);

        boolean validProvider =
                providerState.isOf(Blocks.BOOKSHELF) ||
                        providerState.isOf(ModBlocks.CHISELED_BOOKSHELF);

        if (!validProvider) {
            return false;
        }

        // Same gap rule vanilla uses conceptually:
        // the midpoint between the table and the provider must be air.
        BlockPos gapPos = tablePos.add(offset.getX() / 2, offset.getY(), offset.getZ() / 2);
        return world.isAir(gapPos);
    }

    public static int getBookshelfPower(World world, BlockPos providerPos) {
        BlockState state = world.getBlockState(providerPos);

        if (state.isOf(Blocks.BOOKSHELF)) {
            return 1;
        }

        if (state.isOf(ModBlocks.CHISELED_BOOKSHELF)) {
            BlockEntity be = world.getBlockEntity(providerPos);
            if (be instanceof ChiseledBookshelfBlockEntity shelf) {
                return shelf.getOccupiedSlotCount();
            }
        }

        return 0;
    }
}