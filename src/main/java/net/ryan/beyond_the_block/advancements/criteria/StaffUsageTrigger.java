package net.ryan.beyond_the_block.advancements.criteria;

import com.google.gson.JsonObject;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.advancements.BaseCriterionTrigger;

public class StaffUsageTrigger extends BaseCriterionTrigger<StaffUsageConditions> {
    public StaffUsageTrigger(Identifier id) {
        super(id);
    }

    @Override
    public StaffUsageConditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return StaffUsageConditions.fromJson(this.id(), obj, playerPredicate);
    }

    public void trigger(ServerPlayerEntity player, int oresFound) {
        this.trigger(player, conditions -> conditions.matches(oresFound));
    }
}
