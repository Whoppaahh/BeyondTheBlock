package net.ryan.beyond_the_block.feature.naming;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

import java.util.Random;

public final class TitleResolvers {

    public record Title(String title, Formatting colour) {}

    public static Title resolve(Entity entity) {

        // Wolves
        if (entity instanceof WolfEntity) {
            return new Title("the Loyal Wolf", Formatting.AQUA);
        }

        // Cats (variant-based)
        if (entity instanceof CatEntity cat) {
            String variant = VariantUtils.getVariantPath(
                    Registry.CAT_VARIANT,
                    cat.getVariant()
            );

            String title = (variant == null || variant.equals("unknown"))
                    ? "the Cat"
                    : "the " + humanize(variant) + " Cat";

            return new Title(title, Formatting.GOLD);
        }

        // Parrots
        if (entity instanceof ParrotEntity) {
            return new Title("the Colourful Parrot", Formatting.GREEN);
        }

        // Horses (1.19.2 – enums, not registries)
        if (entity instanceof HorseEntity horse) {

            Random random = new Random();
            if(random.nextInt(25) == 0){
                return switch (horse.getColor()) {
                    case BLACK -> new Title("Black Beauty", Formatting.DARK_GRAY);
                    case WHITE -> new Title("White Stallion", Formatting.WHITE);
                    case CHESTNUT -> new Title("Firemane", Formatting.GOLD);
                    case BROWN -> new Title("Ironmane", Formatting.DARK_RED);
                    default -> new Title("Swiftwind", Formatting.GRAY);
                };
            }
            String color = horse.getColor().name().toLowerCase();       // e.g. "white", "chestnut"
            String marking = horse.getMarking().name().toLowerCase();   // e.g. "white_dots", "none"

            //            if (!"none".equals(marking)) {
//                title.append(humanize(marking)).append(" ");
//            }

            return new Title("the " + humanize(color) + " Horse", Formatting.DARK_GREEN);
        }


        return null;
    }


    private static String humanize(String id) {
        if (id == null || id.isEmpty()) return id;

        String[] parts = id.split("_");
        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) continue;
            sb.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1))
                    .append(" ");
        }

        return sb.toString().trim();
    }

}
