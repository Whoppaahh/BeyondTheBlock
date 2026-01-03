package net.ryan.beyond_the_block.utils.Naming;

import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.ryan.beyond_the_block.config.ModConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class VillagerNameGenerator {
    public static final Random RANDOM = new Random();


    public static String pickName(ModConfig.NamesConfig.GenderMode genderMode) {
        List<String> pool = new ArrayList<>();
        if (genderMode == ModConfig.NamesConfig.GenderMode.MALE || genderMode == ModConfig.NamesConfig.GenderMode.BOTH)
            pool.addAll(NameLoader.getMaleNames());
        if (genderMode == ModConfig.NamesConfig.GenderMode.FEMALE || genderMode == ModConfig.NamesConfig.GenderMode.BOTH)
            pool.addAll(NameLoader.getFemaleNames());

        return pool.isEmpty() ? "Alex" : pool.get(RANDOM.nextInt(pool.size()));
    }

    public static List<String> pickNamesStartingWith(char letter, ModConfig.NamesConfig.GenderMode genderMode) {
        List<String> pool = new ArrayList<>();
        if (genderMode == ModConfig.NamesConfig.GenderMode.MALE || genderMode == ModConfig.NamesConfig.GenderMode.BOTH)
            pool.addAll(NameLoader.getMaleNames());
        if (genderMode == ModConfig.NamesConfig.GenderMode.FEMALE || genderMode == ModConfig.NamesConfig.GenderMode.BOTH)
            pool.addAll(NameLoader.getFemaleNames());

        final char c = Character.toUpperCase(letter);
        return pool.stream()
                .filter(n -> !n.isEmpty() && Character.toUpperCase(n.charAt(0)) == c)
                .collect(Collectors.toList());
    }

    public static String getProfessionTitle(VillagerProfession profession) {
        String id = Registry.VILLAGER_PROFESSION.getId(profession).toString();
        return switch (id) {
            case "minecraft:armorer" -> "the Armourer";
            case "minecraft:butcher" -> "the Butcher";
            case "minecraft:cartographer" -> "the Cartographer";
            case "minecraft:cleric" -> "the Cleric";
            case "minecraft:farmer" -> "the Farmer";
            case "minecraft:fisherman" -> "the Fisherman";
            case "minecraft:fletcher" -> "the Fletcher";
            case "minecraft:leatherworker" -> "the Tanner";
            case "minecraft:librarian" -> "the Librarian";
            case "minecraft:mason" -> "the Mason";
            case "minecraft:shepherd" -> "the Shepherd";
            case "minecraft:toolsmith" -> "the Toolsmith";
            case "minecraft:weaponsmith" -> "the Weaponsmith";
            default -> "the Villager";
        };
    }

    public static Formatting getProfessionColor(VillagerProfession profession) {
        String id = Registry.VILLAGER_PROFESSION.getId(profession).toString();
        return switch (id) {
            case "minecraft:farmer", "minecraft:fisherman", "minecraft:shepherd" -> Formatting.GREEN;
            case "minecraft:librarian", "minecraft:cartographer", "minecraft:cleric" -> Formatting.AQUA;
            case "minecraft:armorer", "minecraft:toolsmith", "minecraft:weaponsmith" -> Formatting.GRAY;
            case "minecraft:mason", "minecraft:leatherworker" -> Formatting.YELLOW;
            case "minecraft:butcher", "minecraft:fletcher" -> Formatting.RED;
            default -> Formatting.WHITE;
        };
    }
}
