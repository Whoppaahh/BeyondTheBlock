package net.ryan.beyond_the_block.content.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
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
        tooltip.add(Text.translatable("item.beyond_the_block.smithing_template.armor_trim.applies_to"));

        if (isNetheriteUpgradeTemplate()) {
            tooltip.add(Text.literal(" ")
                    .append(Text.translatable("item.beyond_the_block.smithing_template.netherite_upgrade.applies_to")));
            tooltip.add(Text.translatable("item.beyond_the_block.smithing_template.armor_trim.ingredients"));
            tooltip.add(Text.literal(" ")
                    .append(Text.translatable("item.beyond_the_block.smithing_template.netherite_upgrade.ingredients")));
            return;
        }

        ModTrimPattern pattern = getPattern();
        if (pattern != null) {
            tooltip.add(Text.literal(" ")
                    .append(Text.translatable("item.beyond_the_block.smithing_template.armor_trim.applies_to")));
            tooltip.add(Text.translatable("item.beyond_the_block.smithing_template.armor_trim.ingredients"));
            tooltip.add(Text.literal(" ")
                    .append(Text.translatable("item.beyond_the_block.smithing_template.armor_trim.ingredients")));
        }
    }
}