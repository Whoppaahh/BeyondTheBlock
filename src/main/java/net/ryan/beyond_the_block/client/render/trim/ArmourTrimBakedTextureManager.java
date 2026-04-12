package net.ryan.beyond_the_block.client.render.trim;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.registry.family.ModArmourTrim;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class ArmourTrimBakedTextureManager {

    private static final Map<String, Identifier> CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> FAILED = new ConcurrentHashMap<>();

    private ArmourTrimBakedTextureManager() {
    }

    public static Identifier get(ModArmourTrim.Data trim, boolean leggings) {
        String pattern = trim.patternId().getPath();
        String material = trim.materialId().getPath();
        String layer = leggings ? "humanoid_leggings" : "humanoid";

        String key = layer + "|" + pattern + "|" + material;

        if (FAILED.containsKey(key)) {
            return null;
        }

        Identifier cached = CACHE.get(key);
        if (cached != null) {
            return cached;
        }

        Identifier baked = bake(layer, pattern, material, key);
        if (baked == null) {
            FAILED.put(key, true);
            return null;
        }

        CACHE.put(key, baked);
        return baked;
    }

    public static void clear() {
        CACHE.clear();
        FAILED.clear();
    }

    private static Identifier bake(String layer, String pattern, String material, String cacheKey) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return null;
        }

        Identifier patternId = new Identifier("minecraft", "textures/trims/entity/" + layer + "/" + pattern + ".png");
        Identifier paletteId = new Identifier("minecraft", "textures/trims/color_palettes/" + material + ".png");

        Optional<Resource> patternRes = client.getResourceManager().getResource(patternId);
        Optional<Resource> paletteRes = client.getResourceManager().getResource(paletteId);

        if (patternRes.isEmpty() || paletteRes.isEmpty()) {
            return null;
        }

        try (
                InputStream patternStream = patternRes.get().getInputStream();
                InputStream paletteStream = paletteRes.get().getInputStream()
        ) {
            NativeImage patternImage = NativeImage.read(patternStream);
            NativeImage paletteImage = NativeImage.read(paletteStream);

            NativeImage bakedImage = bakeTrim(patternImage, paletteImage);

            Identifier dynamicId = new Identifier(BeyondTheBlock.MOD_ID, "generated/trims/" + cacheKey.replace('|', '/'));
            client.getTextureManager().registerTexture(dynamicId, new NativeImageBackedTexture(bakedImage));

            patternImage.close();
            paletteImage.close();

            return dynamicId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static NativeImage bakeTrim(NativeImage patternImage, NativeImage paletteImage) {
        int width = patternImage.getWidth();
        int height = patternImage.getHeight();

        NativeImage output = new NativeImage(width, height, true);

        int paletteWidth = paletteImage.getWidth();
        int paletteHeight = paletteImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = patternImage.getColor(x, y);
                int alpha = (argb >> 24) & 0xFF;

                if (alpha == 0) {
                    output.setColor(x, y, 0);
                    continue;
                }

                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                int brightness = (r + g + b) / 3;

                // Invert lookup so bright mask pixels use the lighter end of the palette
                int paletteX = Math.min(((255 - brightness) * (paletteWidth - 1)) / 255, paletteWidth - 1);
                int paletteY = paletteHeight - 1;

                int paletteColor = paletteImage.getColor(paletteX, paletteY);

                int paletteAlpha = (paletteColor >> 24) & 0xFF;
                int finalAlpha = (alpha * paletteAlpha) / 255;

                int pr = (paletteColor >> 16) & 0xFF;
                int pg = (paletteColor >> 8) & 0xFF;
                int pb = paletteColor & 0xFF;

                int out = (finalAlpha << 24) | (pr << 16) | (pg << 8) | pb;
                output.setColor(x, y, out);
            }
        }

        return output;
    }
}