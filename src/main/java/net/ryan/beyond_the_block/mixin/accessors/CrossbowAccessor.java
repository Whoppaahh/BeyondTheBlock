package net.ryan.beyond_the_block.mixin.accessors;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CrossbowItem.class)
public interface CrossbowAccessor {
    @Invoker("shootAll")
    static void callShootAll(
            World world,
            LivingEntity shooter,
            Hand hand,
            ItemStack crossbow,
            float speed,
            float divergence
    ) {

    }
}
