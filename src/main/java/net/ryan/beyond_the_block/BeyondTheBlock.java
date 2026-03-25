package net.ryan.beyond_the_block;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.WorldChunk;
import net.ryan.beyond_the_block.block.Entity.ModBlockEntities;
import net.ryan.beyond_the_block.block.ModBlocks;
import net.ryan.beyond_the_block.config.ConfigClient;
import net.ryan.beyond_the_block.config.ConfigServer;
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
import net.ryan.beyond_the_block.utils.Naming.NameLoader;
import net.ryan.beyond_the_block.utils.TheftDetection.VillageContainerScannerManager;
import net.ryan.beyond_the_block.utils.XPOrbs.HomingXPManager;
import net.ryan.beyond_the_block.village.ModVillagers;
import net.ryan.beyond_the_block.world.Dimension.ModDimensions;
import net.ryan.beyond_the_block.world.Feature.ModConfiguredFeatures;
import net.ryan.beyond_the_block.world.Gen.ModOreGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class BeyondTheBlock implements ModInitializer {

    public static final String MOD_ID = "beyond_the_block";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final RiddleComponents RIDDLE_COMPONENTS = new RiddleComponents();

    private static final int ASTRACINDER_FUEL = 22500;
    private static final int ECLIPSED_BLOOM_FUEL = 32767;

    private static final Map<String, Formatting> GEM_COLORS = Map.ofEntries(
            Map.entry("miranite", Formatting.GREEN),
            Map.entry("chromite", Formatting.LIGHT_PURPLE),
            Map.entry("nocturnite", Formatting.DARK_PURPLE),
            Map.entry("amberine", Formatting.GOLD),
            Map.entry("azuros", Formatting.AQUA),
            Map.entry("indigra", Formatting.BLUE),
            Map.entry("rosette", Formatting.RED)
    );

    private static final Map<String, Text> ENCHANTMENT_DESC_CACHE = new HashMap<>();
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
            (ServerWorld world, WorldChunk chunk) -> VillageContainerScannerManager.queueChunkForScan(chunk.getPos());

    // ---------------------------------------------------------------------------

    public static Hand getHandWith(LivingEntity entity, Predicate<Item> predicate) {
        return predicate.test(entity.getMainHandStack().getItem()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Beyond The Block...");
        GeckoLib.initialize();

        setupConfig();
        registerModContent();
        registerFuel();
        registerEvents();
        registerTooltips();
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

    private void registerTooltips() {
        ItemTooltipCallback.EVENT.register(this::handleTooltip);
    }

    private void registerSeasonalFeatures() {
        ValentinesFeatures.register();
        AprilFoolsFeatures.register();
        HalloweenFeatures.register();
        ChristmasFeatures.register();
    }

    // --------------------- Tooltip logic ---------------------

    private void handleTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("GemList", NbtElement.LIST_TYPE))
            appendGemInfo(nbt, tooltip);
        appendEnchantmentInfo(stack, tooltip);
    }

    private void appendGemInfo(NbtCompound nbt, List<Text> tooltip) {
        NbtList gems = nbt.getList("GemList", NbtElement.STRING_TYPE);
        if (gems.isEmpty()) return;

        tooltip.add(Text.literal("Gems Applied:").formatted(Formatting.GRAY));
        for (NbtElement element : gems) {
            String gem = element.asString();
            tooltip.add(Text.literal(gem).formatted(getGemColor(gem)));
        }
    }

    private void appendEnchantmentInfo(ItemStack stack, List<Text> tooltip) {
        if (!stack.hasEnchantments()) return;

        if (Screen.hasShiftDown()) {
            for (NbtElement element : stack.getEnchantments()) {
                if (!(element instanceof NbtCompound tag)) continue;
                String idStr = tag.getString("id");
                Text cached = ENCHANTMENT_DESC_CACHE.computeIfAbsent(idStr, this::buildEnchantmentDescription);
                if (cached != null) tooltip.add(cached);
            }
        } else {
            tooltip.add(Text.literal("Hold SHIFT for more info").formatted(Formatting.DARK_GRAY));
        }
    }

    private Text buildEnchantmentDescription(String idStr) {
        Identifier id = Identifier.tryParse(idStr);
        if (id == null) return null;
        String key = "enchantment." + id.getNamespace() + "." + id.getPath() + ".desc";
        return I18n.hasTranslation(key)
                ? Text.translatable(key).formatted(Formatting.GRAY)
                : null;
    }

    private Formatting getGemColor(String gem) {
        return GEM_COLORS.getOrDefault(gem.toLowerCase(), Formatting.GRAY);
    }
}
