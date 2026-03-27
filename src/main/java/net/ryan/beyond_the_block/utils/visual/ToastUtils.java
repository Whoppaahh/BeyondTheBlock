package net.ryan.beyond_the_block.utils.visual;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

public class ToastUtils {
    public static void showToast(String title, String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.getToastManager().add(new SystemToast(SystemToast.Type.PACK_LOAD_FAILURE,
                Text.of(title),
                Text.of(message)));
    }
}

