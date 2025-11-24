package net.ryan.beyond_the_block.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;

public class ExperienceBlock extends OreBlock {
    public ExperienceBlock(Settings settings){
        super(settings, UniformIntProvider.create(0, 0));
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player){
        super.onBreak(world, pos, state, player);
        if (!world.isClient && player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.addExperienceLevels(30);
        }
    }
}
