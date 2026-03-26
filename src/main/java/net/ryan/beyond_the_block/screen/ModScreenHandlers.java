package net.ryan.beyond_the_block.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.screen.handler.*;
import net.ryan.beyond_the_block.screen.handler.Guard.GuardVillagerScreenHandler;

public class ModScreenHandlers {

    public static final ScreenHandlerType<GemScreenHandler> GEM_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(GemScreenHandler::new);

    public static final ScreenHandlerType<TrowelScreenHandler> TROWEL_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(TrowelScreenHandler::new);

    public static final ScreenHandlerType<InfiFurnaceScreenHandler> INFI_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(InfiFurnaceScreenHandler::new);

    public static final ScreenHandlerType<GuardVillagerScreenHandler> GUARD_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(GuardVillagerScreenHandler::new);

    public static final ScreenHandlerType<RiddleCoreScreenHandler> RIDDLE_CORE_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(RiddleCoreScreenHandler::new);

    public static final ScreenHandlerType<PlayerVaultScreenHandler> PLAYER_VAULT_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(PlayerVaultScreenHandler::new);

    public static final ScreenHandlerType<StaffScreenHandler> STAFF_SCREEN_HANDLER =
            new ScreenHandlerType<>(StaffScreenHandler::new);

    public static final ScreenHandlerType<DecrafterScreenHandler> DECRAFTER_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(DecrafterScreenHandler::new);

    public static void registerScreenHandlers() {
        Registry.register(Registry.SCREEN_HANDLER, new Identifier("guard_screen"), GUARD_SCREEN_HANDLER);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier("gem_screen"), GEM_SCREEN_HANDLER);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier("riddle_core_screen"), RIDDLE_CORE_SCREEN_HANDLER);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier("player_vault_screen"), PLAYER_VAULT_SCREEN_HANDLER);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier("staff_screen"), STAFF_SCREEN_HANDLER);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier("infi_furnace_screen"), INFI_SCREEN_HANDLER);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier("decrafter_screen"), DECRAFTER_SCREEN_HANDLER);

        Registry.register(Registry.SCREEN_HANDLER, new Identifier("trowel_screen"), TROWEL_SCREEN_HANDLER);
    }
}
