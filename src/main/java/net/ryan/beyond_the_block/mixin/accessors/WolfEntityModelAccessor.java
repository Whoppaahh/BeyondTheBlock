package net.ryan.beyond_the_block.mixin.accessors;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WolfEntityModel.class)
public interface WolfEntityModelAccessor {
    @Accessor("head")
    ModelPart btb$getHead();
}
