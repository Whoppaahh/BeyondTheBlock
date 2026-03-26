package net.ryan.beyond_the_block.content.village;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestType;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.content.village.GuardVillager.Goals.AttackEntityDaytimeGoal;
import net.ryan.beyond_the_block.content.village.GuardVillager.Goals.HealGolemGoal;
import net.ryan.beyond_the_block.content.village.GuardVillager.Goals.HealGuardAndPlayerGoal;
import net.ryan.beyond_the_block.content.village.GuardVillager.GuardEntity;
import net.ryan.beyond_the_block.client.render.entity.GuardEntityArmourModel;
import net.ryan.beyond_the_block.client.render.entity.GuardEntityModel;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.event.GuardVillagersEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModVillagers {


    public static final PointOfInterestType JEWELLER_POI = registerPOI("jeweller_poi", ModBlocks.GEM_BLOCK);
    public static final VillagerProfession JEWELLER = registerProfession("jeweller", JEWELLER_POI);

//    public static final PointOfInterestType WIZARD_POI = registerPOI("wizard_poi", Blocks.ENCHANTING_TABLE);
//    public static final VillagerProfession WIZARD = registerProfession("wizard", WIZARD_POI);

    public static VillagerProfession registerProfession(String name, PointOfInterestType type) {
        return Registry.register(Registry.VILLAGER_PROFESSION, new Identifier(BeyondTheBlock.MOD_ID, name),
                new VillagerProfession(name,
                        holder -> holder.matchesKey(Registry.POINT_OF_INTEREST_TYPE.getKey(type).orElseThrow()),
                        holder -> holder.matchesKey(Registry.POINT_OF_INTEREST_TYPE.getKey(type).orElseThrow()),
                        ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_ARMORER));
    }

    public static PointOfInterestType registerPOI(String name, Block block) {
        return PointOfInterestHelper.register(new Identifier(BeyondTheBlock.MOD_ID, name),
                1, 1, ImmutableSet.copyOf(block.getStateManager().getStates()));
    }

    public static void registerTrades() {
        TradeOfferHelper.registerVillagerOffers(JEWELLER,1,
                factories -> {
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 3),
                            new ItemStack(ModItems.CHROMITE_ITEM, 1),
                            5, 5, 0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 5),
                            new ItemStack(ModItems.INDIGRA_ITEM, 2),
                            4, 10, 0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 3),
                            new ItemStack(ModItems.MIRANITE_ITEM, 1),
                            6, 10, 0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(ModItems.XIRION_ITEM, 1),
                            10, 3, 0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 3),
                            new ItemStack(ModItems.ROSETTE_ITEM, 1),
                            5, 5, 0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 3),
                            new ItemStack(ModItems.AZUROS_ITEM, 1),
                            5, 5, 0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 1),
                            new ItemStack(ModItems.AMBERINE_ITEM, 1),
                            15, 2, 0.02f
                    )));
                });
        TradeOfferHelper.registerVillagerOffers(JEWELLER,2,
                factories -> {
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 1),
                            new ItemStack(ModItems.CHROMITE_ITEM, 1),
                            15, 5, 0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 5),
                            new ItemStack(ModItems.INDIGRA_ITEM, 1),
                            10, 15, 0.03f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 3),
                            new ItemStack(ModItems.AZUROS_ITEM, 1),
                            8, 15, 0.03f
                    )));
                       factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 10),
                            new ItemStack(ModItems.NOCTURNITE_ITEM, 1),
                            4, 30, 0.05f
                    )));
                });

    }

    public static final EntityType<GuardEntity> GUARD_VILLAGER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(BeyondTheBlock.MOD_ID, "guard"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, GuardEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f)).build());


    public static final EntityModelLayer ROGUE_VILLAGER_MODEL = new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID, "rogue_villager"), "main");

    public static final EntityModelLayer GUARD_ENTITY_MODEL = new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID + "guard_entity"), "main");
    public static final EntityModelLayer GUARD_ENTITY_ARMOUR_OUTER = new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID + "guard_armour_outer"), "main");
    public static final EntityModelLayer GUARD_ENTITY_ARMOUR_INNER = new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID + "guard_armour_inner"), "main");

    private static final int WEAPON_SLOT = 5;

    public static void registerModVillagers() {


        FabricDefaultAttributeRegistry.register(GUARD_VILLAGER, GuardEntity.createAttributes());

        registerTrades();

        EntityModelLayerRegistry.registerModelLayer(GUARD_ENTITY_MODEL, GuardEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(GUARD_ENTITY_ARMOUR_OUTER, GuardEntityArmourModel::createOuterArmourLayer);
        EntityModelLayerRegistry.registerModelLayer(GUARD_ENTITY_ARMOUR_INNER, GuardEntityArmourModel::createInnerArmourLayer);

        UseEntityCallback.EVENT.register(ModVillagers::villagerConvert);
        GuardVillagersEvents.ON_TARGET_EVENT.register(ModVillagers::target);
        GuardVillagersEvents.ON_SPAWNED_ENTITY_EVENT.register(ModVillagers::addGoals);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(ModVillagers::onDamage);

        BeyondTheBlock.LOGGER.info("Registering Mod Villagers for " + BeyondTheBlock.MOD_ID);
    }

    private static ActionResult villagerConvert(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        ItemStack itemStack = player.getStackInHand(hand);
        boolean requiresHOTV = Configs.server().features.guards.convertVillagerIfHaveHotv;
        boolean hasHOTV = player.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE);

        if ((itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof CrossbowItem || itemStack.getItem() instanceof BowItem) && player.isSneaking()) {
            if (entityHitResult != null) {
                Entity target = entityHitResult.getEntity();
                if (target instanceof VillagerEntity villagerEntity) {
                    if (!villagerEntity.isBaby()) {
                        if (villagerEntity.getVillagerData().getProfession() == VillagerProfession.NONE || villagerEntity.getVillagerData().getProfession() == VillagerProfession.NITWIT) {
                            if (!requiresHOTV || hasHOTV) {
                                convertVillager(villagerEntity, player, world);
                                if (!player.getAbilities().creativeMode)
                                    itemStack.decrement(1);
                                return ActionResult.SUCCESS;
                            }
                        }
                    }
                }
            }

        }

        return ActionResult.PASS;
    }

    private static void convertVillager(VillagerEntity villagerEntity, PlayerEntity player, World world) {
        player.swingHand(Hand.MAIN_HAND);
        ItemStack itemstack = player.getEquippedStack(EquipmentSlot.MAINHAND);
        GuardEntity guard = GUARD_VILLAGER.create(world);
        if (guard == null)
            return;
        if (player.getWorld().isClient()) {
            ParticleEffect particleEffect = ParticleTypes.HAPPY_VILLAGER;
            for (int i = 0; i < 10; ++i) {
                double d0 = villagerEntity.getRandom().nextGaussian() * 0.02D;
                double d1 = villagerEntity.getRandom().nextGaussian() * 0.02D;
                double d2 = villagerEntity.getRandom().nextGaussian() * 0.02D;
                villagerEntity.getWorld().addParticle(particleEffect, villagerEntity.getX() + (double) (villagerEntity.getRandom().nextFloat() * villagerEntity.getWidth() * 2.0F) - (double) villagerEntity.getWidth(), villagerEntity.getY() + 0.5D + (double) (villagerEntity.getRandom().nextFloat() * villagerEntity.getWidth()),
                        villagerEntity.getZ() + (double) (villagerEntity.getRandom().nextFloat() * villagerEntity.getWidth() * 2.0F) - (double) villagerEntity.getWidth(), d0, d1, d2);
            }
        }
        guard.copyPositionAndRotation(villagerEntity);
        guard.headYaw = villagerEntity.headYaw;
        guard.refreshPositionAndAngles(villagerEntity.getX(), villagerEntity.getY(), villagerEntity.getZ(), villagerEntity.getYaw(), villagerEntity.getPitch());
        guard.playSound(SoundEvents.ENTITY_VILLAGER_YES, 1.0F, 1.0F);
        guard.equipStack(EquipmentSlot.MAINHAND, itemstack.copy());
        guard.guardInventory.setStack(WEAPON_SLOT, itemstack.copy());

        guard.setPersistent();
        guard.setCustomName(villagerEntity.getCustomName());
        guard.setCustomNameVisible(villagerEntity.isCustomNameVisible());
        guard.setEquipmentDropChance(EquipmentSlot.HEAD, 100.0F);
        guard.setEquipmentDropChance(EquipmentSlot.CHEST, 100.0F);
        guard.setEquipmentDropChance(EquipmentSlot.FEET, 100.0F);
        guard.setEquipmentDropChance(EquipmentSlot.LEGS, 100.0F);
        guard.setEquipmentDropChance(EquipmentSlot.MAINHAND, 100.0F);
        guard.setEquipmentDropChance(EquipmentSlot.OFFHAND, 100.0F);
        guard.setOwnerId(player.getUuid());
        world.spawnEntity(guard);
        villagerEntity.releaseTicketFor(MemoryModuleType.HOME);
        villagerEntity.releaseTicketFor(MemoryModuleType.JOB_SITE);
        villagerEntity.releaseTicketFor(MemoryModuleType.MEETING_POINT);
        villagerEntity.discard();
    }

    private static void target(LivingEntity mob, LivingEntity target) {
        if (target == null || mob instanceof GuardEntity) {
            return;
        }
        boolean isVillager = target.getType() == EntityType.VILLAGER || target instanceof GuardEntity;
        if (isVillager) {
            List<MobEntity> list = mob.getWorld().getNonSpectatingEntities(MobEntity.class, mob.getBoundingBox().expand(Configs.server().features.guards.guardVillagerHelpRange, 5.0D, Configs.server().features.guards.guardVillagerHelpRange));
            for (MobEntity mobEntity : list) {
                if ((mobEntity instanceof GuardEntity || mob.getType() == EntityType.IRON_GOLEM) && mobEntity.getTarget() == null) {
                    mobEntity.setTarget(mob);
                }
            }
        }

        if (mob instanceof IronGolemEntity golem && target instanceof GuardEntity) {
            golem.setTarget(null);
        }
    }

    private static void addGoals(ServerWorld serverWorld, Entity entity) {
        if (Configs.server().features.guards.raidAnimals) {
            if (entity instanceof RaiderEntity raiderEntity)
                if (raiderEntity.hasActiveRaid()) {
                    raiderEntity.targetSelector.add(5, new ActiveTargetGoal<>(raiderEntity, AnimalEntity.class, false));
                }
        }

        if (Configs.server().features.guards.attackAllMobs) {
            if (entity instanceof HostileEntity && !(entity instanceof SpiderEntity)) {
                MobEntity mob = (MobEntity) entity;
                mob.targetSelector.add(2, new ActiveTargetGoal<>(mob, GuardEntity.class, false));
            }
            if (entity instanceof SpiderEntity spider) {
                spider.targetSelector.add(3, new AttackEntityDaytimeGoal<>(spider, GuardEntity.class));
            }
        }


        if (entity instanceof IllagerEntity illager) {
            if (Configs.server().features.guards.illagersRunFromPolarBears) {
                illager.goalSelector.add(2, new FleeEntityGoal<>(illager, PolarBearEntity.class, 6.0F, 1.0D, 1.2D));
            }

            illager.targetSelector.add(2, new ActiveTargetGoal<>(illager, GuardEntity.class, false));
        }

        if (entity instanceof VillagerEntity villagerEntity) {
            if (Configs.server().features.guards.villagersRunFromPolarBears)
                villagerEntity.goalSelector.add(2, new FleeEntityGoal<>(villagerEntity, PolarBearEntity.class, 6.0F, 1.0D, 1.2D));
            if (Configs.server().features.guards.witchesVillager)
                villagerEntity.goalSelector.add(2, new FleeEntityGoal<>(villagerEntity, WitchEntity.class, 6.0F, 1.0D, 1.2D));
        }

        if (entity instanceof VillagerEntity villagerEntity) {
            if (Configs.server().features.guards.blackSmithHealing)
                villagerEntity.goalSelector.add(1, new HealGolemGoal(villagerEntity));
            if (Configs.server().features.guards.clericHealing)
                villagerEntity.goalSelector.add(1, new HealGuardAndPlayerGoal(villagerEntity, 1.0D, 100, 0, 10.0F));
        }

        if (entity instanceof IronGolemEntity golem) {

            RevengeGoal tolerateFriendlyFire = new RevengeGoal(golem, GuardEntity.class).setGroupRevenge();
            golem.targetSelector.getGoals().stream().map(PrioritizedGoal::getGoal).filter(it -> it instanceof RevengeGoal).findFirst().ifPresent(angerGoal -> {
                golem.targetSelector.remove(angerGoal);
                golem.targetSelector.add(2, tolerateFriendlyFire);
            });
        }

        if (entity instanceof ZombieEntity zombie) {
            zombie.targetSelector.add(3, new ActiveTargetGoal<>(zombie, GuardEntity.class, false));
        }

        if (entity instanceof RavagerEntity ravager) {
            ravager.targetSelector.add(2, new ActiveTargetGoal<>(ravager, GuardEntity.class, false));
        }

        if (entity instanceof WitchEntity witch) {
            if (Configs.server().features.guards.witchesVillager) {
                witch.targetSelector.add(3, new ActiveTargetGoal<>(witch, VillagerEntity.class, true));
                witch.targetSelector.add(3, new ActiveTargetGoal<>(witch, IronGolemEntity.class, true));
                witch.targetSelector.add(3, new ActiveTargetGoal<>(witch, GuardEntity.class, true));
            }
        }

        if (entity instanceof CatEntity cat) {
            cat.goalSelector.add(1, new FleeEntityGoal<>(cat, IllagerEntity.class, 12.0F, 1.0D, 1.2D));
        }
    }

    private static boolean onDamage(LivingEntity entity, DamageSource source, float amount) {
        Entity attacker = source.getAttacker();
        if (entity == null || attacker == null)
            return true;
        boolean shouldDamage = true;
        boolean isVillager = entity.getType() == EntityType.VILLAGER || entity.getType() == GUARD_VILLAGER;
        boolean isGolem = isVillager || entity.getType() == EntityType.IRON_GOLEM;
        if (isGolem && attacker.getType() == GUARD_VILLAGER && !Configs.server().features.guards.guardArrowsHurtVillagers) {
            shouldDamage = false;
        }
        if (isVillager && attacker instanceof MobEntity) {
            List<MobEntity> list = attacker.getWorld().getNonSpectatingEntities(MobEntity.class, attacker.getBoundingBox().expand(Configs.server().features.guards.guardVillagerHelpRange, 5.0D, Configs.server().features.guards.guardVillagerHelpRange));
            for (MobEntity mob : list) {
                boolean type = mob.getType() == GUARD_VILLAGER || mob.getType() == EntityType.IRON_GOLEM;
                boolean trueSourceGolem = attacker.getType() == GUARD_VILLAGER || attacker.getType() == EntityType.IRON_GOLEM;
                if (!trueSourceGolem && type && mob.getTarget() == null)
                    mob.setTarget((MobEntity) attacker);
            }
        }
        return shouldDamage;
    }

    public static boolean hotvChecker(PlayerEntity player, GuardEntity guard) {
        boolean hotvRequired = Configs.server().features.guards.giveGuardStuffHotv;
        boolean hasHOTV = player.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE);
        boolean highRep = guard.getPlayerEntityReputation(player) > Configs.server().features.guards.reputationRequirement;

        return (!hotvRequired || hasHOTV) || highRep && !player.getWorld().isClient();
    }

}
