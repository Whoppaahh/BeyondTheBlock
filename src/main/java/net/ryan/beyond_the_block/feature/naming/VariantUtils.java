package net.ryan.beyond_the_block.feature.naming;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class VariantUtils {

    private VariantUtils() {}

    /**
     * Safely extracts the registry path for a variant.
     *
     * @param registry The registry the variant belongs to
     * @param value    The variant instance
     * @return path-only id (e.g. "tabby", "brown", "spotted"), or "unknown"
     */
    public static <T> String getVariantPath(Registry<T> registry, T value) {
        if (value == null) return "unknown";

        Identifier id = registry.getId(value);
        if (id == null) return "unknown";

        return id.getPath();
    }
}
