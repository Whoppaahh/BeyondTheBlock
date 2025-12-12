package net.ryan.beyond_the_block.mixin.Client;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.config.ModConfig;
import net.ryan.beyond_the_block.network.ClientNetworking;
import net.ryan.beyond_the_block.utils.Helpers.PathToolHelper;
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

        ItemStack stack = client.player.getMainHandStack();

        // Only affect shovels
        if (!(stack.getItem() instanceof ShovelItem)) return;

        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).get();
        var pc = config.pathConfig;

        int current = PathToolHelper.getWidth(stack, config);

        // Scroll up increases, scroll down decreases
        int newWidth = vertical > 0 ? current + 1 : current - 1;

        // Clamp width to config limits
        newWidth = Math.max(pc.minWidth, Math.min(pc.maxWidth, newWidth));

        // Only update if changed
        if (newWidth != current) {
            PathToolHelper.setWidth(stack, newWidth);
            ClientNetworking.sendWidthUpdate(newWidth);


            if (pc.showWidthHud) {
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
