package net.ryan.beyond_the_block.mixin.accessors;

import net.minecraft.util.SignType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SignType.class)
public interface SignTypeAccessor {
    @Invoker("register")
    static SignType btb$register(SignType type) {
        throw new AssertionError();
    }
}
