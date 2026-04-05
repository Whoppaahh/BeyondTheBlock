package net.ryan.beyond_the_block.advancements.criteria;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class MinecartLinkedConditions extends AbstractCriterionConditions {
    private final int minCarts;

    public MinecartLinkedConditions(
            Identifier id,
            EntityPredicate.Extended playerPredicate,
            int minCarts
    ) {
        super(id, playerPredicate);
        this.minCarts = minCarts;
    }

    public boolean matches(int cartCount) {
        return cartCount >= this.minCarts;
    }

    public static MinecartLinkedConditions fromJson(
            Identifier id,
            JsonObject obj,
            EntityPredicate.Extended playerPredicate
    ) {
        int minCarts = JsonHelper.getInt(obj, "min_carts", 2);
        return new MinecartLinkedConditions(id, playerPredicate, minCarts);
    }
}