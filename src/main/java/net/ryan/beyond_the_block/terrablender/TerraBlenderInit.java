package net.ryan.beyond_the_block.terrablender;

import net.ryan.beyond_the_block.content.world.biome.ModBiomeGeneration;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

public class TerraBlenderInit implements TerraBlenderApi{
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new ModBiomeGeneration());
    }
}
