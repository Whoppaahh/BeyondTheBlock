package net.ryan.beyond_the_block.village.GuardVillager;

import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.BeyondTheBlock;

public class GuardEntityLootTables {
    public static final LootContextType SLOT = LootContextTypes.register("slot", (builder) -> {
        builder.allow(LootContextParameters.THIS_ENTITY);
    });

    public static final Identifier GUARD_MAIN_HAND = new Identifier(BeyondTheBlock.MOD_ID, "entities/guard_main_hand");
    public static final Identifier GUARD_OFF_HAND = new Identifier(BeyondTheBlock.MOD_ID, "entities/guard_off_hand");
    public static final Identifier GUARD_HELMET = new Identifier(BeyondTheBlock.MOD_ID, "entities/guard_helmet");
    public static final Identifier GUARD_CHEST = new Identifier(BeyondTheBlock.MOD_ID, "entities/guard_chestplate");
    public static final Identifier GUARD_LEGGINGS = new Identifier(BeyondTheBlock.MOD_ID, "entities/guard_legs");
    public static final Identifier GUARD_FEET = new Identifier(BeyondTheBlock.MOD_ID, "entities/guard_feet");
}
