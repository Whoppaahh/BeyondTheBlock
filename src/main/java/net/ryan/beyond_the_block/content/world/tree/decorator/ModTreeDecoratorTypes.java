package net.ryan.beyond_the_block.content.world.tree.decorator;

import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.mixin.world.TreeDecoratorTypeInvoker;

public class ModTreeDecoratorTypes {

    public static final TreeDecoratorType<PaleMossTreeDecorator> PALE_MOSS =
            TreeDecoratorTypeInvoker.beyond_the_block$invokeRegister(BeyondTheBlock.MOD_ID + ":pale_moss_decorator", PaleMossTreeDecorator.CODEC);

    public static void register() {}
}