package net.ryan.beyond_the_block.content.item.tools;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.ryan.beyond_the_block.content.item.armour.ModArmourMaterials;

import java.util.List;

public class TooltipHelper {
    public static void appendToolTooltip(List<Text> tooltip, ToolMaterial material) {
        if (material == ModToolMaterials.AMBERINE) {
            tooltip.add(Text.literal("High Efficiency: Digs faster than it looks.").formatted(Formatting.YELLOW));
        } else if (material == ModToolMaterials.MIRANITE) {
            tooltip.add(Text.literal("Shattering: Breaks through the toughest blocks.").formatted(Formatting.AQUA));
        } else if (material == ModToolMaterials.NOCTURNITE) {
            tooltip.add(Text.literal("End-Born: Harnesses the power of the void.").formatted(Formatting.DARK_PURPLE));
        } else if (material == ModToolMaterials.CHROMITE) {
            tooltip.add(Text.literal("Reinforced: Built to last through any trial.").formatted(Formatting.GOLD));
        } else if (material == ModToolMaterials.AZUROS) {
            tooltip.add(Text.literal("Azure Power: Infused with sky energy.").formatted(Formatting.BLUE));
        } else if (material == ModToolMaterials.ROSETTE) {
            tooltip.add(Text.literal("Petal-Forged: Surprisingly sturdy and elegant.").formatted(Formatting.LIGHT_PURPLE));
        } else if (material == ModToolMaterials.XIRION) {
            tooltip.add(Text.literal("Starlight: Shimmers with distant celestial light.").formatted(Formatting.WHITE));
        }
    }

    public static void appendSwordTooltip(List<Text> tooltip, ToolMaterial material) {
        if (material == ModToolMaterials.RUBY) {
            tooltip.add(Text.literal("Vampiric: Causes bleeding on hit.").formatted(Formatting.RED));
        }
        appendToolTooltip(tooltip, material);
        tooltip.add(Text.literal("Socketable: Supports gem infusions.").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.literal("Combine different gems for powerful combo effects!").formatted(Formatting.ITALIC).formatted(Formatting.GOLD));
    }

    public static void appendArmourTooltip(List<Text> tooltip, ArmorMaterial material) {
        if (material == ModArmourMaterials.RUBY) {
            tooltip.add(Text.literal("Set Bonus: Grants permanent Luck II when fully equipped.").formatted(Formatting.RED));
        } else if (material == ModArmourMaterials.AMBERINE) {
            tooltip.add(Text.literal("Weather Control: Clears rain and thunder when equipped.").formatted(Formatting.YELLOW));
        } else if (material == ModArmourMaterials.NOCTURNITE) {
            tooltip.add(Text.literal("Void Shield: Superior protection from the depths.").formatted(Formatting.DARK_PURPLE));
        } else if (material == ModArmourMaterials.AZUROS) {
            tooltip.add(Text.literal("Sky Protection: Lighter than average armor.").formatted(Formatting.BLUE));
        } else if (material == ModArmourMaterials.ROSETTE) {
            tooltip.add(Text.literal("Thorned: Elegant and defensive.").formatted(Formatting.LIGHT_PURPLE));
        } else if (material == ModArmourMaterials.XIRION) {
            tooltip.add(Text.literal("Celestial: Shines with inner light.").formatted(Formatting.WHITE));
        }
    }

    public static void appendArrowTooltip(List<Text> tooltip) {
        tooltip.add(Text.literal("Cupid's Touch: Makes animals fall in love and heals players.").formatted(Formatting.LIGHT_PURPLE));
    }
}
