package net.ryan.beyond_the_block.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.entity.CupidArrowEntity;

public class CupidArrowItem extends ArrowItem {
    public CupidArrowItem(Settings settings) {
        super(settings);
    }

    @Override
    public CupidArrowEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new CupidArrowEntity(world, shooter);
    }
}
