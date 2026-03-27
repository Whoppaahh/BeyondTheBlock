package net.ryan.beyond_the_block.core;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.ryan.beyond_the_block.config.schema.ConfigClient;
import net.ryan.beyond_the_block.config.schema.ConfigServer;
import net.ryan.beyond_the_block.config.sync.ServerConfigSync;
import net.ryan.beyond_the_block.core.bootstrap.CommonBootstrap;
import net.ryan.beyond_the_block.feature.naming.NameLoader;
import net.ryan.beyond_the_block.utils.Helpers.ServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class BeyondTheBlock implements ModInitializer {

    public static final String MOD_ID = "beyond_the_block";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    public static Hand getHandWith(LivingEntity entity, Predicate<Item> predicate) {
        return predicate.test(entity.getMainHandStack().getItem()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Beyond The Block...");
        setupConfig();
        CommonBootstrap.init();
        ServerContext.init();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerConfigSync.sendToPlayer(handler.getPlayer());
        });

        LOGGER.info("Beyond The Block initialized successfully!");
    }

    // --------------------- Configuration ---------------------

    private void setupConfig() {
        AutoConfig.register(ConfigClient.class, JanksonConfigSerializer::new);
        AutoConfig.register(ConfigServer.class, JanksonConfigSerializer::new);
        //holder.registerSaveListener((h, c) -> { c.validateConfig(); return ActionResult.SUCCESS; });
        //holder.registerLoadListener((h, c) -> { c.validateConfig(); return ActionResult.SUCCESS; });
        ServerLifecycleEvents.SERVER_STARTED.register(NameLoader::load);
    }
}
