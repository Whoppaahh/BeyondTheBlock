package net.ryan.beyond_the_block.utils.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

public class GlowPacketHelper {

    /**
     * Sends a glowing state update to a single player for a target entity.
     * This does not affect the actual glowing status on the server — it's purely client-side.
     *
     * @param player  The player who should see (or stop seeing) the glow
     * @param target  The entity to glow
     * @param glowing Whether the entity should glow
     */
    public static void setGlowing(ServerPlayerEntity player, Entity target, boolean glowing) {
        // Temporarily set the tracked glowing value
        DataTracker dataTracker = target.getDataTracker();

        boolean currentlyGlowing = target.isGlowing();
        target.setGlowing(glowing);

        // Send a packet that makes the entity glow or stop glowing for that player
        EntityTrackerUpdateS2CPacket packet = new EntityTrackerUpdateS2CPacket(target.getId(), dataTracker, true);
        player.networkHandler.sendPacket(packet);

        // Restore original glowing state
        target.setGlowing(currentlyGlowing);
    }
}
