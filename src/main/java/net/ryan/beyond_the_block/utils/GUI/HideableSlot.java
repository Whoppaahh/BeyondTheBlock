package net.ryan.beyond_the_block.utils.GUI;

import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsScreen;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

import java.util.function.Supplier;

public class HideableSlot extends Slot {
    private final Supplier<SocialInteractionsScreen.Tab> activeTabSupplier;
    private final SocialInteractionsScreen.Tab tab;

    public HideableSlot(Inventory inventory, int index, int x, int y, SocialInteractionsScreen.Tab tab, Supplier<SocialInteractionsScreen.Tab> activeTabSupplier) {
        super(inventory, index, x, y);
        this.tab = tab;
        this.activeTabSupplier = activeTabSupplier;
    }

    @Override
    public boolean isEnabled() {
        return activeTabSupplier.get() == tab;
    }
}
