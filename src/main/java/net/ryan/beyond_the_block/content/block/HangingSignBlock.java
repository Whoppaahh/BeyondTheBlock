package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.SignType;

public class HangingSignBlock extends AbstractSignBlock {

    public static final IntProperty ROTATION = Properties.ROTATION;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty ATTACHED = BooleanProperty.of("attached");

    public HangingSignBlock(Settings settings, SignType type) {
        super(settings, type);
    }
}
