package net.ryan.beyond_the_block.advancements.criteria;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class SpongeDriedConditions extends AbstractCriterionConditions {
    private final ItemPredicate input;
    private final ItemPredicate output;
    private final Identifier dimension;

    public SpongeDriedConditions(
            Identifier id,
            EntityPredicate.Extended playerPredicate,
            ItemPredicate input,
            ItemPredicate output,
            Identifier dimension
    ) {
        super(id, playerPredicate);
        this.input = input;
        this.output = output;
        this.dimension = dimension;
    }

    public boolean matches(World world, ItemStack inputStack, ItemStack outputStack) {
        if (!this.input.test(inputStack)) {
            return false;
        }

        if (!this.output.test(outputStack)) {
            return false;
        }

        if (this.dimension != null && !world.getRegistryKey().getValue().equals(this.dimension)) {
            return false;
        }

        return true;
    }

    public static SpongeDriedConditions fromJson(
            Identifier id,
            JsonObject obj,
            EntityPredicate.Extended playerPredicate
    ) {
        ItemPredicate input = obj.has("input")
                ? ItemPredicate.fromJson(obj.get("input"))
                : ItemPredicate.ANY;

        ItemPredicate output = obj.has("output")
                ? ItemPredicate.fromJson(obj.get("output"))
                : ItemPredicate.ANY;

        Identifier dimension = obj.has("dimension")
                ? new Identifier(JsonHelper.getString(obj, "dimension"))
                : null;

        return new SpongeDriedConditions(id, playerPredicate, input, output, dimension);
    }
}