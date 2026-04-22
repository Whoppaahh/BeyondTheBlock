package net.ryan.beyond_the_block.utils;

import net.minecraft.util.StringIdentifiable;

public enum CreakingHeartState implements StringIdentifiable {
    UPROOTED("uprooted"),
    DORMANT("dormant"),
    AWAKE("awake");

    private final String name;

    CreakingHeartState(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
