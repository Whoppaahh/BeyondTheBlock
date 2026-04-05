package net.ryan.beyond_the_block.feature.world.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.entity.villager.guard.GuardEntity;
import net.ryan.beyond_the_block.content.entity.villager.guard.goals.AttackEntityDaytimeGoal;
import net.ryan.beyond_the_block.content.entity.villager.guard.goals.HealGolemGoal;
import net.ryan.beyond_the_block.content.entity.villager.guard.goals.HealGuardAndPlayerGoal;

public final class GuardAIHandler {

    private GuardAIHandler() {
    }

    public static void apply(Entity entity) {
        applyRaidAnimalTargeting(entity);
        applyHostileGuardTargeting(entity);
        applyIllagerLogic(entity);
        applyVillagerLogic(entity);
        applyIronGolemLogic(entity);
        applyZombieLogic(entity);
        applyRavagerLogic(entity);
        applyWitchLogic(entity);
        applyCatLogic(entity);
    }

    private static void applyRaidAnimalTargeting(Entity entity) {
        if (!Configs.server().features.guards.raidAnimals) {
            return;
        }

        if (entity instanceof RaiderEntity raiderEntity && raiderEntity.hasActiveRaid()) {
            raiderEntity.targetSelector.add(
                    5,
                    new ActiveTargetGoal<>(raiderEntity, AnimalEntity.class, false)
            );
        }
    }

    private static void applyHostileGuardTargeting(Entity entity) {
        if (!Configs.server().features.guards.attackAllMobs) {
            return;
        }

        if (entity instanceof HostileEntity hostile && !(entity instanceof SpiderEntity)) {
            hostile.targetSelector.add(
                    2,
                    new ActiveTargetGoal<>(hostile, GuardEntity.class, false)
            );
        }

        if (entity instanceof SpiderEntity spider) {
            spider.targetSelector.add(
                    3,
                    new AttackEntityDaytimeGoal<>(spider, GuardEntity.class)
            );
        }
    }

    private static void applyIllagerLogic(Entity entity) {
        if (!(entity instanceof IllagerEntity illager)) {
            return;
        }

        if (Configs.server().features.guards.illagersRunFromPolarBears) {
            illager.goalSelector.add(
                    2,
                    new FleeEntityGoal<>(illager, PolarBearEntity.class, 6.0F, 1.0D, 1.2D)
            );
        }

        illager.targetSelector.add(
                2,
                new ActiveTargetGoal<>(illager, GuardEntity.class, false)
        );
    }

    private static void applyVillagerLogic(Entity entity) {
        if (!(entity instanceof VillagerEntity villagerEntity)) {
            return;
        }

        if (Configs.server().features.guards.villagersRunFromPolarBears) {
            villagerEntity.goalSelector.add(
                    2,
                    new FleeEntityGoal<>(villagerEntity, PolarBearEntity.class, 6.0F, 1.0D, 1.2D)
            );
        }

        if (Configs.server().features.guards.witchesVillager) {
            villagerEntity.goalSelector.add(
                    2,
                    new FleeEntityGoal<>(villagerEntity, WitchEntity.class, 6.0F, 1.0D, 1.2D)
            );
        }

        if (Configs.server().features.guards.blackSmithHealing) {
            villagerEntity.goalSelector.add(1, new HealGolemGoal(villagerEntity));
        }

        if (Configs.server().features.guards.clericHealing) {
            villagerEntity.goalSelector.add(
                    1,
                    new HealGuardAndPlayerGoal(villagerEntity, 1.0D, 100, 0, 10.0F)
            );
        }
    }

    private static void applyIronGolemLogic(Entity entity) {
        if (!(entity instanceof IronGolemEntity golem)) {
            return;
        }

        RevengeGoal tolerateFriendlyFire = new RevengeGoal(golem, GuardEntity.class).setGroupRevenge();

        golem.targetSelector.getGoals().stream()
                .map(PrioritizedGoal::getGoal)
                .filter(goal -> goal instanceof RevengeGoal)
                .findFirst()
                .ifPresent(existingGoal -> {
                    golem.targetSelector.remove(existingGoal);
                    golem.targetSelector.add(2, tolerateFriendlyFire);
                });
    }

    private static void applyZombieLogic(Entity entity) {
        if (!(entity instanceof ZombieEntity zombie)) {
            return;
        }

        zombie.targetSelector.add(
                3,
                new ActiveTargetGoal<>(zombie, GuardEntity.class, false)
        );
    }

    private static void applyRavagerLogic(Entity entity) {
        if (!(entity instanceof RavagerEntity ravager)) {
            return;
        }

        ravager.targetSelector.add(
                2,
                new ActiveTargetGoal<>(ravager, GuardEntity.class, false)
        );
    }

    private static void applyWitchLogic(Entity entity) {
        if (!(entity instanceof WitchEntity witch)) {
            return;
        }

        if (!Configs.server().features.guards.witchesVillager) {
            return;
        }

        witch.targetSelector.add(
                3,
                new ActiveTargetGoal<>(witch, VillagerEntity.class, true)
        );
        witch.targetSelector.add(
                3,
                new ActiveTargetGoal<>(witch, IronGolemEntity.class, true)
        );
        witch.targetSelector.add(
                3,
                new ActiveTargetGoal<>(witch, GuardEntity.class, true)
        );
    }

    private static void applyCatLogic(Entity entity) {
        if (!(entity instanceof CatEntity cat)) {
            return;
        }

        cat.goalSelector.add(
                1,
                new FleeEntityGoal<>(cat, IllagerEntity.class, 12.0F, 1.0D, 1.2D)
        );
    }
}