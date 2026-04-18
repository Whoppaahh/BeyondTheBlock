package net.ryan.beyond_the_block.content.world.tree;

import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.ryan.beyond_the_block.content.world.feature.ModConfiguredFeatures;

public class PaleOakSaplingGenerator extends LargeTreeSaplingGenerator {

    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return null;
    }

    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getLargeTreeFeature(Random random) {
        return ModConfiguredFeatures.PALE_OAK_TREE;
    }
}