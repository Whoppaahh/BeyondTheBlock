package net.ryan.beyond_the_block.content.riddles;

import com.google.common.hash.Hashing;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.io.InputStream;
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

    public void loadFromJson() {
        intros.clear();
        outros.clear();
        itemMetaphors.clear();

        loadIntroStanzas();
        loadOutroStanzas();
        loadItemMetaphors();

        validateLoadedContent();
    }

    public List<String> getIntros() {
        return Collections.unmodifiableList(intros);
    }

    public List<String> getOutros() {
        return Collections.unmodifiableList(outros);
    }

    public Map<Item, String> getItemMetaphors() {
        return Collections.unmodifiableMap(itemMetaphors);
    }

    public String getRandomIntroStanza() {
        if (intros.isEmpty()) {
            throw new IllegalStateException("No riddle intro stanzas loaded");
        }
        return intros.get(random.nextInt(intros.size()));
    }

    public String getRandomOutroStanza() {
        if (outros.isEmpty()) {
            throw new IllegalStateException("No riddle outro stanzas loaded");
        }
        return outros.get(random.nextInt(outros.size()));
    }

    public String getItemMetaphor(Item item) {
        return itemMetaphors.getOrDefault(item, "???");
    }

    public List<Item> selectItemsForRiddle() {
        if (itemMetaphors.isEmpty()) {
            return Collections.emptyList();
        }

        List<Item> items = new ArrayList<>(itemMetaphors.keySet());
        Collections.shuffle(items, random);

        int count = 1 + random.nextInt(2); // 1 or 2
        return List.copyOf(items.subList(0, Math.min(count, items.size())));
    }

    public static String generateSignature(Riddle riddle) {
        String content = String.join("|", riddle.pages())
                + riddle.requiredItems().stream()
                .map(item -> Registry.ITEM.getId(item).toString())
                .collect(Collectors.joining(","));

        return Hashing.sha256().hashString(content, StandardCharsets.UTF_8).toString();
    }

    public Optional<Riddle> createRandomRiddle(Set<String> seenSignatures) {
        if (intros.isEmpty() || outros.isEmpty() || itemMetaphors.isEmpty()) {
            BeyondTheBlock.LOGGER.error("Cannot generate riddle: components are not loaded");
            return Optional.empty();
        }

        for (int attempts = 0; attempts < 100; attempts++) {
            String intro = getRandomIntroStanza();
            String outro = getRandomOutroStanza();
            List<Item> selectedItems = selectItemsForRiddle();

            if (selectedItems.isEmpty()) {
                BeyondTheBlock.LOGGER.warn("Cannot generate riddle: no selectable items");
                return Optional.empty();
            }

            List<String> pages = new ArrayList<>();
            pages.add(intro);

            for (Item item : selectedItems) {
                pages.add(getItemMetaphor(item));
            }

            pages.add(outro);

            Riddle riddle = new Riddle(UUID.randomUUID(), pages, selectedItems);
            String signature = generateSignature(riddle);

            if (seenSignatures.add(signature)) {
                return Optional.of(riddle);
            }
        }

        BeyondTheBlock.LOGGER.warn("Failed to create unique riddle after 100 attempts");
        return Optional.empty();
    }

    private void validateLoadedContent() {
        if (intros.isEmpty()) {
            BeyondTheBlock.LOGGER.error("No riddle intros loaded");
        }
        if (outros.isEmpty()) {
            BeyondTheBlock.LOGGER.error("No riddle outros loaded");
        }
        if (itemMetaphors.isEmpty()) {
            BeyondTheBlock.LOGGER.error("No riddle item metaphors loaded");
        }
    }

    private void loadIntroStanzas() {
        List<String> loadedIntros = loadJsonFile(
                "riddles/intros.json",
                new TypeToken<List<String>>() {}.getType()
        );

        if (loadedIntros != null) {
            intros.addAll(loadedIntros);
        }
    }

    private void loadOutroStanzas() {
        List<String> loadedOutros = loadJsonFile(
                "riddles/outros.json",
                new TypeToken<List<String>>() {}.getType()
        );

        if (loadedOutros != null) {
            outros.addAll(loadedOutros);
        }
    }

    private void loadItemMetaphors() {
        String jsonPath = "data/" + BeyondTheBlock.MOD_ID + "/riddles/item_metaphors.json";

        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(jsonPath)) {
            if (stream == null) {
                BeyondTheBlock.LOGGER.error("Failed to find item metaphor file at {}", jsonPath);
                return;
            }

            try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    Identifier id;
                    try {
                        id = new Identifier(entry.getKey());
                    } catch (Exception e) {
                        BeyondTheBlock.LOGGER.warn("Invalid item metaphor key '{}'", entry.getKey());
                        continue;
                    }

                    Item item = Registry.ITEM.get(id);
                    if (item == Items.AIR) {
                        continue;
                    }

                    itemMetaphors.put(item, getMetaphor(entry));
                }
            }
        } catch (Exception e) {
            BeyondTheBlock.LOGGER.error("Failed to load item metaphors", e);
        }
    }

    private static String getMetaphor(Map.Entry<String, JsonElement> entry) {
        JsonElement value = entry.getValue();

        if (value.isJsonArray()) {
            JsonArray array = value.getAsJsonArray();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < array.size(); i++) {
                if (i > 0) {
                    sb.append(" ");
                }
                sb.append(array.get(i).getAsString());
            }

            return sb.toString();
        }

        if (value.isJsonPrimitive()) {
            return value.getAsString();
        }

        return "???";
    }

    private <T> T loadJsonFile(String path, Type type) {
        Identifier id = new Identifier(BeyondTheBlock.MOD_ID, path);
        String fullPath = "data/" + id.getNamespace() + "/" + id.getPath();

        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(fullPath)) {
            if (stream == null) {
                BeyondTheBlock.LOGGER.error("Could not find JSON file at: {}", fullPath);
                return null;
            }

            try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                return GSON.fromJson(reader, type);
            }
        } catch (Exception e) {
            BeyondTheBlock.LOGGER.error("Error loading JSON file at: {}", fullPath, e);
            return null;
        }
    }
}