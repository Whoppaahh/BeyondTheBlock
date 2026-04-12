package net.ryan.beyond_the_block.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SmithingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.screen.handler.ArmourTrimSmithingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingTableBlock.class)
public class SmithingTableBlockMixin {

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void openTrimSmithing(
            BlockState state,
            World world,
            BlockPos pos,
            PlayerEntity player,
            Hand hand,
            BlockHitResult hit,
            CallbackInfoReturnable<ActionResult> cir
    ) {
        if (!world.isClient) {
            NamedScreenHandlerFactory factory = new SimpleNamedScreenHandlerFactory(
                    (syncId, inventory, p) -> new ArmourTrimSmithingScreenHandler(syncId, inventory),
                    Text.translatable("container.beyond_the_block.armor_trim")
            );

            player.openHandledScreen(factory);
        }

        cir.setReturnValue(ActionResult.SUCCESS);
    }
}
