package net.ryan.beyond_the_block.content.registry.family;

import net.minecraft.util.StringIdentifiable;

public enum ModBoatVariant implements StringIdentifiable {
    CHERRY("cherry", ModBoatFamily.BOAT),
    PALE_OAK("pale_oak", ModBoatFamily.BOAT),
    BAMBOO("bamboo", ModBoatFamily.RAFT);

    private final String name;
    private final ModBoatFamily family;

    ModBoatVariant(String name, ModBoatFamily family) {
        this.name = name;
        this.family = family;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public ModBoatFamily getFamily() {
        return family;
    }

    public static ModBoatVariant byId(int id) {
        ModBoatVariant[] values = values();
        return id < 0 || id >= values.length ? values[0] : values[id];
    }
}