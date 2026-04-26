package net.ryan.beyond_the_block.feature.pets;

public enum PetCommandState {
    FOLLOW,
    STAY,
    WANDER;

    public PetCommandState next() {
        return switch (this) {
            case FOLLOW -> STAY;
            case STAY -> WANDER;
            case WANDER -> FOLLOW;
        };
    }

    public static PetCommandState fromId(int id) {
        PetCommandState[] values = values();
        if (id < 0 || id >= values.length) {
            return FOLLOW;
        }
        return values[id];
    }
}
