package net.ryan.beyond_the_block.riddles;

import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.PersistentState;
import net.ryan.beyond_the_block.config.Configs;
import net.ryan.beyond_the_block.network.ServerNetworking;

import java.util.*;

public class RiddleDataManager extends PersistentState {
    public static final String ID = "shrine_riddle_data";

    private static long syncedTimeOfDay = 0;
    private BlockPos shrinePos = null;
    private static final Map<UUID, Riddle> activeRiddles = new HashMap<>();
    private static final Map<UUID, Set<UUID>> completedRiddles = new HashMap<>();
    private final RiddleComponents components;
    public static long lastGenerationTime = -1;
    private static long lastUpdated = System.currentTimeMillis();
    // A map to keep track of player UUIDs and the set of riddle signatures they've seen
    public static final Map<UUID, Map<UUID, String>> playerRiddlesMap = new HashMap<>();


    public RiddleDataManager(RiddleComponents components) {
        this.components = components;
    }

    public static void updateTime(long timeOfDay){
        syncedTimeOfDay = timeOfDay;
    }
    public static RiddleDataManager get(ServerWorld world, RiddleComponents components) {
        return world.getPersistentStateManager().getOrCreate(
                nbt -> createFromNbt(nbt, components),
                () -> new RiddleDataManager(components),
                ID
        );
    }

    public static RiddleDataManager createFromNbt(NbtCompound nbt, RiddleComponents components) {
        RiddleDataManager handler = new RiddleDataManager(components);

        if (nbt.contains("ShrinePos")) {
            handler.shrinePos = BlockPos.fromLong(nbt.getLong("ShrinePos"));
        }
        playerRiddlesMap.clear();
        completedRiddles.clear();
        activeRiddles.clear();

        // --- PlayerRiddles ---
        NbtCompound playerRiddlesTag = nbt.getCompound("PlayerRiddles");
        for (String playerIdStr : playerRiddlesTag.getKeys()) {
            UUID playerId = UUID.fromString(playerIdStr);
            NbtCompound riddlesCompound = playerRiddlesTag.getCompound(playerIdStr);

            Map<UUID, String> riddles = new HashMap<>();
            for (String riddleIDString : riddlesCompound.getKeys()) {
                UUID riddleID = UUID.fromString(riddleIDString);
                String signature = riddlesCompound.getString(riddleIDString);
                riddles.put(riddleID, signature);
            }

            playerRiddlesMap.put(playerId, riddles);
        }

        // --- CompletedRiddles ---
        NbtCompound completedTag = nbt.getCompound("CompletedRiddles");
        for (String playerIdStr : completedTag.getKeys()) {
            UUID playerId = UUID.fromString(playerIdStr);
            NbtList list = completedTag.getList(playerIdStr, NbtElement.STRING_TYPE);
            Set<UUID> riddleIds = new HashSet<>();
            for (NbtElement element : list) {
                riddleIds.add(UUID.fromString(element.asString()));
            }
            completedRiddles.put(playerId, riddleIds);
        }

        // --- ActiveRiddles ---
        NbtCompound activeTag = nbt.getCompound("ActiveRiddles");
        for (String playerIdStr : activeTag.getKeys()) {
            UUID playerId = UUID.fromString(playerIdStr);
            NbtCompound playerRiddles = activeTag.getCompound(playerIdStr);

            for (String riddleIdStr : playerRiddles.getKeys()) {
                UUID riddleId = UUID.fromString(riddleIdStr);
                NbtCompound riddleTag = playerRiddles.getCompound(riddleIdStr);

                List<String> pages = riddleTag.getList("Pages", NbtElement.STRING_TYPE)
                        .stream().map(NbtElement::asString).toList();

                List<String> itemIds = riddleTag.getList("Items", NbtElement.STRING_TYPE)
                        .stream().map(NbtElement::asString).toList();

                Riddle riddle = Riddle.fromStored(riddleId, pages, itemIds);

                activeRiddles.put(playerId, riddle);
            }
        }
        return handler;
    }


    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        if (shrinePos != null) {
            nbt.putLong("ShrinePos", shrinePos.asLong());
        }

        // --- PlayerRiddles ---
        NbtCompound playerRiddlesTag = new NbtCompound();
        for (Map.Entry<UUID, Map<UUID, String>> entry : playerRiddlesMap.entrySet()) {
            UUID playerID = entry.getKey();
            Map<UUID, String> riddles = entry.getValue();

            NbtCompound riddlesCompound = new NbtCompound();

            for (Map.Entry<UUID, String> riddleEntry : riddles.entrySet()) {
                riddlesCompound.putString(riddleEntry.getKey().toString(), riddleEntry.getValue());
            }


            playerRiddlesTag.put(playerID.toString(), riddlesCompound);
        }
        nbt.put("PlayerRiddles", playerRiddlesTag);

