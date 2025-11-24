package net.ryan.beyond_the_block.mixin.Blocks;

import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Overwrites the onStateReplaced method to prevent a special "riddle book" from being dropped
 * when the lectern block's state is replaced. Instead of dropping the book, it clears it from
 * the lectern to ensure it is handled correctly.
 *
 * @author YourName
 * @reason Prevents the "riddle book" from being dropped and clears it from the lectern.
 */
@Mixin(LecternBlock.class)
public class LecternBlockMixin {

    /**
     * Overwrites the vanilla onStateReplaced method to implement custom behavior.
     * If the lectern contains a "riddle book", it will not be dropped when the state is replaced.
     * Instead, the book is cleared from the lectern to ensure it isn't lost.
     *
     * @author YourName
     * @reason Custom behavior to handle a special book that shouldn't be dropped when the lectern block state changes.
     */
    @Overwrite
    public void onStateReplaced(net.minecraft.block.BlockState state, World world, BlockPos pos, net.minecraft.block.BlockState newState, boolean moved) {
        // Check if the block is being replaced (broken)
        if (!state.isOf(newState.getBlock())) {
            // If the lectern has a book, prevent it from being dropped
            if (state.get(LecternBlock.HAS_BOOK)) {
                if (world instanceof ServerWorld) {
                    LecternBlockEntity lecternEntity = (LecternBlockEntity) world.getBlockEntity(pos);
                    if (lecternEntity != null) {
                        ItemStack book = lecternEntity.getBook();
                        if (isRiddleBook(book)) {
                            lecternEntity.clear();  // Clear the book without dropping it
                        } else {
                            // Default behavior: drop the book
                            dropBook(state, world, pos, book);
                        }
                    }
                }
            }

           // super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    // Helper method to check if the book is a riddle book
    private boolean isRiddleBook(ItemStack book) {
        return book.hasCustomName() && book.getName().getString().equals("Riddle Book");
    }

    // Helper method to drop the book
    private void dropBook(net.minecraft.block.BlockState state, World world, BlockPos pos, ItemStack book) {
//        Direction direction = state.get(LecternBlock.FACING);
//        ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5 + direction.getOffsetX() * 0.25,
//                pos.getY() + 1, pos.getZ() + 0.5 + direction.getOffsetZ() * 0.25, book);
//        itemEntity.setToDefaultPickupDelay();
//        world.spawnEntity(itemEntity);
    }
}

