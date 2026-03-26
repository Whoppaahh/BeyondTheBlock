package net.ryan.beyond_the_block;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.chunk.WorldChunk;
import net.ryan.beyond_the_block.block.Entity.ModBlockEntities;
import net.ryan.beyond_the_block.block.ModBlocks;
import net.ryan.beyond_the_block.config.schema.ConfigClient;
import net.ryan.beyond_the_block.config.schema.ConfigServer;
import net.ryan.beyond_the_block.effect.ModEffects;
import net.ryan.beyond_the_block.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.entity.ModEntities;
import net.ryan.beyond_the_block.event.ModEvents;
import net.ryan.beyond_the_block.item.ModItems;
import net.ryan.beyond_the_block.network.ServerNetworking;
import net.ryan.beyond_the_block.particle.ModParticles;
import net.ryan.beyond_the_block.recipes.ModRecipes;
import net.ryan.beyond_the_block.riddles.RiddleComponents;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;
import net.ryan.beyond_the_block.seasonal.AprilFoolsFeatures;
import net.ryan.beyond_the_block.seasonal.ChristmasFeatures;
import net.ryan.beyond_the_block.seasonal.HalloweenFeatures;
import net.ryan.beyond_the_block.seasonal.ValentinesFeatures;
import net.ryan.beyond_the_block.sound.ModSounds;
import net.ryan.beyond_the_block.utils.Helpers.BleedingParticleHandler;
import net.ryan.beyond_the_block.utils.Helpers.SandToGlassManager;
import net.ryan.beyond_the_block.utils.Helpers.ServerContext;
import net.ryan.beyond_the_block.utils.ModLootTableModifiers;
import net.ryan.beyond_the_block.utils.ModTags;
import net.ryan.beyond_the_block.feature.naming.NameLoader;
import net.ryan.beyond_the_block.feature.theft_detection.VillageContainerScannerManager;
import net.ryan.beyond_the_block.feature.xp_orbs.HomingXPManager;
import net.ryan.beyond_the_block.village.ModVillagers;
import net.ryan.beyond_the_block.world.Dimension.ModDimensions;
import net.ryan.beyond_the_block.world.Feature.ModConfiguredFeatures;
import net.ryan.beyond_the_block.world.Gen.ModOreGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class BeyondTheBlock implements ModInitializer {

    public static final String MOD_ID = "beyond_the_block";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final RiddleComponents RIDDLE_COMPONENTS = new RiddleComponents();

    private static final int ASTRACINDER_FUEL = 22500;
    private static final int ECLIPSED_BLOOM_FUEL = 32767;


    private static BleedingParticleHandler BLEEDING_HANDLER;

    // ---------- Static event handlers (no lambda allocation at runtime) ----------
    private static final ServerTickEvents.EndWorldTick END_WORLD_TICK_HANDLER = world -> {
        if (world.isClient) return;
        var handler = RiddleComponents.get(world);
        if (handler != null) handler.tick(world);
        BLEEDING_HANDLER.onWorldTick(world);
        VillageContainerScannerManager.tick(world);
    };

    private static final ServerTickEvents.EndTick END_SERVER_TICK_HANDLER =
            server -> server.getWorlds().forEach(HomingXPManager::tick);

    private static final ServerChunkEvents.Load CHUNK_LOAD_HANDLER =
            (ServerWorld world, WorldChunk chunk) -> VillageContainerScannerManager.queueChunkForScan(world, chunk.getPos());

    // ---------------------------------------------------------------------------

    public static Hand getHandWith(LivingEntity entity, Predicate<Item> predicate) {
        return predicate.test(entity.getMainHandStack().getItem()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Beyond The Block...");

        setupConfig();
        registerModContent();
        registerFuel();
        registerEvents();
        registerSeasonalFeatures();
        ServerContext.init();

        LOGGER.info("Beyond The Block initialized successfully!");
    }

    // --------------------- Configuration ---------------------

    private void setupConfig() {
        AutoConfig.register(ConfigClient.class, JanksonConfigSerializer::new);
        AutoConfig.register(ConfigServer.class, JanksonConfigSerializer::new);
        BLEEDING_HANDLER = new BleedingParticleHandler();
        //holder.registerSaveListener((h, c) -> { c.validateConfig(); return ActionResult.SUCCESS; });
        //holder.registerLoadListener((h, c) -> { c.validateConfig(); return ActionResult.SUCCESS; });
        ServerLifecycleEvents.SERVER_STARTED.register(NameLoader::load);
    }

    // --------------------- Registrations ---------------------

    private void registerModContent() {
        ModConfiguredFeatures.registerConfiguredFeatures();
       // SnowGeneration.addToBiomes();
        ModBlocks.registerModBlocks();
        ModBlockEntities.registerModBlockEntities();
        ModItems.registerModItems();
        ModVillagers.registerModVillagers();
        ModRecipes.registerModRecipes();
        ModParticles.registerModParticles();
        ModEnchantments.registerModEnchantments();
        ModEffects.registerEffects();
        ModTags.registerModTags();
        ModOreGeneration.generateModOres();
        ModLootTableModifiers.modifyLootTables();
        ModDimensions.register();
        ModEntities.registerEntities();
        ModScreenHandlers.registerScreenHandlers();
        ModSounds.registerSounds();
        SandToGlassManager.register();

        RIDDLE_COMPONENTS.loadFromJson();
        ServerNetworking.registerC2SPackets();
        ModEvents.register();
    }

    private void registerFuel() {
        FuelRegistry.INSTANCE.add(ModItems.ASTRACINDER, ASTRACINDER_FUEL);
        FuelRegistry.INSTANCE.add(ModItems.ECLIPSED_BLOOM, ECLIPSED_BLOOM_FUEL);
    }

    private void registerEvents() {
        ServerTickEvents.END_WORLD_TICK.register(END_WORLD_TICK_HANDLER);
        ServerTickEvents.END_SERVER_TICK.register(END_SERVER_TICK_HANDLER);
        ServerChunkEvents.CHUNK_LOAD.register(CHUNK_LOAD_HANDLER);
    }



    private void registerSeasonalFeatures() {
        ValentinesFeatures.register();
        AprilFoolsFeatures.register();
        HalloweenFeatures.register();
        ChristmasFeatures.register();
    }

    }
