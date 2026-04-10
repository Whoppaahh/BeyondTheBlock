package net.ryan.beyond_the_block.content.registry.family;

public enum ModBoatVariant {
    CHERRY("cherry", ModBoatRenderFamily.BOAT),
    PALE_OAK("pale_oak", ModBoatRenderFamily.BOAT),
    BAMBOO("bamboo", ModBoatRenderFamily.RAFT);

    private final String name;
    private final ModBoatRenderFamily family;

    ModBoatVariant(String name, ModBoatRenderFamily family) {
        this.name = name;
        this.family = family;
    }

    public String asString() {
        return this.name;
    }

    public ModBoatRenderFamily getFamily() {
        return this.family;
    }
}
