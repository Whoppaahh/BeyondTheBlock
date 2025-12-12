package net.ryan.beyond_the_block.utils;

import net.minecraft.util.StringIdentifiable;

public enum CauldronDye implements StringIdentifiable {
    WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK,
    GRAY, LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK;

    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
