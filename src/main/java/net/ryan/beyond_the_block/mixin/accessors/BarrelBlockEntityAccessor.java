package net.ryan.beyond_the_block.mixin.accessors;

import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BarrelBlockEntity.class)
public interface BarrelBlockEntityAccessor {
    @Accessor("inventory") // or "invStackList" depending on mapping
    DefaultedList<ItemStack> getInvStackList();
}
