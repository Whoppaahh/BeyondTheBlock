package net.ryan.beyond_the_block.mixin.item;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.feature.paths.PathBuilder;
import net.ryan.beyond_the_block.feature.paths.PathToolHelper;
import net.ryan.beyond_the_block.feature.paths.PathUndoEntry;
import net.ryan.beyond_the_block.feature.paths.PathUndoManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ShovelItem.class)
public class ItemPathMixin {

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$pathingUseOnBlock(ItemUsageContext context,
                                                    CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        if (world.isClient()) return;
            // Server handles placement; client handles preview separately

        ItemStack stack = context.getStack();
        if (!(stack.getItem() instanceof ShovelItem)) return;
        if (!Configs.server().features.paths.enabled) return;

        PlayerEntity player = context.getPlayer();
        if (player == null) return;

        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        Set<Block> allowedStart = PathToolHelper.resolveBlockList(Configs.server().features.paths.allowedStartingBlocks);
        Set<Block> allowedEnd = PathToolHelper.resolveBlockList(Configs.server().features.paths.allowedEndingBlocks);

        boolean hasStart = PathToolHelper.hasStart(stack);
        boolean sneaking = player.isSneaking();
        boolean validStartBlock = allowedStart.contains(state.getBlock());
        boolean validEndBlock = allowedEnd.contains(state.getBlock());

        // FIRST CLICK: start a new path.
        // This must win over undo if the clicked block is a valid start block.
        if (!hasStart && sneaking && validStartBlock) {
            PathToolHelper.setStart(stack, pos);
            player.sendMessage(Text.literal("Path start set: " + pos.toShortString()), true);
            cir.setReturnValue(ActionResult.SUCCESS);
            return;
        }


        // UNDO: only when there is no active start point and the click was not a valid new start.
        if (!hasStart && sneaking && !validStartBlock && PathUndoManager.hasUndo(player)) {
            PathUndoEntry entry = PathUndoManager.pop(player);
            if (entry != null) {
                undoEntry(world, entry);
                player.sendMessage(Text.literal("Undid last path"), true);
                cir.setReturnValue(ActionResult.SUCCESS);
            }
            return;
        }

        // No active start and not starting a path: let vanilla shovel behavior happen
        if (!hasStart) {
            return;
        }

        // SECOND CLICK: build path from stored start to this pos
        if (!validEndBlock) {
            // Keep the stored start so the player can try another end point
            return;
        }

        BlockPos start = PathToolHelper.getStart(stack);
        PathBuilder.buildPath(world, player, stack, start, pos, Configs.server());

        cir.setReturnValue(ActionResult.SUCCESS);
    }
    @Unique
    private void undoEntry(World world, PathUndoEntry entry) {
        for (PathUndoEntry.BlockChange change : entry.changes) {
            world.setBlockState(change.pos(), change.before(), Block.NOTIFY_ALL);
        }
    }

}

