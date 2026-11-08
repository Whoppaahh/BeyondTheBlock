package net.ryan.beyond_the_block.advancements.criteria;

import com.google.gson.JsonObject;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.advancements.BaseCriterionTrigger;

public class TheftTrigger extends BaseCriterionTrigger<TheftConditions> {
    public TheftTrigger(Identifier id) {
        super(id);
    }

    @Override
    public TheftConditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return TheftConditions.fromJson(this.id(), obj, playerPredicate);
    }

    public void trigger(ServerPlayerEntity player, boolean caught) {
        this.trigger(player, conditions -> conditions.matches(caught));
    }
}
