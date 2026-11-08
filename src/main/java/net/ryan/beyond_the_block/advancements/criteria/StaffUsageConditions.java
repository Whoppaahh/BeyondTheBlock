package net.ryan.beyond_the_block.advancements.criteria;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class StaffUsageConditions extends AbstractCriterionConditions {
    private final int minOres;

    public StaffUsageConditions(Identifier id, EntityPredicate.Extended playerPredicate, int minOres) {
        super(id, playerPredicate);
        this.minOres = minOres;
    }

    public boolean matches(int oresFound) {
        return oresFound >= this.minOres;
    }

    public static StaffUsageConditions fromJson(Identifier id, JsonObject obj, EntityPredicate.Extended playerPredicate) {
        int minOres = JsonHelper.getInt(obj, "min_ores", 1);
        return new StaffUsageConditions(id, playerPredicate, minOres);
    }
}
