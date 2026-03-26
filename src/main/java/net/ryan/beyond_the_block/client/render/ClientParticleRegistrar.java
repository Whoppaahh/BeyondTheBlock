package net.ryan.beyond_the_block.client.render;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.ryan.beyond_the_block.content.particle.*;

public class ClientParticleRegistrar {
    public static void register() {
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
}
