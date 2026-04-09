package net.ryan.beyond_the_block.client.render.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.ryan.beyond_the_block.client.render.item.CupidArrowEntityRenderer;
import net.ryan.beyond_the_block.content.entity.villager.ModVillagers;
import net.ryan.beyond_the_block.content.registry.ModEntities;

public class EntityRenderRegistrar {
    public static void register(){
        registerEntityRenderers();
    }
    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.WITHER_ZOMBIE, WitherZombieRenderer::new);
        EntityRendererRegistry.register(ModEntities.WITHER_ZOMBIE_HORSE, WitherZombieHorseRenderer::new);
        EntityRendererRegistry.register(ModEntities.WITHER_SKELETON_HORSE, WitherSkeletonHorseRenderer::new);
        EntityRendererRegistry.register(ModVillagers.GUARD_VILLAGER, GuardEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.COBWEB_PROJECTILE, CobwebProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.CUPID_ARROW, CupidArrowEntityRenderer::new);
    }
}
