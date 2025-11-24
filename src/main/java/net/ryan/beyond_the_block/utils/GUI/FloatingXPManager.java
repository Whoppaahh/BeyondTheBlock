package net.ryan.beyond_the_block.utils.GUI;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FloatingXPManager {
    private static final List<FloatingXPText> entries = new ArrayList<>();
    private static int queuedXP = 0;
    private static int ticksSinceLastPickup = 9999;
    private static final int COMBINE_WINDOW = 40; // same as entry lifetime
    private static boolean registered = false;
    private static int lastXP = -1;

    public static void onXPPickedUp(int xpValue) {
        queuedXP += xpValue; // just queue it
    }

    private static void checkXPChange() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        int totalXP = client.player.totalExperience;
        if (lastXP != -1 && totalXP > lastXP) {
            onXPPickedUp(totalXP - lastXP);
        }
        lastXP = totalXP;
    }

    public static void tick() {
        checkXPChange();
        ticksSinceLastPickup++;

        if (queuedXP > 0) {
            // Combine if within short time window
            if (!entries.isEmpty() && ticksSinceLastPickup < COMBINE_WINDOW) {
                entries.get(0).addXP(queuedXP);
                entries.get(0).resetAge();
            } else {
                entries.add(0, new FloatingXPText(queuedXP));
            }
            ticksSinceLastPickup = 0;
            queuedXP = 0;
        }

        // Update & clean up
        Iterator<FloatingXPText> it = entries.iterator();
        while (it.hasNext()) {
            FloatingXPText e = it.next();
            e.tick();
            if (!e.isAlive()) it.remove();
        }
    }

    public static void render(MatrixStack matrices) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden || client.player == null) return;

        int baseX = client.getWindow().getScaledWidth() / 2;
        int baseY = client.getWindow().getScaledHeight() - 60;

        int i = 0;
        for (FloatingXPText entry : entries) {
            entry.render(matrices, baseX, baseY, i++);
        }
    }

    public static void register() {
        if (registered) return;
        registered = true;

        // Tick updates
        ClientTickEvents.END_CLIENT_TICK.register(client -> tick());

        // Render to HUD each frame
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> render(matrices));
    }

}

