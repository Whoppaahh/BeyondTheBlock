package net.ryan.beyond_the_block.content.block.sponges;

import net.ryan.beyond_the_block.content.block.ModBlocks;

public class CompressedSpongeBlock extends AbstractCompressedSpongeBlock {
    public CompressedSpongeBlock(Settings settings) {
        super(settings, 14,
                () -> ModBlocks.WET_COMPRESSED_SPONGE);
    }
}

