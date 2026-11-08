package net.ryan.beyond_the_block.damage_sources;

import net.minecraft.entity.damage.DamageSource;

public class ModDamageSources {
    public static final DamageSource BLEED = new DamageSource("bleed").setBypassesArmor().setUnblockable();
    public static final DamageSource FREEZE = new DamageSource("freeze").setBypassesArmor().setUnblockable();
    public static final DamageSource CRYSTALLIZED = new DamageSource("crystallized").setBypassesArmor();
    public static final DamageSource SOUL_LINK = new DamageSource("soul_link").setBypassesArmor();
    public static final DamageSource STOMP = new DamageSource("stomp");

}
