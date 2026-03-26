package net.ryan.beyond_the_block.content.block.Sponges;

import net.ryan.beyond_the_block.content.block.ModBlocks;

public class TripleCompressedSpongeBlock extends AbstractCompressedSpongeBlock {
    public TripleCompressedSpongeBlock(Settings settings) {
        super(settings, 40,
                () -> ModBlocks.WET_TRIPLE_COMPRESSED_SPONGE);
    }
}

