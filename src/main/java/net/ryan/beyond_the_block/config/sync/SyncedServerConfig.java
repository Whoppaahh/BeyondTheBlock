package net.ryan.beyond_the_block.config.sync;

import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class SyncedServerConfig {
    public static final int PROTOCOL_VERSION = 1;

    // Horses
    public final boolean horseSwimmingEnabled;
    public final boolean undeadHorseSwimmingEnabled;
    public final boolean horsePreventWandering;
    public final double horseStayRadius;
    public final boolean horseRemoveMiningPenalty;
    public final boolean horseIncreaseStepHeight;

    // Paths
    public final boolean pathsEnabled;
    public final int pathsMaxDistance;
    public final boolean pathsUseTerrainFollowing;
    public final boolean pathsPreserveDurability;
    public final String pathsDefaultPathBlockId;
    public final List<String> pathsAllowedStartingBlocks;
    public final List<String> pathsAllowedEndingBlocks;
    public final int pathsMinWidth;
    public final int pathsMaxWidth;

    public SyncedServerConfig(
            boolean horseSwimmingEnabled,
            boolean undeadHorseSwimmingEnabled,
            boolean horsePreventWandering,
            double horseStayRadius,
            boolean horseRemoveMiningPenalty,
            boolean horseIncreaseStepHeight,
            boolean pathsEnabled,
            int pathsMaxDistance,
            boolean pathsUseTerrainFollowing,
            boolean pathsPreserveDurability,
            String pathsDefaultPathBlockId,
            List<String> pathsAllowedStartingBlocks,
            List<String> pathsAllowedEndingBlocks,
            int pathsMinWidth,
            int pathsMaxWidth
    ) {
        this.horseSwimmingEnabled = horseSwimmingEnabled;
        this.undeadHorseSwimmingEnabled = undeadHorseSwimmingEnabled;
        this.horsePreventWandering = horsePreventWandering;
        this.horseStayRadius = horseStayRadius;
        this.horseRemoveMiningPenalty = horseRemoveMiningPenalty;
        this.horseIncreaseStepHeight = horseIncreaseStepHeight;

        this.pathsEnabled = pathsEnabled;
        this.pathsMaxDistance = pathsMaxDistance;
        this.pathsUseTerrainFollowing = pathsUseTerrainFollowing;
        this.pathsPreserveDurability = pathsPreserveDurability;
        this.pathsDefaultPathBlockId = pathsDefaultPathBlockId;
        this.pathsAllowedStartingBlocks = List.copyOf(pathsAllowedStartingBlocks);
        this.pathsAllowedEndingBlocks = List.copyOf(pathsAllowedEndingBlocks);
        this.pathsMinWidth = pathsMinWidth;
        this.pathsMaxWidth = pathsMaxWidth;
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(PROTOCOL_VERSION);

        // Horses
        buf.writeBoolean(horseSwimmingEnabled);
        buf.writeBoolean(undeadHorseSwimmingEnabled);
        buf.writeBoolean(horsePreventWandering);
        buf.writeDouble(horseStayRadius);
        buf.writeBoolean(horseRemoveMiningPenalty);
        buf.writeBoolean(horseIncreaseStepHeight);

        // Paths
        buf.writeBoolean(pathsEnabled);
        buf.writeInt(pathsMaxDistance);
        buf.writeBoolean(pathsUseTerrainFollowing);
        buf.writeBoolean(pathsPreserveDurability);
        buf.writeString(pathsDefaultPathBlockId);

        buf.writeInt(pathsAllowedStartingBlocks.size());
        for (String value : pathsAllowedStartingBlocks) {
            buf.writeString(value);
        }

        buf.writeInt(pathsAllowedEndingBlocks.size());
        for (String value : pathsAllowedEndingBlocks) {
            buf.writeString(value);
        }

        buf.writeInt(pathsMinWidth);
        buf.writeInt(pathsMaxWidth);
    }

    public static SyncedServerConfig read(PacketByteBuf buf) {
        int version = buf.readInt();
        if (version != PROTOCOL_VERSION) {
            throw new IllegalStateException(
                    "Unsupported synced server config version: " + version + ", expected " + PROTOCOL_VERSION
            );
        }

        boolean horseSwimmingEnabled = buf.readBoolean();
        boolean undeadHorseSwimmingEnabled = buf.readBoolean();
        boolean horsePreventWandering = buf.readBoolean();
        double horseStayRadius = buf.readDouble();
        boolean horseRemoveMiningPenalty = buf.readBoolean();
        boolean horseIncreaseStepHeight = buf.readBoolean();

        boolean pathsEnabled = buf.readBoolean();
        int pathsMaxDistance = buf.readInt();
        boolean pathsUseTerrainFollowing = buf.readBoolean();
        boolean pathsPreserveDurability = buf.readBoolean();
        String pathsDefaultPathBlockId = buf.readString();

        int startingSize = buf.readInt();
        List<String> pathsAllowedStartingBlocks = new ArrayList<>(startingSize);
        for (int i = 0; i < startingSize; i++) {
            pathsAllowedStartingBlocks.add(buf.readString());
        }

        int endingSize = buf.readInt();
        List<String> pathsAllowedEndingBlocks = new ArrayList<>(endingSize);
        for (int i = 0; i < endingSize; i++) {
            pathsAllowedEndingBlocks.add(buf.readString());
        }

        int pathsMinWidth = buf.readInt();
        int pathsMaxWidth = buf.readInt();

        return new SyncedServerConfig(
                horseSwimmingEnabled,
                undeadHorseSwimmingEnabled,
                horsePreventWandering,
                horseStayRadius,
                horseRemoveMiningPenalty,
                horseIncreaseStepHeight,
                pathsEnabled,
                pathsMaxDistance,
                pathsUseTerrainFollowing,
                pathsPreserveDurability,
                pathsDefaultPathBlockId,
                pathsAllowedStartingBlocks,
                pathsAllowedEndingBlocks,
                pathsMinWidth,
                pathsMaxWidth
        );
    }

    public static SyncedServerConfig defaults() {
        return new SyncedServerConfig(
                true,
                false,
                true,
                10.0,
                true,
                true,
                true,
                64,
                true,
                false,
                "minecraft:dirt_path",
                List.of("minecraft:grass_block", "minecraft:stone", "minecraft:dirt"),
                List.of("minecraft:grass_block", "minecraft:stone"),
                1,
                7
        );
    }
}
