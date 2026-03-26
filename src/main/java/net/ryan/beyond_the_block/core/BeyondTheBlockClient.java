package net.ryan.beyond_the_block.core;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.ryan.beyond_the_block.content.block.Entity.DyedWaterCauldronBlockEntity;
import net.ryan.beyond_the_block.content.block.Entity.ModBlockEntities;
import net.ryan.beyond_the_block.content.block.Entity.Render.*;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.effect.Beneficial.ClarityEffect;
import net.ryan.beyond_the_block.content.effect.FreezeEffectLayer;
import net.ryan.beyond_the_block.content.enchantment.Armour.boots.LeapOfFaithEnchantment;
import net.ryan.beyond_the_block.content.entity.ModEntities;
import net.ryan.beyond_the_block.content.entity.Render.CobwebProjectileRenderer;
import net.ryan.beyond_the_block.content.entity.Render.WitherSkeletonHorseRenderer;
import net.ryan.beyond_the_block.content.entity.Render.WitherZombieHorseRenderer;
import net.ryan.beyond_the_block.content.entity.Render.WitherZombieRenderer;
import net.ryan.beyond_the_block.content.item.AnimatedBlock.AnimatedBlockItemRenderer;
import net.ryan.beyond_the_block.content.item.AnimatedItem.AnimatedItem;
import net.ryan.beyond_the_block.content.item.AnimatedItem.AnimatedItemRenderer;
import net.ryan.beyond_the_block.content.item.Armour.ModArmourMaterials;
import net.ryan.beyond_the_block.content.item.CupidArrowEntityRenderer;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.network.ClientNetworking;
import net.ryan.beyond_the_block.content.particle.*;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;
import net.ryan.beyond_the_block.screen.Screens.*;
import net.ryan.beyond_the_block.utils.GUI.FloatingXPManager;
import net.ryan.beyond_the_block.utils.GUI.PlayerHeadManager;
import net.ryan.beyond_the_block.utils.GUI.TrajectoryRenderer;
import net.ryan.beyond_the_block.utils.Helpers.HighlightTracker;
import net.ryan.beyond_the_block.feature.paths.PathPreviewState;
import net.ryan.beyond_the_block.feature.paths.PathToolHelper;
import net.ryan.beyond_the_block.utils.OutlineRenderer;
import net.ryan.beyond_the_block.village.GuardVillager.Render.GuardEntityRenderer;
import net.ryan.beyond_the_block.village.ModVillagers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeyondTheBlockClient implements ClientModInitializer {

    public static final String MOD_ID = "beyond_the_block";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final Map<String, Text> ENCHANTMENT_DESC_CACHE = new HashMap<>();
    private static final Map<String, Formatting> GEM_COLORS = Map.ofEntries(
            Map.entry("miranite", Formatting.GREEN),
            Map.entry("chromite", Formatting.LIGHT_PURPLE),
            Map.entry("nocturnite", Formatting.DARK_PURPLE),
            Map.entry("amberine", Formatting.GOLD),
            Map.entry("azuros", Formatting.AQUA),
            Map.entry("indigra", Formatting.BLUE),
            Map.entry("rosette", Formatting.RED)
    );

    private boolean modOresLayersRegistered = false;
    // New tag for automatic ore registration
    private static final TagKey<Block> MOD_ORES =
            TagKey.of(Registry.BLOCK.getKey(), new Identifier(BeyondTheBlock.MOD_ID, "mod_ores"));


    @Override
    public void onInitializeClient() {
        WorldRenderEvents.BEFORE_ENTITIES.register(TrajectoryRenderer::render);
        registerBlockEntities();
        registerGeoItems();
        registerParticles();
        registerScreens();
        registerClientTickEvents();
        registerEntityRenderers();
        registerBlockRenderLayers();
        registerDynamicLights();
        registerLivingEntityFeatures();
        registerTooltips();

        FloatingXPManager.register();
        OutlineRenderer.init();
    }


    private void registerBlockEntities() {
        BlockEntityRendererFactories.register(ModBlockEntities.PEDESTAL_BLOCK_ENTITY, PedestalBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHRINE_CORE_BLOCK_ENTITY, ShrineCoreBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHRINE_HEADS_BLOCK_ENTITY, ShrineHeadsBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHRINE_DECOR_BLOCK_ENTITY, ShrineDecorBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.PLAYER_VAULT_BLOCK_ENTITY, PlayerVaultBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SINGLE_INPUT_BLOCK_ENTITY, SingleInputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.DOUBLE_INPUT_BLOCK_ENTITY, DoubleInputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.ANIMATED_BLOCK_ENTITY, AnimatedBlockRenderer::new);

    }

    private void registerGeoItems() {
        GeckoLib.initialize();
        GeoItemRenderer.registerItemRenderer(ModItems.ANIMATED_ITEM, new AnimatedItemRenderer());
        GeoItemRenderer.registerItemRenderer(ModItems.ANIMATED_BLOCK_ITEM, new AnimatedBlockItemRenderer());
    }

    private void registerParticles() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(ModParticles.ROSETTE_PARTICLE, RosetteParticle.Factory::new);
        registry.register(ModParticles.AMBERINE_PARTICLE, AmberineParticle.Factory::new);
        registry.register(ModParticles.RUBY_PARTICLE, RubyParticle.Factory::new);
        registry.register(ModParticles.AZUROS_PARTICLE, AzurosParticle.Factory::new);
        registry.register(ModParticles.CHROMITE_PARTICLE, ChromiteParticle.Factory::new);
        registry.register(ModParticles.MIRANITE_PARTICLE, MiraniteParticle.Factory::new);
        registry.register(ModParticles.NOCTURNITE_PARTICLE, NocturniteParticle.Factory::new);
        registry.register(ModParticles.INDIGRA_PARTICLE, IndigraParticle.Factory::new);
        registry.register(ModParticles.BLEED_SWEEP_PARTICLE, BleedSweepParticle.Factory::new);
    }

    @SuppressWarnings("unchecked")
    private void registerLivingEntityFeatures() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, renderer, helper, context) -> {
                    LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer =
                            (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) renderer;
                    helper.register(new FreezeEffectLayer<>(livingRenderer, livingRenderer.getModel()));
                }
        );
    }

    private void registerScreens() {
        HandledScreens.register(ModScreenHandlers.TROWEL_SCREEN_HANDLER, TrowelScreen::new);
        HandledScreens.register(ModScreenHandlers.GEM_SCREEN_HANDLER, GemScreen::new);
        HandledScreens.register(ModScreenHandlers.GUARD_SCREEN_HANDLER, GuardVillagerScreen::new);
        HandledScreens.register(ModScreenHandlers.RIDDLE_CORE_SCREEN_HANDLER, RiddleCoreScreen::new);
        HandledScreens.register(ModScreenHandlers.STAFF_SCREEN_HANDLER, StaffScreen::new);
        HandledScreens.register(ModScreenHandlers.PLAYER_VAULT_SCREEN_HANDLER, PlayerVaultScreen::new);
        HandledScreens.register(ModScreenHandlers.INFI_SCREEN_HANDLER, InfiFurnaceScreen::new);
        HandledScreens.register(ModScreenHandlers.DECRAFTER_SCREEN_HANDLER, DecrafterScreen::new);
    }

    private void registerClientTickEvents() {
        ClientNetworking.registerS2CPackets();
        ClientTickEvents.END_CLIENT_TICK.register(ClarityEffect::tickHandler);
        ClientTickEvents.END_CLIENT_TICK.register(AnimatedItem::handleLeftClick);
        ClientTickEvents.END_CLIENT_TICK.register(LeapOfFaithEnchantment::handleJumpPress);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerHeadManager.tick();
            if (client.player != null) HighlightTracker.updateHighlights(client.player);
        });
    }

    private void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.WITHER_ZOMBIE, WitherZombieRenderer::new);
        EntityRendererRegistry.register(ModEntities.WITHER_ZOMBIE_HORSE, WitherZombieHorseRenderer::new);
        EntityRendererRegistry.register(ModEntities.WITHER_SKELETON_HORSE, WitherSkeletonHorseRenderer::new);
        EntityRendererRegistry.register(ModVillagers.GUARD_VILLAGER, GuardEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.COBWEB_PROJECTILE, CobwebProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.CUPID_ARROW, CupidArrowEntityRenderer::new);
    }

    /* ------------------------------- TAG-DRIVEN RENDER LAYERS ------------------------------- */

    private void registerBlockRenderLayers() {
        // Manual translucency
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WATER_TORCH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_WATER_TORCH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SPEED_RAIL_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LAVA_LAMP_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MODDED_FLUID_CAULDRON_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DYED_WATER_CAULDRON_BLOCK, RenderLayer.getTranslucent());

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world != null && pos != null && world.getBlockEntity(pos) instanceof DyedWaterCauldronBlockEntity be) {
                return be.getColor();
            }
            return 0x3F76E4; // default water tint
        }, ModBlocks.DYED_WATER_CAULDRON_BLOCK);


        // Backup tick retry (stops after success)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!modOresLayersRegistered) attemptRegisterModOres();
            updatePathPreview(client);
        });

        WorldRenderEvents.AFTER_ENTITIES.register((context) -> {
            if(PathPreviewState.hasPreview() && Configs.client().hud.paths.previewMode){
                renderPathPreview(context.matrixStack());
            }
        });
    }

    private void renderPathPreview(MatrixStack matrices) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();

        VertexConsumerProvider.Immediate provider = client.getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer consumer = provider.getBuffer(RenderLayer.getLines());

        for (BlockPos pos : PathPreviewState.getPositions()) {
            double x = pos.getX() - camPos.x;
            double y = pos.getY() - camPos.y;
            double z = pos.getZ() - camPos.z;

            Box box = new Box(x, y, z, x + 1, y + 1, z + 1);

            WorldRenderer.drawBox(
                    matrices,
                    consumer,
                    box,
                    0f, 1f, 0f, 1f // RGBA - bright green
            );
        }

        provider.draw(); // flush to GPU
    }



    private void updatePathPreview(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            PathPreviewState.clear();
            return;
        }

        ItemStack stack = client.player.getMainHandStack();
        if (!(stack.getItem() instanceof ShovelItem)) {
            PathPreviewState.clear();
            return;
        }

        // Must have a starting point set
        if (!PathToolHelper.hasStart(stack)) {
            PathPreviewState.clear();
            return;
        }

        // Must be looking at a block
        if (!(client.crosshairTarget instanceof BlockHitResult hit)) {
            PathPreviewState.clear();
            return;
        }

        BlockPos start = PathToolHelper.getStart(stack);
        BlockPos end = hit.getBlockPos();

        // Too far? No preview
        if (!PathToolHelper.withinMaxDistance(start, end,  Configs.server().features.paths.maxDistance)) {
            PathPreviewState.clear();
            return;
        }

        // Compute width
        int width = PathToolHelper.getWidth(stack, Configs.server());

        // Compute line & widened area
        List<BlockPos> centerLine = PathToolHelper.computeLine2D(start, end);
        var direction = PathToolHelper.getPrimaryDirection(start, end);
        List<BlockPos> full = PathToolHelper.widenLine(centerLine, width, direction);

        // Terrain-follow for preview
        List<BlockPos> adjusted = full.stream()
                .map(pos -> PathToolHelper.adjustToTerrain(client.world, pos,  Configs.server().features.paths.useTerrainFollowing))
                .toList();

        PathPreviewState.setPositions(adjusted);
    }

    private void attemptRegisterModOres() {
        if (modOresLayersRegistered) return;

        Registry.BLOCK.getEntryList(MOD_ORES).ifPresent(entryList -> {
            int count = 0;
            for (RegistryEntry<Block> entry : entryList) {
                Block block = entry.value();
                BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
                count++;
            }

            if (count > 0) {
                modOresLayersRegistered = true;
                LOGGER.info("✅ Registered {} mod ores for Cutout render layer from beyond_the_block:mod_ores", count);
            } else {
                LOGGER.warn("⚠️ Tag beyond_the_block:mod_ores was found but empty");
            }
        });
    }
    private void registerDynamicLights() {
        DynamicLightHandlers.registerDynamicLightHandler(EntityType.PLAYER, player -> {
            int maxLight = 0;

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR && isAmberineArmor(player.getEquippedStack(slot))) {
                    maxLight = 15;
                }
            }

            if (isAmberineItem(player.getMainHandStack()) || isAmberineItem(player.getOffHandStack())) {
                maxLight = 15;
            }

            return maxLight;
        });
    }

    private boolean isAmberineArmor(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ArmorItem armor
                && armor.getMaterial() == ModArmourMaterials.AMBERINE;
    }

    private boolean isAmberineToolOrWeapon(ItemStack stack) {
        return stack != null && (stack.isOf(ModItems.AMBERINE_SWORD) ||
                stack.isOf(ModItems.AMBERINE_PICKAXE) ||
                stack.isOf(ModItems.AMBERINE_AXE) ||
                stack.isOf(ModItems.AMBERINE_SHOVEL) ||
                stack.isOf(ModItems.AMBERINE_HOE));
    }

    private boolean isAmberineItem(ItemStack stack) {
        return stack != null && (isAmberineArmor(stack) || isAmberineToolOrWeapon(stack)
                || stack.isOf(ModBlocks.AMBERINE_BLOCK.asItem()) || stack.isOf(ModItems.AMBERINE_ITEM));
    }


    // --------------------- Tooltip logic ---------------------
    private void registerTooltips() {
        ItemTooltipCallback.EVENT.register(this::handleTooltip);
    }

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
