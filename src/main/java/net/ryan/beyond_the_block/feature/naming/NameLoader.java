package net.ryan.beyond_the_block.feature.naming;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resource.Resource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class NameLoader {
    private static final Identifier NAMES_FILE = new Identifier(BeyondTheBlock.MOD_ID, "villager_names/names.json");

    private static final List<String> maleNames = new ArrayList<>();
    private static final List<String> femaleNames = new ArrayList<>();

    public static void load(MinecraftServer server) {
        maleNames.clear();
        femaleNames.clear();

        Optional<Resource> optional = server.getResourceManager().getResource(NAMES_FILE);
        if (optional.isEmpty()) {
            BeyondTheBlock.LOGGER.warn("Names file not found: {}", NAMES_FILE);
            return;
        }

        Resource resource = optional.get();
        try (InputStream stream = resource.getInputStream();
             Reader reader = new InputStreamReader(stream)) {

            JsonElement json = JsonParser.parseReader(reader);
            if (!json.isJsonObject()) {
                BeyondTheBlock.LOGGER.error("Names JSON root must be an object!");
                return;
            }

            JsonObject obj = json.getAsJsonObject();

            // Parse male names
            if (obj.has("male") && obj.get("male").isJsonArray()) {
                JsonArray males = obj.getAsJsonArray("male");
                for (JsonElement element : males) {
                    if (element.isJsonPrimitive()) {
                        maleNames.add(element.getAsString());
                    }
                }
            }

            // Parse female names
            if (obj.has("female") && obj.get("female").isJsonArray()) {
                JsonArray females = obj.getAsJsonArray("female");
                for (JsonElement element : females) {
                    if (element.isJsonPrimitive()) {
                        femaleNames.add(element.getAsString());
                    }
                }
            }

            BeyondTheBlock.LOGGER.info("Loaded {} male and {} female names.",
                    maleNames.size(), femaleNames.size());

        } catch (IOException e) {
            BeyondTheBlock.LOGGER.error("Failed to load names from {}", NAMES_FILE, e);
        }
    }

    public static List<String> getMaleNames() {
        return Collections.unmodifiableList(maleNames);
    }

    public static List<String> getFemaleNames() {
        return Collections.unmodifiableList(femaleNames);
    }
}
