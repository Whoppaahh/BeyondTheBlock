package net.ryan.beyond_the_block.mixin.accessors;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ScreenHandler.class)
public interface ScreenHandlerAccessor {
    @Accessor("slots")
    DefaultedList<Slot> beyond_the_block$getSlots();
}