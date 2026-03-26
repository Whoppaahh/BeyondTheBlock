package net.ryan.beyond_the_block.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.event.GuardVillagersEvents;
import net.ryan.beyond_the_block.village.GuardVillager.Goals.AttackEntityDaytimeGoal;
import net.ryan.beyond_the_block.village.GuardVillager.Goals.HealGolemGoal;
import net.ryan.beyond_the_block.village.GuardVillager.Goals.HealGuardAndPlayerGoal;
import net.ryan.beyond_the_block.village.GuardVillager.GuardEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements StructureWorldAccess {

    @Unique
    private int tickCounter = 0;

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "addEntity", at = @At("HEAD"))
    private void onSpawnedEvent(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        GuardVillagersEvents.ON_SPAWNED_ENTITY_EVENT.invoker().onSpawned((ServerWorld) (Object) this, entity);
    }

    @Inject(method = "spawnEntity", at = @At("TAIL"))
    private void onSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (Configs.server().features.guards.raidAnimals) {
            if (entity instanceof RaiderEntity raiderEntity)
                if (raiderEntity.hasActiveRaid()) {
                    raiderEntity.targetSelector.add(5, new ActiveTargetGoal<>(raiderEntity, AnimalEntity.class, false));
                }
        }

        if (Configs.server().features.guards.attackAllMobs) {
            if (entity instanceof HostileEntity mob && !(entity instanceof SpiderEntity)) {
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

    // This method will be triggered every game tick, and we'll check if we need to boost crop growth
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {

        tickCounter++;
        if (tickCounter % 20 != 0) return; // Run once per second

        ServerWorld world = (ServerWorld) (Object) this;

        // Check nearby crops around the player and apply growth boost
        for (PlayerEntity player : world.getPlayers()) {
            ItemStack tool = player.getMainHandStack();
            int bountyLevel = EnchantmentHelper.getLevel(ModEnchantments.GARDENS_BOUNTY, tool);
            int cultivationLevel = EnchantmentHelper.getLevel(ModEnchantments.NIGHT_CULTIVATION, tool);

            if (bountyLevel > 0) {
                // Apply the growth boost to crops within a 5-block radius
                BlockPos playerPos = player.getBlockPos();
                applyGrowthBoost(world, playerPos, 5, bountyLevel);
            }
            // If the player has NightCultivation on their hoe, and it's night or dark
            if (cultivationLevel > 0 && (world.isNight() || isDarkArea(player))) {
                // Boost crop growth around the player
                BlockPos playerPos = player.getBlockPos();
                applyGrowthBoost(world, playerPos, 10, cultivationLevel);
            }

        }
    }

    @Unique
    private boolean isDarkArea(PlayerEntity player) {
        // Check if the player is in a dark area (e.g., caves, Nether, etc.)
        return player.world.getLightLevel(player.getBlockPos()) < 5 || !player.world.getDimension().bedWorks();
    }


    // This method applies the growth boost to nearby crops
    @Unique
    private void applyGrowthBoost(World world, BlockPos pos, int radius, int level) {
        for (BlockPos checkPos : BlockPos.iterate(pos.add(-radius, -1, -radius), pos.add(radius, 1, radius))) {
            BlockState blockState = world.getBlockState(checkPos);

            if (blockState.getBlock() instanceof CropBlock cropBlock) {
                int age = blockState.get(CropBlock.AGE);

                // If the crop is not fully grown, speed up its growth
                if (age < cropBlock.getMaxAge()) {
                    world.setBlockState(checkPos, cropBlock.withAge(Math.min(age + level, cropBlock.getMaxAge())), 3);
                }
            }
        }
    }
}


