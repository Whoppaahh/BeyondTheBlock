package net.ryan.beyond_the_block.mixin.Accessors;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceAccessor {
    @Accessor("inventory") // or "invStackList" depending on mapping
    DefaultedList<ItemStack> getInvStackList();
}

