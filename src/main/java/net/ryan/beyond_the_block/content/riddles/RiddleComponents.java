package net.ryan.beyond_the_block.content.riddles;

import com.google.common.hash.Hashing;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.core.bootstrap.ContentRegistrar;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class RiddleComponents {
    private static final Gson GSON = new Gson();
    private final List<String> intros = new ArrayList<>();
    private final List<String> outros = new ArrayList<>();
    private final Map<Item, String> itemMetaphors = new HashMap<>();

    private final Random random = new Random();



    public static RiddleDataManager get(World world) {
        if (!(world instanceof ServerWorld serverWorld)) return null;
        return RiddleDataManager.get(serverWorld, ContentRegistrar.RIDDLE_COMPONENTS);
    }

    public void loadFromJson() {
        loadIntroStanzas();
        loadOutroStanzas();
        loadItemMetaphors();
    }

    public List<String> getIntros() {
        return intros;
    }

    public List<String> getOutros() {
        return outros;
    }

    public Map<Item, String> getItemMetaphors() {
        return itemMetaphors;
    }

    public String getRandomIntroStanza() {
        return intros.get(random.nextInt(intros.size()));
    }

    public String getRandomOutroStanza() {
        return outros.get(random.nextInt(outros.size()));
    }

    public String getItemMetaphor(Item item) {
        String metaphor = itemMetaphors.getOrDefault(item, "???");
        BeyondTheBlock.LOGGER.info("Item: {} -> Metaphor: {}", item.getName().getString(), metaphor);  // Log the item and metaphor
        return metaphor;
    }

    public List<Item> selectItemsForRiddle() {
        List<Item> items = new ArrayList<>(itemMetaphors.keySet());
        Collections.shuffle(items);
        if (items.isEmpty()) return Collections.emptyList();

        int count = 1 + random.nextInt(2); // 1 or 2
        return items.subList(0, Math.min(count, items.size()));
    }
    public static String generateSignature(Riddle riddle) {
        String content = String.join("|", riddle.pages())
                + riddle.requiredItems().stream()
                .map(item -> Registry.ITEM.getId(item).toString())
                .collect(Collectors.joining(","));
        return Hashing.sha256().hashString(content, StandardCharsets.UTF_8).toString();
    }

    // New method to create a random riddle
    public Riddle createRandomRiddle(UUID playerId) {
        Map<UUID, String> used = RiddleDataManager.playerRiddlesMap.computeIfAbsent(playerId, k -> new HashMap<>());

        Riddle riddle = null;
        String signature = "";

        int safetyCounter = 0;
        do {
            if (safetyCounter++ > 100) {
                BeyondTheBlock.LOGGER.warn("Failed to create unique riddle for player {}", playerId);
                break;
            }

            // Generate the parts of the riddle
            String intro = getRandomIntroStanza();
            String outro = getRandomOutroStanza();
            List<Item> selectedItems = selectItemsForRiddle();

            List<String> pages = new ArrayList<>();
            pages.add(intro);
            for (Item item : selectedItems) {
                pages.add(getItemMetaphor(item));
            }
            pages.add(outro);

            UUID id = UUID.randomUUID();
            riddle = new Riddle(id, pages, selectedItems);
            signature = generateSignature(riddle);

        } while (used.containsValue(signature));

        used.put(riddle.id(), signature);
        return riddle;
    }


    private void loadIntroStanzas() {
        List<String> loadedIntros = loadJsonFile("riddles/intros.json",
                new TypeToken<List<String>>() {
                }.getType());
        if (loadedIntros != null) {
            intros.addAll(loadedIntros);
        }
    }

    private void loadOutroStanzas() {
        List<String> loadedOutros = loadJsonFile("riddles/outros.json",
                new TypeToken<List<String>>() {
                }.getType());
        if (loadedOutros != null) {
            outros.addAll(loadedOutros);
        }
    }

    private void loadItemMetaphors() {
        String jsonPath = "data/" + BeyondTheBlock.MOD_ID + "/riddles/item_metaphors.json";
        try (InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(jsonPath)))) {

            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            for (var entry : jsonObject.entrySet()) {
                Identifier id = new Identifier(entry.getKey());
                Item item = Registry.ITEM.get(id);
                if (item == Items.AIR) {
              //      BeyondTheBlock.LOGGER.warn("Item {} not found in registry, skipping metaphor", id);
                    continue;
                }

                String metaphor = getMetaphor(entry);

                itemMetaphors.put(item, metaphor);
            }

        } catch (Exception e) {
            BeyondTheBlock.LOGGER.error("Failed to load item metaphors", e);
        }
    }

    private static String getMetaphor(Map.Entry<String, JsonElement> entry) {
        JsonElement value = entry.getValue();
        String metaphor;

        if (value.isJsonArray()) {
            JsonArray array = value.getAsJsonArray();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.size(); i++) {
                if (i > 0) sb.append(" ");
                sb.append(array.get(i).getAsString());
            }
            metaphor = sb.toString();
        } else if (value.isJsonPrimitive()) {
            metaphor = value.getAsString();
        } else {
            metaphor = "???";
        }
        return metaphor;
    }


    private <T> T loadJsonFile(String path, Type type) {
        Identifier id = new Identifier(BeyondTheBlock.MOD_ID, path);
        String fullPath = "data/" + id.getNamespace() + "/" + id.getPath();
        InputStreamReader reader;

        try {
            var stream = getClass().getClassLoader().getResourceAsStream(fullPath);
            if (stream == null) {
                BeyondTheBlock.LOGGER.error("Could not find JSON file at: {}", fullPath);
                return null;
            }
            reader = new InputStreamReader(stream);
        } catch (Exception e) {
            BeyondTheBlock.LOGGER.error("Error loading JSON file at: {}", fullPath, e);
            return null;
        }

        return GSON.fromJson(reader, type);
    }
}
