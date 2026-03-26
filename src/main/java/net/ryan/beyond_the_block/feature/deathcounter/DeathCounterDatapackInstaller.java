package net.ryan.beyond_the_block.feature.deathcounter;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.WorldSavePath;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DeathCounterDatapackInstaller {
    ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
        try {
            Path worldDatapacks = minecraftServer.getSavePath(WorldSavePath.DATAPACKS);
            Path targetDatapack = worldDatapacks.resolve("beyond_the_block");

            boolean installed = false;
            if (!Files.exists(targetDatapack)) {
                Files.createDirectories(targetDatapack);

                // Copy pack.mcmeta
                copyResource("/beyond_the_block/pack.mcmeta", targetDatapack.resolve("pack.mcmeta"));

                // Copy setup.mcfunction
                Path dataFunctions = targetDatapack.resolve("data/death_counter/functions");
                Files.createDirectories(dataFunctions);
                copyResource("/beyond_the_block/data/beyond_the_block/functions/setup.mcfunction",
                        dataFunctions.resolve("setup.mcfunction"));

                // Copy load.json
                Path tagFunctions = targetDatapack.resolve("data/minecraft/tags/functions");
                Files.createDirectories(tagFunctions);
                copyResource("/beyond_the_block/data/minecraft/tags/functions/load.json",
                        tagFunctions.resolve("load.json"));

                installed = true;
                BeyondTheBlock.LOGGER.info("[DeathCounterMod] Datapack installed successfully!");
            }
            // Reload datapacks to ensure it's active immediately
            if (installed) {
                minecraftServer.getCommandManager().executeWithPrefix(minecraftServer.getCommandSource(),
                        "reload");
                BeyondTheBlock.LOGGER.info("[DeathCounterMod] Datapack reloaded!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    });

    private static void copyResource(String resourcePath, Path target) throws IOException {
        try (InputStream in = BeyondTheBlock.class.getResourceAsStream(resourcePath)) {
            if (in == null) throw new IOException("Resource not found: " + resourcePath);
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
