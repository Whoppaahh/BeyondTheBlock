package net.ryan.beyond_the_block.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.BeyondTheBlock;

public class ModSounds {
    public static SoundEvent PLAYER_VAULT_OPEN = registerSoundEvent("player_vault_open");
    public static SoundEvent RIDDLE_GENERATED = registerSoundEvent("riddle_generated");
    public static SoundEvent RIDDLE_COMPLETE = registerSoundEvent("riddle_complete");


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(BeyondTheBlock.MOD_ID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }

    public static void registerSounds() {
        BeyondTheBlock.LOGGER.info("Registering Mod Sounds for " + BeyondTheBlock.MOD_ID);
    }
}
