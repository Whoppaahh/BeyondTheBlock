package net.ryan.beyond_the_block.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public abstract class BaseCriterionTrigger<T extends AbstractCriterionConditions> extends AbstractCriterion<T> {
    private final Identifier id;

    protected BaseCriterionTrigger(Identifier id) {
        this.id = id;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    protected Identifier id() {
        return this.id;
    }

    @Override
    public abstract T conditionsFromJson(
            JsonObject obj,
            EntityPredicate.Extended playerPredicate,
            AdvancementEntityPredicateDeserializer predicateDeserializer
    );

    protected void trigger(ServerPlayerEntity player, Predicate<T> predicate) {
        this.trigger(player, predicate);
    }
}