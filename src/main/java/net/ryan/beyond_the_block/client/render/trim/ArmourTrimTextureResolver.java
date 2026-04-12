package net.ryan.beyond_the_block.client.render.trim;

import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.registry.family.ModArmourTrim;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ArmourTrimTextureResolver {

    private static final Map<String, Identifier> CACHE = new ConcurrentHashMap<>();

    private ArmourTrimTextureResolver() {
    }

    public static Identifier resolve(ModArmourTrim.Data trim, boolean leggings) {
        String folder = leggings ? "humanoid_leggings" : "humanoid";
        String pattern = trim.patternId().getPath();
        String material = trim.materialId().getPath();

        String key = folder + "|" + pattern + "|" + material;

        return CACHE.computeIfAbsent(key, unused ->
                new Identifier(
                        BeyondTheBlock.MOD_ID,
                        "textures/trims/entity/" + folder + "/" + pattern + "_" + material + ".png"
                )
        );
    }
}