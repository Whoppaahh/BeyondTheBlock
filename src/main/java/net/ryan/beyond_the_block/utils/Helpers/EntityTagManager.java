package net.ryan.beyond_the_block.utils.Helpers;

public interface EntityTagManager {
    //April Fools
    boolean beyondTheBlock$shouldHideName();
    void beyondTheBlock$setHideName(boolean hide);

    //Valentines
    boolean beyondTheBlock$wasLoveForced();
    void beyondTheBlock$setWasLoveForced(boolean hide);

    //Halloween
    boolean beyondTheBlock$isHalloweenCostume();
    void beyondTheBlock$setHalloweenCostume(boolean hide);

    //Christmas
    boolean beyondTheBlock$isWearingSantaHat();
    void beyondTheBlock$setWearingSantaHat(boolean hide);
    boolean beyondTheBlock$hasChristmasName();
    void beyondTheBlock$setHasChristmasName(boolean hide);
}

