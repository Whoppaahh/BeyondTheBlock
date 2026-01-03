package net.ryan.beyond_the_block.block.Sponges;

import net.ryan.beyond_the_block.block.ModBlocks;

public class TripleCompressedSpongeBlock extends AbstractCompressedSpongeBlock {
    public TripleCompressedSpongeBlock(Settings settings) {
        super(settings, 40,
                () -> ModBlocks.WET_TRIPLE_COMPRESSED_SPONGE);
    }
}

