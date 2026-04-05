package net.ryan.beyond_the_block.content.riddles;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.network.sync.riddles.RiddleTimeSync;

import java.util.*;

public class RiddleDataManager extends PersistentState {
    public static final String ID = "shrine_riddle_data";

    private long syncedTimeOfDay = 0L;
    private long lastGenerationTime = -1L;
    private long lastUpdatedWorldTime = 0L;

    private BlockPos shrinePos = null;

    private final Map<UUID, Riddle> activeRiddles = new HashMap<>();
    private final Map<UUID, Set<UUID>> completedRiddles = new HashMap<>();
    private final Map<UUID, Set<String>> seenSignaturesByPlayer = new HashMap<>();

    private final RiddleComponents components;

    public RiddleDataManager(RiddleComponents components) {
        this.components = components;
    }

    public static RiddleDataManager get(ServerWorld world, RiddleComponents components) {
        return world.getPersistentStateManager().getOrCreate(
                nbt -> createFromNbt(nbt, components),
                () -> new RiddleDataManager(components),
                ID
        );
    }

    public static RiddleDataManager createFromNbt(NbtCompound nbt, RiddleComponents components) {
        RiddleDataManager manager = new RiddleDataManager(components);

        if (nbt.contains("ShrinePos")) {
            manager.shrinePos = BlockPos.fromLong(nbt.getLong("ShrinePos"));
        }

        if (nbt.contains("SyncedTimeOfDay")) {
            manager.syncedTimeOfDay = nbt.getLong("SyncedTimeOfDay");
        }

        if (nbt.contains("LastGenerationTime")) {
            manager.lastGenerationTime = nbt.getLong("LastGenerationTime");
        }

        if (nbt.contains("LastUpdatedWorldTime")) {
            manager.lastUpdatedWorldTime = nbt.getLong("LastUpdatedWorldTime");
        }

        // Seen signatures
        NbtCompound seenTag = nbt.getCompound("SeenSignatures");
        for (String playerIdStr : seenTag.getKeys()) {
            UUID playerId = UUID.fromString(playerIdStr);
            NbtList list = seenTag.getList(playerIdStr, NbtElement.STRING_TYPE);

            Set<String> signatures = new HashSet<>();
            for (NbtElement element : list) {
                signatures.add(element.asString());
            }

            manager.seenSignaturesByPlayer.put(playerId, signatures);
        }

        // Completed riddles
        NbtCompound completedTag = nbt.getCompound("CompletedRiddles");
        for (String playerIdStr : completedTag.getKeys()) {
            UUID playerId = UUID.fromString(playerIdStr);
            NbtList list = completedTag.getList(playerIdStr, NbtElement.STRING_TYPE);

            Set<UUID> riddleIds = new HashSet<>();
            for (NbtElement element : list) {
                riddleIds.add(UUID.fromString(element.asString()));
            }

            manager.completedRiddles.put(playerId, riddleIds);
        }

        // Active riddles
        NbtCompound activeTag = nbt.getCompound("ActiveRiddles");
        for (String playerIdStr : activeTag.getKeys()) {
            UUID playerId = UUID.fromString(playerIdStr);
            NbtCompound playerActiveTag = activeTag.getCompound(playerIdStr);

            for (String riddleIdStr : playerActiveTag.getKeys()) {
                NbtCompound riddleTag = playerActiveTag.getCompound(riddleIdStr);
                Riddle riddle = Riddle.fromNbt(riddleTag);
                manager.activeRiddles.put(playerId, riddle);
            }
        }

        return manager;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        if (shrinePos != null) {
            nbt.putLong("ShrinePos", shrinePos.asLong());
        }

        nbt.putLong("SyncedTimeOfDay", syncedTimeOfDay);
        nbt.putLong("LastGenerationTime", lastGenerationTime);
        nbt.putLong("LastUpdatedWorldTime", lastUpdatedWorldTime);

        // Seen signatures
        NbtCompound seenTag = new NbtCompound();
        for (Map.Entry<UUID, Set<String>> entry : seenSignaturesByPlayer.entrySet()) {
            NbtList list = new NbtList();
            for (String signature : entry.getValue()) {
                list.add(NbtString.of(signature));
            }
            seenTag.put(entry.getKey().toString(), list);
        }
        nbt.put("SeenSignatures", seenTag);

        // Completed riddles
        NbtCompound completedTag = new NbtCompound();
        for (Map.Entry<UUID, Set<UUID>> entry : completedRiddles.entrySet()) {
            NbtList list = new NbtList();
            for (UUID riddleId : entry.getValue()) {
                list.add(NbtString.of(riddleId.toString()));
            }
            completedTag.put(entry.getKey().toString(), list);
        }
        nbt.put("CompletedRiddles", completedTag);

        // Active riddles
        NbtCompound activeTag = new NbtCompound();
        for (Map.Entry<UUID, Riddle> entry : activeRiddles.entrySet()) {
            UUID playerId = entry.getKey();
            Riddle riddle = entry.getValue();

            NbtCompound playerActiveTag = activeTag.contains(playerId.toString())
                    ? activeTag.getCompound(playerId.toString())
                    : new NbtCompound();

            playerActiveTag.put(riddle.id().toString(), riddle.toNbt());
            activeTag.put(playerId.toString(), playerActiveTag);
        }
        nbt.put("ActiveRiddles", activeTag);

        return nbt;
    }

