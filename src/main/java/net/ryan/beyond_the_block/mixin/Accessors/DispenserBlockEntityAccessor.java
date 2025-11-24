package net.ryan.beyond_the_block.mixin.Accessors;

import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DispenserBlockEntity.class)
public interface DispenserBlockEntityAccessor {
    @Accessor("inventory") // or "invStackList" depending on mapping
    DefaultedList<ItemStack> getInvStackList();
}
