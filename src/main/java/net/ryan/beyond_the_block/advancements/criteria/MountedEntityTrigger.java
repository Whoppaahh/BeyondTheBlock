package net.ryan.beyond_the_block.advancements.criteria;

import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.advancements.BaseCriterionTrigger;

public class MountedEntityTrigger extends BaseCriterionTrigger<MountedEntityConditions> {
    public MountedEntityTrigger(Identifier id) {
        super(id);
    }

    @Override
    public MountedEntityConditions conditionsFromJson(
            JsonObject obj,
            EntityPredicate.Extended playerPredicate,
            AdvancementEntityPredicateDeserializer predicateDeserializer
    ) {
        return MountedEntityConditions.fromJson(this.id(), obj, playerPredicate);
    }

    public void trigger(ServerPlayerEntity player, Entity mountedEntity) {
        this.trigger(player, conditions -> conditions.matches(mountedEntity));
    }
}