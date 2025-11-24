package net.ryan.beyond_the_block.damage_sources;

import net.minecraft.entity.damage.DamageSource;

public class ModDamageSources {
    public static final DamageSource BLEED = new DamageSource("bleed").setBypassesArmor().setUnblockable();

}
