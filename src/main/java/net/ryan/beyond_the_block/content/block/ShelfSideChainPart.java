package net.ryan.beyond_the_block.content.block;

import net.minecraft.util.StringIdentifiable;

public enum ShelfSideChainPart implements StringIdentifiable {
    UNCONNECTED("unconnected"),
    LEFT("left"),
    CENTER("center"),
    RIGHT("right");

    private final String name;

    ShelfSideChainPart(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
