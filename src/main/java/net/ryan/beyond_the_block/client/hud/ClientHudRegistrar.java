package net.ryan.beyond_the_block.client.hud;

import net.ryan.beyond_the_block.client.hud.path.PathPreviewRegistrar;

public class ClientHudRegistrar {
    public static void register(){

        FloatingXPManager.register();
        PathPreviewRegistrar.register();
    }
}
