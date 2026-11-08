package net.ryan.beyond_the_block.advancements.criteria;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class TheftConditions extends AbstractCriterionConditions {
    private final boolean caught;

    public TheftConditions(Identifier id, EntityPredicate.Extended playerPredicate, boolean caught) {
        super(id, playerPredicate);
        this.caught = caught;
    }

    public boolean matches(boolean caught) {
        return this.caught == caught;
    }

    public static TheftConditions fromJson(Identifier id, JsonObject obj, EntityPredicate.Extended playerPredicate) {
        boolean caught = JsonHelper.getBoolean(obj, "caught", false);
        return new TheftConditions(id, playerPredicate, caught);
    }
}
