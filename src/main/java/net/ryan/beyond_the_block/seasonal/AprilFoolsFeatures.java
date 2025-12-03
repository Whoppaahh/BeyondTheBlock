package net.ryan.beyond_the_block.seasonal;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.ryan.beyond_the_block.item.Tools.ModSwordItem;
import net.ryan.beyond_the_block.utils.VillagerNames.EntityTagManager;

import java.util.List;
import java.util.Random;

public class AprilFoolsFeatures {

    private static final Random RANDOM = new Random();
    private static final List<Item> ALL_ITEMS = Registry.ITEM.stream().toList();
    private static final Object[] ALL_SWORDS = Registry.ITEM.stream().filter(item -> item instanceof SwordItem || item instanceof ModSwordItem).toArray();
    private static final List<Enchantment> ALL_ENCHANTMENTS = Registry.ENCHANTMENT.stream().toList();


    public static void register() {

        // Start hook
        HolidayFeatureRegistry.registerStart(HolidayManager.Holiday.APRIL_FOOLS, () -> {

            // Random upside-down mobs
            ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
                if (entity instanceof LivingEntity living && RANDOM.nextFloat() < 0.1f) {
                    living.setCustomName(Text.literal("Dinnerbone").formatted(Formatting.OBFUSCATED));
                    living.setCustomNameVisible(false);
                    ((EntityTagManager) living).beyondTheBlock$setHideName(true);
                }
            });

            // Rainbow sheep
            ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
                if (entity instanceof SheepEntity sheep && RANDOM.nextFloat() < 0.1f) {
                    sheep.setCustomName(Text.literal("jeb_"));
                    sheep.setCustomNameVisible(false);
                    ((EntityTagManager) sheep).beyondTheBlock$setHideName(true);
                }
            });

            ServerLivingEntityEvents.AFTER_DEATH.register(AprilFoolsFeatures::handleDeath);

        });

        // End hook (cleanup)
        HolidayFeatureRegistry.registerEnd(HolidayManager.Holiday.APRIL_FOOLS, () -> {
            for (ServerWorld world : HolidayManager.currentServer.getWorlds()) {
                for (LivingEntity living : world.getEntitiesByClass(LivingEntity.class,
                        world.getWorldBorder().asVoxelShape().getBoundingBox(),
                        e -> ((EntityTagManager) e).beyondTheBlock$shouldHideName())) {
                    living.setCustomName(null);
                    living.setCustomNameVisible(false);
                    ((EntityTagManager) living).beyondTheBlock$setHideName(false);
                }
            }
        });

    }

    private static void handleDeath(LivingEntity livingEntity, DamageSource damageSource) {
        dropAbsoluteChaos(livingEntity);
    }

    private static void dropAbsoluteChaos(LivingEntity entity) {
        World world = entity.world;
        if (world.isClient) return;
        BlockPos pos = entity.getBlockPos();

        int dropCount = 1 + RANDOM.nextInt(5); // up to 20 items
        for (int i = 0; i < dropCount; i++) {
            ItemStack stack = getRandomItemStack();

            // 20% chance of troll item chaos
            float chance = RANDOM.nextFloat();
            if (chance < 0.2f) {
                stack = getTrollLoot();
            }

            ItemEntity drop = new ItemEntity(world, pos.getX() + RANDOM.nextDouble(-0.5, 0.5),
                    pos.getY() + 0.5,
                    pos.getZ() + RANDOM.nextDouble(), stack);
            drop.setToDefaultPickupDelay();
            world.spawnEntity(drop);
        }

        // 5% chance to spawn an extra mob
        if (world.random.nextFloat() < 0.05f && world instanceof ServerWorld server) {
            spawnRandomMob(server, pos);
        }

        // 2% chance of a small explosion
        if (world.random.nextFloat() < 0.02f) {
            world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 2.5f, Explosion.DestructionType.DESTROY);
        }
    }

    private static ItemStack getRandomItemStack() {
        Item item = ALL_ITEMS.get(RANDOM.nextInt(ALL_ITEMS.size()));
        int count = 1 + RANDOM.nextInt(Math.min(item.getMaxCount(), 64));
        return new ItemStack(item, count);
    }

    private static ItemStack getTrollLoot() {
        float chance = RANDOM.nextFloat();

        if (chance < 0.2f) {
            // Random spawn egg
            return new ItemStack(Items.CREEPER_SPAWN_EGG);
        } else if (chance < 0.4f) {
            // Random enchanted sword
            ItemStack sword = new ItemStack(Item.byRawId(RANDOM.nextInt(0, ALL_SWORDS.length)));
            sword.addEnchantment(Enchantment.byRawId(RANDOM.nextInt(0, ALL_ENCHANTMENTS.size())), RANDOM.nextInt(1, 30));
            return sword;
        } else if (chance < 0.6f) {
            // Cursed potion
            ItemStack potion = PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.HARMING);
            potion.getOrCreateNbt().putString("display", "§cCursed Brew");
            return potion;
        } else if (chance < 0.8f) {
            // Stack of TNT
            return new ItemStack(Items.TNT, 16);
        } else {
            // Something useless but funny
            return new ItemStack(Items.STICK, 1);
        }
    }

    private static void spawnRandomMob(ServerWorld world, BlockPos pos) {
        EntityType<?>[] mobs = new EntityType[]{
                EntityType.CREEPER,
                EntityType.SHEEP,
                EntityType.BAT,
                EntityType.VILLAGER,
                EntityType.SLIME,
                EntityType.CHICKEN
        };

        EntityType<?> randomMob = mobs[RANDOM.nextInt(mobs.length)];
        LivingEntity mob = (LivingEntity) randomMob.create(world);

        if (mob != null) {
            world.spawnEntity(mob);

            // Sometimes buff the mob for more trolling
            if (mob instanceof CreeperEntity creeper && RANDOM.nextFloat() < 0.3f) {
                creeper.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 2));
            }
        }
    }
}
