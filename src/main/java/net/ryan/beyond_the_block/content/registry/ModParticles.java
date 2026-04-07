package net.ryan.beyond_the_block.content.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class ModParticles {
    public static final DefaultParticleType AMBERINE_PARTICLE = registerParticles("amberine_particle", FabricParticleTypes.simple());
    public static final DefaultParticleType RUBY_PARTICLE = registerParticles("ruby_particle", FabricParticleTypes.simple());
    public static final DefaultParticleType AZUROS_PARTICLE = registerParticles("azuros_particle", FabricParticleTypes.simple());
    public static final DefaultParticleType ROSETTE_PARTICLE = registerParticles("rosette_particle", FabricParticleTypes.simple());
    public static final DefaultParticleType CHROMITE_PARTICLE = registerParticles("chromite_particle", FabricParticleTypes.simple());
    public static final DefaultParticleType MIRANITE_PARTICLE = registerParticles("miranite_particle", FabricParticleTypes.simple());
    public static final DefaultParticleType NOCTURNITE_PARTICLE = registerParticles("nocturnite_particle", FabricParticleTypes.simple());
    public static final DefaultParticleType INDIGRA_PARTICLE = registerParticles("indigra_particle", FabricParticleTypes.simple());
    public static final DefaultParticleType BLEED_SWEEP_PARTICLE = registerParticles("bleed_sweep_particle", FabricParticleTypes.simple());
   // public static final DefaultParticleType FIRE_SLASH_PARTICLE = registerParticles("fire_slash", FabricParticleTypes.simple());

    public static DefaultParticleType registerParticles(String name, DefaultParticleType particle) {
        return Registry.register(Registry.PARTICLE_TYPE, new Identifier(BeyondTheBlock.MOD_ID, name), particle);
    }
    public static void registerModParticles() {
        BeyondTheBlock.LOGGER.info("Registering Mod Particles for " + BeyondTheBlock.MOD_ID);

    }
}
