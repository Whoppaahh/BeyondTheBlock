package net.ryan.beyond_the_block.feature.fire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.config.sync.SyncedServerConfig;

import java.util.List;
import java.util.Locale;

public final class FireColourResolver {
    private FireColourResolver() {
    }

    public static int resolve(BlockRenderView world, BlockPos firePos, boolean soulFire) {
        SyncedServerConfig cfg = Configs.syncedServerConfig();

        if (!cfg.fireEnabled()) {
            return 0xFFFFFF;
        }

        BlockPos belowPos = firePos.down();
        BlockState belowState = world.getBlockState(belowPos);

        Integer resolved = switch (cfg.firePriority()) {
            case TAG_BLOCK_BIOME -> firstNonNull(
                    matchBlockTag(cfg.fireBlockTagRules(), belowState),
                    matchBlock(cfg.fireBlockRules(), belowState),
                    matchBiome(cfg.fireBiomeRules(), world, firePos)
            );
            case BLOCK_BIOME_TAG -> firstNonNull(
                    matchBlock(cfg.fireBlockRules(), belowState),
                    matchBiome(cfg.fireBiomeRules(), world, firePos),
                    matchBlockTag(cfg.fireBlockTagRules(), belowState)
            );
            case BIOME_BLOCK_TAG -> firstNonNull(
                    matchBiome(cfg.fireBiomeRules(), world, firePos),
                    matchBlock(cfg.fireBlockRules(), belowState),
                    matchBlockTag(cfg.fireBlockTagRules(), belowState)
            );
            case BLOCK_TAG_BIOME -> firstNonNull(
                    matchBlock(cfg.fireBlockRules(), belowState),
                    matchBlockTag(cfg.fireBlockTagRules(), belowState),
                    matchBiome(cfg.fireBiomeRules(), world, firePos)
            );
        };

        if (resolved != null) {
            return resolved;
        }

        return soulFire ? cfg.fireSoulBaseColor() : cfg.fireBaseColor();
    }

    private static Integer matchBiome(List<String> rules, BlockRenderView  world, BlockPos pos) {
        if (!(world instanceof WorldView worldView)) {
            return null;
        }
        RegistryEntry<Biome> biomeEntry = worldView.getBiome(pos);

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return null;

        Identifier biomeId = client.world.getRegistryManager()
                .get(Registry.BIOME_KEY)
                .getId(biomeEntry.value());

        if (biomeId == null) return null;

        for (String raw : rules) {
            ParsedRule parsed = ParsedRule.parse(raw, false);
            if (parsed != null && biomeId.toString().equals(parsed.key())) {
                return parsed.colour();
            }
        }

        return null;
    }

    private static Integer matchBlock(List<String> rules, BlockState state) {
        Identifier blockId = Registry.BLOCK.getId(state.getBlock());

        for (String raw : rules) {
            ParsedRule parsed = ParsedRule.parse(raw, false);
            if (parsed != null && blockId.toString().equals(parsed.key())) {
                return parsed.colour();
            }
        }

        return null;
    }

    private static Integer matchBlockTag(List<String> rules, BlockState state) {
        for (String raw : rules) {
            ParsedRule parsed = ParsedRule.parse(raw, true);
            if (parsed == null) continue;

            Identifier tagId = Identifier.tryParse(parsed.key());
            if (tagId == null) continue;

            TagKey<Block> tagKey = TagKey.of(Registry.BLOCK_KEY, tagId);
            if (state.isIn(tagKey)) {
                return parsed.colour();
            }
        }

        return null;
    }

    @SafeVarargs
    private static <T> T firstNonNull(T... values) {
        for (T value : values) {
            if (value != null) return value;
        }
        return null;
    }

    private record ParsedRule(String key, int colour) {
        static ParsedRule parse(String raw, boolean expectTag) {
            if (raw == null) return null;

            String trimmed = raw.trim();
            int split = trimmed.indexOf('=');
            if (split <= 0 || split >= trimmed.length() - 1) {
                return null;
            }

            String left = trimmed.substring(0, split).trim();
            String right = trimmed.substring(split + 1).trim();

            if (expectTag) {
                if (!left.startsWith("#")) return null;
                left = left.substring(1);
            } else {
                if (left.startsWith("#")) return null;
            }

            Integer colour = parseColour(right);
            if (colour == null) return null;

            return new ParsedRule(left, colour);
        }

        private static Integer parseColour(String raw) {
            String value = raw.trim().toLowerCase(Locale.ROOT);

            try {
                if (value.startsWith("0x")) {
                    return (int) Long.parseLong(value.substring(2), 16);
                }
                if (value.startsWith("#")) {
                    return (int) Long.parseLong(value.substring(1), 16);
                }
                return Integer.parseInt(value);
            } catch (Exception ignored) {
                return null;
            }
        }
    }
}