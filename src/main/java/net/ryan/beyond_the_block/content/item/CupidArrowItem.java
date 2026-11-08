package net.ryan.beyond_the_block.content.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.entity.CupidArrowEntity;
import net.ryan.beyond_the_block.content.item.tools.TooltipHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CupidArrowItem extends ArrowItem {
    public CupidArrowItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        TooltipHelper.appendArrowTooltip(tooltip);
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public CupidArrowEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new CupidArrowEntity(world, shooter);
    }
}
