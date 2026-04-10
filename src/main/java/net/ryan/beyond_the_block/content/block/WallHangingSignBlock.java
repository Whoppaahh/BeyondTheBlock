package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.SignType;

public class WallHangingSignBlock extends AbstractSignBlock {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public WallHangingSignBlock(Settings settings, SignType type) {
        super(settings, type);
    }
}
