package net.ryan.beyond_the_block.client.util;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.ryan.beyond_the_block.content.item.AnimatedItem;
import org.lwjgl.glfw.GLFW;

import static net.ryan.beyond_the_block.network.packets.PacketIDs.TELEPORT_WITH_STAFF_ID;

public class AnimatedItemClientHandler {

    private static boolean wasPressedLastTick = false;

    public static void handleLeftClick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (client.currentScreen != null) return; // Don't trigger in GUI

        long window = client.getWindow().getHandle();
        boolean isPressed = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS;

        // Detect fresh press, not hold
        if (isPressed && !wasPressedLastTick) {
            ItemStack heldStack = client.player.getMainHandStack();

            if (heldStack.getItem() instanceof AnimatedItem) {

                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                ClientPlayNetworking.send(TELEPORT_WITH_STAFF_ID, buf);
            }
        }
        wasPressedLastTick = isPressed;
    }
}
