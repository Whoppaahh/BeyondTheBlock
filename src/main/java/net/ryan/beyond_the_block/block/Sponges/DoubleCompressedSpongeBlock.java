package net.ryan.beyond_the_block.block.Sponges;

import net.ryan.beyond_the_block.block.ModBlocks;

public class DoubleCompressedSpongeBlock extends AbstractCompressedSpongeBlock {
    public DoubleCompressedSpongeBlock(Settings settings) {
        super(settings, 26,
                () -> ModBlocks.WET_DOUBLE_COMPRESSED_SPONGE);
    }
}