    public void updateTime(long timeOfDay) {
        this.syncedTimeOfDay = timeOfDay;
        markDirty();
    }

    public void setShrinePos(BlockPos pos) {
        this.shrinePos = pos;
        markDirty();
    }

    public BlockPos getShrinePos() {
        return shrinePos;
    }

    public long getEstimatedTimeOfDay(ServerWorld world) {
        return world.getTimeOfDay() % Configs.server().features.shrines.generationInterval;
    }

    public long getSecondsUntilNextDay(ServerWorld world) {
        long ticksRemaining = Configs.server().features.shrines.generationInterval - getEstimatedTimeOfDay(world);
        return ticksRemaining / 20L;
    }

    public void tick(ServerWorld world) {
        long timeOfDay = world.getTimeOfDay() % Configs.server().features.shrines.generationInterval;

        if (timeOfDay < 100 && lastGenerationTime != world.getTimeOfDay() && shrinePos != null) {
            lastGenerationTime = world.getTimeOfDay();
            generateDailyRiddle(world);
        }

        if (world.getTime() % 200 == 0) {
            long syncedTime = world.getTimeOfDay() % Configs.server().features.shrines.generationInterval;
            syncedTimeOfDay = syncedTime;

            for (ServerPlayerEntity player : world.getPlayers()) {
                RiddleTimeSync.syncRiddleTime(player, syncedTime);
            }
        }
    }

    private void generateDailyRiddle(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers();
        if (players.isEmpty()) {
            return;
        }

        final int MAX_RIDDLES = 4;
        int assignedRiddles = 0;

        for (ServerPlayerEntity player : players) {
            UUID playerId = player.getUuid();

            if (activeRiddles.containsKey(playerId)) {
                continue;
            }

            Set<String> seenSignatures = seenSignaturesByPlayer.computeIfAbsent(playerId, id -> new HashSet<>());
            Optional<Riddle> maybeRiddle = components.createRandomRiddle(seenSignatures);

            if (maybeRiddle.isEmpty()) {
                continue;
            }

            activeRiddles.put(playerId, maybeRiddle.get());
            lastUpdatedWorldTime = world.getTimeOfDay();
            markDirty();
            assignedRiddles++;

            if (assignedRiddles >= MAX_RIDDLES) {
                break;
            }
        }

        if (shrinePos != null) {
            world.playSound(null, shrinePos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1.0f, 1.0f);
        }

        markDirty();
    }

    public long getLastUpdatedWorldTime() {
        return lastUpdatedWorldTime;
    }

    public Riddle getRiddle(UUID playerId) {
        return activeRiddles.get(playerId);
    }

    public Map<UUID, Riddle> getActiveRiddles() {
        return Collections.unmodifiableMap(activeRiddles);
    }

    public Map<UUID, Set<UUID>> getCompletedRiddles() {
        Map<UUID, Set<UUID>> copy = new HashMap<>();
        for (Map.Entry<UUID, Set<UUID>> entry : completedRiddles.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
        }
        return Collections.unmodifiableMap(copy);
    }

    public boolean hasCompleted(UUID playerId, UUID riddleId) {
        return completedRiddles.getOrDefault(playerId, Collections.emptySet()).contains(riddleId);
    }

    public void markCompleted(UUID playerId, UUID riddleId) {
        completedRiddles.computeIfAbsent(playerId, id -> new HashSet<>()).add(riddleId);
        activeRiddles.remove(playerId);
        markDirty();
    }

    public void clearActiveRiddle(UUID playerId) {
        if (activeRiddles.remove(playerId) != null) {
            markDirty();
        }
    }
}