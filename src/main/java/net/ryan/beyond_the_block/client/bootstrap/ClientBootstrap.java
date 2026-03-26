package net.ryan.beyond_the_block.client.bootstrap;

import net.ryan.beyond_the_block.client.hud.ClientHudRegistrar;
import net.ryan.beyond_the_block.client.networking.ClientNetworkingRegistrar;
import net.ryan.beyond_the_block.client.render.ClientItemRenderRegistrar;
import net.ryan.beyond_the_block.client.render.ClientParticleRegistrar;
import net.ryan.beyond_the_block.client.render.ClientRenderRegistrar;
import net.ryan.beyond_the_block.client.tooltip.ClientTooltipRegistrar;

public class ClientBootstrap {
    public static void init(){
        ClientHudRegistrar.register();
        ClientNetworkingRegistrar.register();
        ClientItemRenderRegistrar.register();
        ClientParticleRegistrar.register();
        ClientRenderRegistrar.register();
        ClientTooltipRegistrar.register();
    }
}
