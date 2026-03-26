package net.ryan.beyond_the_block.client.hud.path;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.feature.paths.PathPreviewState;
import net.ryan.beyond_the_block.feature.paths.client.PathPreviewController;

public class PathPreviewRegistrar {
    public static void register(){
        // Backup tick retry (stops after success)
        ClientTickEvents.END_CLIENT_TICK.register(PathPreviewController::updatePathPreview);

        WorldRenderEvents.AFTER_ENTITIES.register((context) -> {
            if(PathPreviewState.hasPreview() && Configs.client().hud.paths.previewMode){
                PathPreviewRenderer.renderPathPreview(context.matrixStack());
            }
        });
    }
}
