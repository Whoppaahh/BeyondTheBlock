package net.ryan.beyond_the_block.advancements.criteria;

import com.google.gson.JsonObject;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.advancements.BaseCriterionTrigger;

public class MinecartLinkedTrigger extends BaseCriterionTrigger<MinecartLinkedConditions> {
    public MinecartLinkedTrigger(Identifier id) {
        super(id);
    }

    @Override
    public MinecartLinkedConditions conditionsFromJson(
            JsonObject obj,
            EntityPredicate.Extended playerPredicate,
            AdvancementEntityPredicateDeserializer predicateDeserializer
    ) {
        return MinecartLinkedConditions.fromJson(this.id(), obj, playerPredicate);
    }

    public void trigger(ServerPlayerEntity player, int cartCount) {
        this.trigger(player, conditions -> conditions.matches(cartCount));
    }
}