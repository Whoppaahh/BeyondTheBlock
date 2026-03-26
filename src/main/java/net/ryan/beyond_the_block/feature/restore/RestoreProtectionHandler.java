package net.ryan.beyond_the_block.feature.restore;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.utils.Helpers.RestoreManager;

public class RestoreProtectionHandler {
    public static TypedActionResult<ItemStack> onItemUsed(
            PlayerEntity player,
            World world,
            Hand hand
    ) {
        ItemStack stack = player.getStackInHand(hand);

        // Only care about block items
        if (!(stack.getItem() instanceof BlockItem)) {
            return TypedActionResult.pass(stack);
        }

        // Raycast MUST run on both sides
        BlockHitResult hit = (BlockHitResult) player.raycast(5.0D, 0.0F, false);
        if (hit == null) {
            return TypedActionResult.pass(stack);
        }

        BlockPos placePos = hit.getBlockPos().offset(hit.getSide());

        // CLIENT-SIDE BLOCK (prevents prediction)
        if (world.isClient) {
            if (world instanceof ClientWorld clientWorld
                    && RestoreManager.isProtectedClient(placePos)) {
                return TypedActionResult.fail(stack);
            }
            return TypedActionResult.pass(stack);
        }

        // SERVER-SIDE BLOCK (authoritative)
        if (world instanceof ServerWorld serverWorld) {
            if (RestoreManager.isProtected(serverWorld, placePos)) {
                player.sendMessage(
                        Text.literal("This area is restoring.")
                                .formatted(Formatting.GRAY),
                        true
                );
                return TypedActionResult.fail(stack);
            }
        }

        return TypedActionResult.pass(stack);
    }

}
