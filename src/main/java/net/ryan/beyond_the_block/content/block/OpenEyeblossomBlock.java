package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffects;

public class OpenEyeblossomBlock extends AbstractEyeblossomBlock {

    public OpenEyeblossomBlock(Settings settings) {
        super(StatusEffects.BLINDNESS, 11, settings);
    }

    @Override
    protected boolean isOpenVariant() {
        return true;
    }
}