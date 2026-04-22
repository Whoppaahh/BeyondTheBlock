package net.ryan.beyond_the_block.utils.accessors;

import net.ryan.beyond_the_block.feature.rails.CouplerSide;
import net.ryan.beyond_the_block.feature.rails.MinecartCouplerComponent;

import java.util.UUID;

public interface MinecartCouplerAccess {
    MinecartCouplerComponent beyond_the_block$getCouplers();
    UUID beyond_the_block$getSyncedCoupler(CouplerSide side);
    void beyond_the_block$syncCouplers();
}