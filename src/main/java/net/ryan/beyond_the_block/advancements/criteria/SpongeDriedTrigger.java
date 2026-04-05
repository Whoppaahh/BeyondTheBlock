package net.ryan.beyond_the_block.advancements.criteria;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.advancements.BaseCriterionTrigger;

public class SpongeDriedTrigger extends BaseCriterionTrigger<SpongeDriedConditions> {
    public SpongeDriedTrigger(Identifier id) {
        super(id);
    }

    @Override
    public SpongeDriedConditions conditionsFromJson(
            JsonObject obj,
            EntityPredicate.Extended playerPredicate,
            AdvancementEntityPredicateDeserializer predicateDeserializer
    ) {
        return SpongeDriedConditions.fromJson(this.id(), obj, playerPredicate);
    }

    public void trigger(ServerPlayerEntity player, World world, ItemStack inputStack, ItemStack outputStack) {
        this.trigger(player, conditions -> conditions.matches(world, inputStack, outputStack));
    }
}