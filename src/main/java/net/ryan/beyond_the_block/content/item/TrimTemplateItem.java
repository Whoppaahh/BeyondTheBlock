package net.ryan.beyond_the_block.content.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.content.registry.ModTrimRegistry;
import net.ryan.beyond_the_block.content.registry.family.ModTrimPattern;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrimTemplateItem extends Item {
    public TrimTemplateItem(Settings settings) {
        super(settings);
    }

    @Nullable
    public ModTrimPattern getPattern() {
        return ModTrimRegistry.getPatternFromTemplate(this);
    }

    private boolean isNetheriteUpgradeTemplate() {
        return this == ModItems.NETHERITE_UPGRADE_SMITHING_TEMPLATE;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.empty());

        tooltip.add(Text.translatable("item.minecraft.smithing_template.applies_to")
                .formatted(Formatting.GRAY));

        if (isNetheriteUpgradeTemplate()) {
            tooltip.add(Text.literal(" ")
                    .append(Text.translatable("item.beyond_the_block.smithing_template.netherite_upgrade.applies_to"))
                    .formatted(Formatting.BLUE));

            tooltip.add(Text.translatable("item.minecraft.smithing_template.ingredients")
                    .formatted(Formatting.GRAY));

            tooltip.add(Text.literal(" ")
                    .append(Text.translatable("item.beyond_the_block.smithing_template.netherite_upgrade.ingredients"))
                    .formatted(Formatting.BLUE));
            return;
        }

        tooltip.add(Text.literal(" ")
                .append(Text.translatable("item.beyond_the_block.smithing_template.armor_trim.applies_to"))
                .formatted(Formatting.BLUE));

        tooltip.add(Text.translatable("item.minecraft.smithing_template.ingredients")
                .formatted(Formatting.GRAY));

        tooltip.add(Text.literal(" ")
                .append(Text.translatable("item.beyond_the_block.smithing_template.armor_trim.ingredients"))
                .formatted(Formatting.BLUE));
    }
}