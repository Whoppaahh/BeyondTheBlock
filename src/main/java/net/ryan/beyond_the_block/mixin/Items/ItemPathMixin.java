package net.ryan.beyond_the_block.mixin.Items;


import me.shedaniel.autoconfig.AutoConfig;
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
import net.ryan.beyond_the_block.config.ModConfig;
import net.ryan.beyond_the_block.utils.Helpers.PathBuilder;
import net.ryan.beyond_the_block.utils.Helpers.PathToolHelper;
import net.ryan.beyond_the_block.utils.Helpers.PathUndoManager;
import net.ryan.beyond_the_block.utils.PathUndoEntry;
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
        if (world.isClient()) {
            // Server handles placement; client handles preview separately
            return;
        }

        ItemStack stack = context.getStack();
        if (!(stack.getItem() instanceof ShovelItem)) {
            return;
        }

        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).get();
        if (!config.pathConfig.enabled) {
            return;
        }

        PlayerEntity player = context.getPlayer();
        if (player == null) return;

        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        var pc = config.pathConfig;
        Set<Block> allowedStart = PathToolHelper.resolveBlockList(pc.allowedStartingBlocks);
        Set<Block> allowedEnd = PathToolHelper.resolveBlockList(pc.allowedEndingBlocks);

        boolean hasStart = PathToolHelper.hasStart(stack);

        if (player.isSneaking() && PathUndoManager.hasUndo(player)) {
            PathUndoEntry entry = PathUndoManager.pop(player);
            undoEntry(world, entry);
            player.sendMessage(Text.literal("Undid last path"), true);
            cir.setReturnValue(ActionResult.SUCCESS);
            return;
        }


        // FIRST CLICK (set start): requires sneaking, no existing start, allowed starting block
        if (!hasStart && player.isSneaking()) {
            if (!allowedStart.contains(state.getBlock())) {
                return; // Let vanilla shovel handle it
            }

            PathToolHelper.setStart(stack, pos);
            // Optional: feedback
             player.sendMessage(Text.literal("Path start set: " + pos.toShortString()), true);
            cir.setReturnValue(ActionResult.SUCCESS);
            return;
        }

        // If there's no stored start yet and this is not the "first click" case, let vanilla behavior happen
        if (!hasStart) {
            return;
        }

        // SECOND CLICK: build path from stored start to this pos
        BlockPos start = PathToolHelper.getStart(stack);

        // The block we right-click on must be a valid "ending" block
        if (!allowedEnd.contains(state.getBlock())) {
            // You might want to clear the start here or keep it; I'll keep it so the player can try again.
            return;
        }

        PathBuilder.buildPath(world, player, stack, start, pos, config);

        cir.setReturnValue(ActionResult.SUCCESS);
    }
    @Unique
    private void undoEntry(World world, PathUndoEntry entry) {
        for (PathUndoEntry.BlockChange change : entry.changes) {
            world.setBlockState(change.pos, change.before, Block.NOTIFY_ALL);
        }
    }

}

