package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffects;

public class ClosedEyeblossomBlock extends AbstractEyeblossomBlock {

    public ClosedEyeblossomBlock(Settings settings) {
        super(StatusEffects.NAUSEA, 7, settings);
    }

    @Override
    protected boolean isOpenVariant() {
        return false;
    }
}
