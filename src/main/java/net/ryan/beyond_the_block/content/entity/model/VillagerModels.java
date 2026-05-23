package net.ryan.beyond_the_block.content.entity.model;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.client.render.entity.GuardEntityArmourModel;
import net.ryan.beyond_the_block.client.render.entity.GuardEntityModel;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class VillagerModels {

    public static final EntityModelLayer GUARD_ENTITY_MODEL = new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID + "guard_entity"), "main");
    public static final EntityModelLayer GUARD_ENTITY_ARMOUR_OUTER = new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID + "guard_armour_outer"), "main");
    public static final EntityModelLayer GUARD_ENTITY_ARMOUR_INNER = new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID + "guard_armour_inner"), "main");

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(GUARD_ENTITY_MODEL, GuardEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(GUARD_ENTITY_ARMOUR_OUTER, GuardEntityArmourModel::createOuterArmourLayer);
        EntityModelLayerRegistry.registerModelLayer(GUARD_ENTITY_ARMOUR_INNER, GuardEntityArmourModel::createInnerArmourLayer);
    }
}
