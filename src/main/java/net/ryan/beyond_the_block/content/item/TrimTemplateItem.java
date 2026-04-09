package net.ryan.beyond_the_block.content.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
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

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        ModTrimPattern pattern = getPattern();
        if (pattern != null) {
            tooltip.add(Text.translatable("item.minecraft.smithing_template.applies_to"));
            tooltip.add(Text.literal(" ").append(Text.translatable("item.beyond_the_block.smithing_template.armor_trim.applies_to")));
            tooltip.add(Text.translatable("item.minecraft.smithing_template.ingredients"));
            tooltip.add(Text.literal(" ").append(Text.translatable("item.beyond_the_block.smithing_template.armor_trim.ingredients")));
        }
    }
}
