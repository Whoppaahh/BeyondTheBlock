package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.feature.paths.PathToolHelper;
import net.ryan.beyond_the_block.network.ClientNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseScrollMixin {

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void btb_onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        // Ignore no-scroll
        if (vertical == 0) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.world == null) return;
        // Do not interfere with scrolling in menus/screens
        if (client.currentScreen != null) return;

        ItemStack stack = client.player.getMainHandStack();

        // Only affect shovels
        if (!(stack.getItem() instanceof ShovelItem)) return;

        // Only modify path width while sneaking
        // Otherwise let vanilla hotbar scrolling behave normally
        if (!client.player.isSneaking()) return;
        int current = PathToolHelper.getWidth(stack, Configs.server());

        // Scroll up increases, scroll down decreases
        int newWidth = vertical > 0 ? current + 1 : current - 1;

        // Clamp width to config limits
        newWidth = Math.max(Configs.server().features.paths.minWidth, Math.min(Configs.server().features.paths.maxWidth, newWidth));

        // Only update if changed
        if (newWidth != current) {
            PathToolHelper.setWidth(stack, newWidth);
            ClientNetworking.sendWidthUpdate(newWidth);


            if (Configs.client().hud.paths.showWidthHud) {
                client.player.sendMessage(
                        Text.literal("Path Width = " + newWidth),
                        true
                );
            }

            // Consume the scroll event so hotbar doesn't move
            ci.cancel();
        }
    }
}
