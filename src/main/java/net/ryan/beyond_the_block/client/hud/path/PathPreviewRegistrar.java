package net.ryan.beyond_the_block.client.hud.path;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.feature.paths.PathPreviewState;
import net.ryan.beyond_the_block.feature.paths.client.PathPreviewController;

import static net.ryan.beyond_the_block.client.render.layer.BlockRenderLayerRegistrar.attemptRegisterModOres;
import static net.ryan.beyond_the_block.client.render.layer.BlockRenderLayerRegistrar.modOresLayersRegistered;

public class PathPreviewRegistrar {
    public static void register(){
        // Backup tick retry (stops after success)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!modOresLayersRegistered) attemptRegisterModOres();
            PathPreviewController.updatePathPreview(client);
        });

        WorldRenderEvents.AFTER_ENTITIES.register((context) -> {
            if (!PathPreviewState.hasPreview()) return;
            if (!Configs.client().hud.paths.previewMode) return;
            if (!Configs.syncedServerConfig().pathsEnabled) return;

            PathPreviewRenderer.renderPathPreview(context.matrixStack());
        });
    }
}
