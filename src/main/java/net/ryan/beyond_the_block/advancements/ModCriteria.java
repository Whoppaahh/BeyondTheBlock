package net.ryan.beyond_the_block.advancements;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.advancements.criteria.MinecartLinkedTrigger;
import net.ryan.beyond_the_block.advancements.criteria.MountedEntityTrigger;
import net.ryan.beyond_the_block.advancements.criteria.SpongeDriedTrigger;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public final class ModCriteria {
    public static final SpongeDriedTrigger SPONGE_DRIED =
            Criteria.register(new SpongeDriedTrigger(new Identifier(BeyondTheBlock.MOD_ID, "sponge_dried")));

    public static final MountedEntityTrigger MOUNTED_ENTITY =
            Criteria.register(new MountedEntityTrigger(new Identifier(BeyondTheBlock.MOD_ID, "mounted_entity")));

    public static final MinecartLinkedTrigger MINECART_LINKED =
            Criteria.register(new MinecartLinkedTrigger(new Identifier(BeyondTheBlock.MOD_ID, "minecart_linked")));

    private ModCriteria() {
    }

    public static void init() {
        // forces class load
    }
}