package net.ryan.beyond_the_block.mixin.block;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.block.HangingSignBlock;
import net.ryan.beyond_the_block.content.block.WallHangingSignBlock;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSignBlock.class)
public abstract class SignBlockMixin {

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void btb$reopenVanillaSignEditor(BlockState state, World world, BlockPos pos,
                                             PlayerEntity player, Hand hand, BlockHitResult hit,
                                             CallbackInfoReturnable<ActionResult> cir) {
        Identifier id = Registry.BLOCK.getId(state.getBlock());

        if (!(state.getBlock() instanceof AbstractSignBlock)) {
            return;
        }

        if (state.getBlock() instanceof HangingSignBlock ||
                state.getBlock() instanceof WallHangingSignBlock) {
            return;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof SignBlockEntity signBlockEntity)) {
            return;
        }

        ItemStack stack = player.getStackInHand(hand);

        // Let vanilla dye / ink / glow ink behavior happen
        if (stack.getItem() instanceof DyeItem
                || stack.isOf(Items.INK_SAC)
                || stack.isOf(Items.GLOW_INK_SAC)) {
            return;
        }

        if (!world.isClient) {
            signBlockEntity.setEditor(player.getUuid());
            player.openEditSignScreen(signBlockEntity);
        }

        cir.setReturnValue(ActionResult.success(world.isClient));
    }
}