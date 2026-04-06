package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.WoodcutterBlockEntity;

public class WoodcutterBlock extends BlockWithEntity {

    public WoodcutterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WoodcutterBlockEntity(pos, state);
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        return be instanceof WoodcutterBlockEntity wbe ? wbe : null;
    }
}