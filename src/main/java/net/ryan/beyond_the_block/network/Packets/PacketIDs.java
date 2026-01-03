package net.ryan.beyond_the_block.network.Packets;

import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.BeyondTheBlock;

public class PacketIDs {
    public static final Identifier SYNC_RESTORE_PACKET_ID = new Identifier(BeyondTheBlock.MOD_ID, "sync_restore");

    public static final Identifier SYNC_PATH_WIDTH_PACKET_ID = new Identifier(BeyondTheBlock.MOD_ID, "sync_path_width");
    public static final Identifier SYNC_INVENTORY_PACKET_ID = new Identifier(BeyondTheBlock.MOD_ID, "sync_inventory");
    public static final Identifier SYNC_BREEDING_PACKET_ID = new Identifier(BeyondTheBlock.MOD_ID, "sync_breeding");

    public static final Identifier GUARD_FOLLOW_PACKET_ID = new Identifier(BeyondTheBlock.MOD_ID, "guard_follow");
    public static final Identifier GUARD_PATROL_PACKET_ID = new Identifier(BeyondTheBlock.MOD_ID, "guard_patrol");
    public static final Identifier GUARD_STATS_SYNC_PACKET_ID = new Identifier(BeyondTheBlock.MOD_ID, "guard_stats");

    public static final Identifier TELEPORT_WITH_STAFF_ID = new Identifier(BeyondTheBlock.MOD_ID, "teleport_with_staff");

    public static final Identifier SYNC_RIDDLES_ID = new Identifier(BeyondTheBlock.MOD_ID, "sync_riddles");
    public static final Identifier RIDDLE_TIME_SYNC_PACKET_ID = new Identifier(BeyondTheBlock.MOD_ID, "riddle_time_sync");

    public static final Identifier LEAP_OF_FAITH_PACKET_ID = new Identifier(BeyondTheBlock.MOD_ID, "leap_of_faith");


}
