package net.ryan.beyond_the_block.utils.Naming;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.ryan.beyond_the_block.config.ModConfig;

import java.util.List;
import java.util.Random;

public final class NameEngine {

    private static final Random RANDOM = new Random();

    public static void assignName(
            LivingEntity entity,
            NameableMob nameable,
            String title,
            Formatting colour,
            ModConfig.NamesConfig cfg,
            boolean allowAlliteration
    ) {
        // Base name
        String baseName = nameable.beyondTheBlock$getBaseName();
        if (baseName == null || baseName.isBlank()) {
            baseName = VillagerNameGenerator.pickName(cfg.genderMode);
            nameable.beyondTheBlock$setBaseName(baseName);
        }

        // Alliteration (caller decides if allowed)
        if (cfg.useAlliteration && allowAlliteration && title.startsWith("the ")) {
            char letter = Character.toUpperCase(title.charAt(4));
            List<String> options =
                    VillagerNameGenerator.pickNamesStartingWith(letter, cfg.genderMode);

            if (!options.isEmpty()) {
                baseName = options.get(RANDOM.nextInt(options.size()));
                nameable.beyondTheBlock$setBaseName(baseName);
            }
        }

        entity.setCustomName(
                Text.literal(baseName + " " + title)
                        .formatted(cfg.colouriseNames ? colour : Formatting.WHITE)
        );
        entity.setCustomNameVisible(false);
    }

    private NameEngine() {}
}
