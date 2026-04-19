package net.ryan.beyond_the_block.content.world.tree;

import net.minecraft.world.gen.trunk.TrunkPlacerType;
import net.ryan.beyond_the_block.mixin.world.TrunkPlacerTypeInvoker;

public class ModTrunkPlacerTypes {

    public static final TrunkPlacerType<CherryTrunkPlacer> CHERRY_TRUNK_PLACER =
            TrunkPlacerTypeInvoker.beyond_the_block$invokeRegister("cherry_trunk_placer", CherryTrunkPlacer.CODEC);


    public static void register() {}
}