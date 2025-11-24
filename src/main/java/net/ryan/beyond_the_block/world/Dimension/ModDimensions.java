package net.ryan.beyond_the_block.world.Dimension;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.ryan.beyond_the_block.BeyondTheBlock;

public class ModDimensions {
    private static final RegistryKey<DimensionOptions> DIMENSION_MINING_KEY = RegistryKey.of(Registry.DIMENSION_KEY,
            new Identifier(BeyondTheBlock.MOD_ID, "mining_dim"));
    public static RegistryKey<World> MINING_DIM_KEY = RegistryKey.of(Registry.WORLD_KEY, DIMENSION_MINING_KEY.getValue());
    private static final RegistryKey<DimensionType> DIMENSION_MINING_TYPE_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY,
            new Identifier(BeyondTheBlock.MOD_ID, "mining_dim_type"));

    private static final RegistryKey<DimensionOptions> DIMENSION_CHOPPING_KEY = RegistryKey.of(Registry.DIMENSION_KEY,
            new Identifier(BeyondTheBlock.MOD_ID, "chopping_dim"));
    public static RegistryKey<World> CHOPPING_DIM_KEY = RegistryKey.of(Registry.WORLD_KEY, DIMENSION_CHOPPING_KEY.getValue());
    private static final RegistryKey<DimensionType> DIMENSION_CHOPPING_TYPE_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY,
            new Identifier(BeyondTheBlock.MOD_ID, "chopping_dim_type"));

    public static void register() {
        BeyondTheBlock.LOGGER.info("Registering ModDimensions for " + BeyondTheBlock.MOD_ID);
    }
}
