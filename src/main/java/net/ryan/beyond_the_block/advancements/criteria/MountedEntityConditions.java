package net.ryan.beyond_the_block.advancements.criteria;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class MountedEntityConditions extends AbstractCriterionConditions {
    private final EntityType<?> entityType;
    private final int minPassengers;

    public MountedEntityConditions(
            Identifier id,
            EntityPredicate.Extended playerPredicate,
            EntityType<?> entityType,
            int minPassengers
    ) {
        super(id, playerPredicate);
        this.entityType = entityType;
        this.minPassengers = minPassengers;
    }

    public boolean matches(Entity mountedEntity) {
        if (this.entityType != null && mountedEntity.getType() != this.entityType) {
            return false;
        }

        return mountedEntity.getPassengerList().size() >= this.minPassengers;
    }

    public static MountedEntityConditions fromJson(
            Identifier id,
            JsonObject obj,
            EntityPredicate.Extended playerPredicate
    ) {
        EntityType<?> entityType = null;
        if (obj.has("entity_type")) {
            Identifier entityId = new Identifier(JsonHelper.getString(obj, "entity_type"));
            entityType = Registry.ENTITY_TYPE.get(entityId);
        }

        int minPassengers = JsonHelper.getInt(obj, "min_passengers", 1);

        return new MountedEntityConditions(id, playerPredicate, entityType, minPassengers);
    }
}