        // --- CompletedRiddles ---
        NbtCompound completedTag = new NbtCompound();
        for (Map.Entry<UUID, Set<UUID>> entry : completedRiddles.entrySet()) {
            NbtList completedList = new NbtList();
            for (UUID riddleId : entry.getValue()) {
                completedList.add(NbtString.of(riddleId.toString()));
            }
            completedTag.put(entry.getKey().toString(), completedList);
        }
        nbt.put("CompletedRiddles", completedTag);

        // --- ActiveRiddles ---
        NbtCompound activeTag = new NbtCompound();
        for (Map.Entry<UUID, Riddle> entry : activeRiddles.entrySet()) {
            UUID playerId = entry.getKey();
            Riddle riddle = entry.getValue();

            NbtCompound playerActiveRiddlesTag = activeTag.getCompound(playerId.toString()); // Get the player section or create one if it doesn't exist

            NbtCompound riddleTag = new NbtCompound();

            NbtList pages = new NbtList();
            for (String page : riddle.getPages()) {
                pages.add(NbtString.of(page));
            }
            riddleTag.put("Pages", pages);

            NbtList items = new NbtList();
            for (Item item : riddle.getRequiredItems()) {
                items.add(NbtString.of(Registry.ITEM.getId(item).toString()));
            }
            riddleTag.put("Items", items);

            // Add the riddle under the player's key
            playerActiveRiddlesTag.put(riddle.getId().toString(), riddleTag);

            // Now update the activeTag with the player section
            activeTag.put(playerId.toString(), playerActiveRiddlesTag);
        }

        nbt.put("ActiveRiddles", activeTag);
        return nbt;
    }


    public void setShrinePos(BlockPos pos) {
        this.shrinePos = pos;
        this.markDirty();
    }

    public static long getEstimatedTimeOfDay() {
        long elapsed = (System.currentTimeMillis() - lastUpdated) / 50; // convert ms to ticks
        return (syncedTimeOfDay + elapsed) % Configs.server().features.shrines.generationInterval;
    }

    public static long getSecondsUntilNextDay() {
        long ticksRemaining = Configs.server().features.shrines.generationInterval - getEstimatedTimeOfDay();
        return ticksRemaining / 20;
    }

    public void tick(ServerWorld world) {
        long timeOfDay = world.getTimeOfDay() % Configs.server().features.shrines.generationInterval; //24000 for full day

        if (timeOfDay < 100 && lastGenerationTime != world.getTimeOfDay() && shrinePos != null) {
            lastGenerationTime = world.getTimeOfDay();
            generateDailyRiddle(world);
        }
        if (world.getTime() % 200 == 0) { // Every 10 seconds
            long time = world.getTimeOfDay() % Configs.server().features.shrines.generationInterval;

            for (ServerPlayerEntity player : world.getPlayers()) {
                ServerNetworking.syncRiddleTime(player, time);
            }
        }

    }

    private void generateDailyRiddle(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers();
        if (players.isEmpty()) return;

        final int MAX_RIDDLES = 4;
        int assignedRiddles = 0; //Counter to track the number of players assigned riddles

        for (ServerPlayerEntity player : players) {
            UUID persistentId = player.getUuid();
            if (!activeRiddles.containsKey(persistentId)) {
                Riddle riddle = components.createRandomRiddle(persistentId);
                activeRiddles.put(persistentId, riddle);
                lastUpdated = System.currentTimeMillis();
                markDirty();
                assignedRiddles++;
            }
            //Stop assigning riddles if 4 players have been assigned one
            if (assignedRiddles >= MAX_RIDDLES) {
                break;
            }
        }

        if (shrinePos != null) {
            world.playSound(null, shrinePos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1.0f, 1.0f);
        }
        this.markDirty();
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public Riddle getRiddle(UUID playerId) {
        return activeRiddles.get(playerId);
    }

    public Map<UUID, Riddle> getActiveRiddles() {
        return Collections.unmodifiableMap(activeRiddles);
    }

    public Map<UUID, Set<UUID>> getCompletedRiddles() {
        return Collections.unmodifiableMap(completedRiddles);
    }

    public void markCompleted(UUID playerId, UUID riddleId) {
        completedRiddles.computeIfAbsent(UUID.fromString(playerId.toString()), id -> new HashSet<>()).add(UUID.fromString(riddleId.toString()));
        activeRiddles.remove(playerId);
        this.markDirty();
    }

    public BlockPos getShrinePos() {
        return shrinePos;
    }
}
