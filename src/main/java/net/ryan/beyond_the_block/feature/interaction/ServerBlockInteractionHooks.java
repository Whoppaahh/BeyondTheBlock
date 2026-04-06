package net.ryan.beyond_the_block.feature.interaction;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.feature.interaction.handlers.*;

public final class ServerBlockInteractionHooks {

    private ServerBlockInteractionHooks() {
    }

    public static ActionResult handleInteractBlock(ServerPlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        ActionResult fertility = FertilityInteractionHandler.handle(player, world, hand, hitResult);
        if (fertility != ActionResult.PASS) {
            return fertility;
        }

        ActionResult frozenMomentum = FrozenMomentumInteractionHandler.handle(player, world);
        if (frozenMomentum != ActionResult.PASS) {
            return frozenMomentum;
        }

        return ActionResult.PASS;
    }

    public static BreakContext createBreakContext(ServerPlayerEntity player, ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        BreakContext context = new BreakContext(state, blockEntity, pos, player.getMainHandStack().copy());

        BreakSuppressionHandler.configureSuppression(player, world, context);
        return context;
    }

    public static void handleAfterBreak(ServerPlayerEntity player, ServerWorld world, BreakContext context) {
        if (context == null) {
            return;
        }

        ShadowMiningHandler.handle(player, world, context);
        DarkDigHandler.handle(player, world, context);
        CropDropHandler.handle(player, world, context);
    }

    public static double getAdjustedBreakDistanceSquared(ServerPlayerEntity player, double vanillaSquaredDistance) {
        return ReachDistanceHandler.getAdjustedBreakDistanceSquared(player, vanillaSquaredDistance);
    }
}