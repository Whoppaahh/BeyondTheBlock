package net.ryan.beyond_the_block.content.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public final class FreezeRenderUtil {
    private FreezeRenderUtil() {}

    public static int getCrackLevel(LivingEntity entity) {
        float healthRatio = entity.getHealth() / entity.getMaxHealth();
        return (healthRatio < 0.25f) ? 3 :
                (healthRatio < 0.50f) ? 2 :
                        (healthRatio < 0.75f) ? 1 : 0;
    }

    public static Identifier getFreezeTexture(LivingEntity entity) {
        return new Identifier("minecraft", "textures/block/frosted_ice_" + getCrackLevel(entity) + ".png");
    }

    public static float getFreezeAlpha() {
        return 0.7f;
    }
}