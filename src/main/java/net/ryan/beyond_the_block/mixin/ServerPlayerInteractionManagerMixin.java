package net.ryan.beyond_the_block.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.feature.interaction.BreakContext;
import net.ryan.beyond_the_block.feature.interaction.ServerBlockInteractionHooks;
import net.ryan.beyond_the_block.mixin.accessors.ServerPlayerInteractionManagerAccessor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    @Shadow
    @Final
    protected ServerPlayerEntity player;

    @Unique
    private BreakContext beyond_the_block$breakContext;

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$interactBlock(
            ServerPlayerEntity player,
            World world,
            ItemStack item,
            Hand hand,
            BlockHitResult hitResult,
            CallbackInfoReturnable<ActionResult> cir
    ) {
        ActionResult result = ServerBlockInteractionHooks.handleInteractBlock(player, world, hand, hitResult);
        if (result != ActionResult.PASS) {
            cir.setReturnValue(result);
        }
    }

    @Inject(method = "tryBreakBlock", at = @At("HEAD"))
    private void beyond_the_block$cacheBlockData(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntity player = ((ServerPlayerInteractionManagerAccessor) this).getPlayer();
        ServerWorld world = player.getWorld();
        this.beyond_the_block$breakContext = ServerBlockInteractionHooks.createBreakContext(player, world, pos);
    }

    @Inject(method = "tryBreakBlock", at = @At("RETURN"))
    private void beyond_the_block$afterBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) {
            return;
        }

        ServerPlayerEntity player = ((ServerPlayerInteractionManagerAccessor) this).getPlayer();
        ServerWorld world = player.getWorld();
        ServerBlockInteractionHooks.handleAfterBreak(player, world, this.beyond_the_block$breakContext);
    }

    @Redirect(
            method = "tryBreakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V"
            )
    )
    private void beyond_the_block$conditionallySkipAfterBreak(
            Block block,
            World world,
            PlayerEntity player,
            BlockPos pos,
            BlockState state,
            BlockEntity blockEntity,
            ItemStack stack
    ) {
        if (this.beyond_the_block$breakContext != null && this.beyond_the_block$breakContext.isSuppressVanillaDrops()) {
            return;
        }

        block.afterBreak(world, player, pos, state, blockEntity, stack);
    }

    @Inject(method = "tryBreakBlock", at = @At("RETURN"))
    private void beyond_the_block$clearBreakContext(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        this.beyond_the_block$breakContext = null;
    }

    @Redirect(
            method = "processBlockBreakingAction",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private double beyond_the_block$increaseBreakDistanceLimit() {
        double vanillaSq = ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE;
        ServerPlayerEntity player = ((ServerPlayerInteractionManagerAccessor) this).getPlayer();
        return ServerBlockInteractionHooks.getAdjustedBreakDistanceSquared(player, vanillaSq);
    }
}