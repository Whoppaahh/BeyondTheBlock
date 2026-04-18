package net.ryan.beyond_the_block.content.world.tree;

import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.ryan.beyond_the_block.mixin.world.FoliagePlacerTypeInvoker;

public class ModFoliagePlacerTypes {

    public static final FoliagePlacerType<BtBCherryFoliagePlacer> CHERRY_FOLIAGE_PLACER =
            FoliagePlacerTypeInvoker.beyond_the_block$invokeRegister("cherry_foliage_placer", BtBCherryFoliagePlacer.CODEC);


    public static void register() {}
}