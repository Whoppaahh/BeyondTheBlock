package net.ryan.beyond_the_block.mixin.entity;

import net.minecraft.block.entity.BannerBlockEntity;
import net.ryan.beyond_the_block.client.visual.Glowable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityMixin implements Glowable {

    @Unique
    private boolean btb_glow = false;

    public boolean bt$isGlowing() {
        return btb_glow;
    }

    public void bt$setGlowing(boolean glow) {
        btb_glow = glow;
    }
}