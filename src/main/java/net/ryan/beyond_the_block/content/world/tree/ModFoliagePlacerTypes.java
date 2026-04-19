package net.ryan.beyond_the_block.content.world.tree;

import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.ryan.beyond_the_block.mixin.world.FoliagePlacerTypeInvoker;

public class ModFoliagePlacerTypes {

    public static final FoliagePlacerType<CherryFoliagePlacer> CHERRY_FOLIAGE_PLACER =
            FoliagePlacerTypeInvoker.beyond_the_block$invokeRegister("cherry_foliage_placer", CherryFoliagePlacer.CODEC);


    public static void register() {}
}