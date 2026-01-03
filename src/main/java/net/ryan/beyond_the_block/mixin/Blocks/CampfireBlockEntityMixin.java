package net.ryan.beyond_the_block.mixin.Blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {

    @Inject(
            method = "litServerTick",
            at = @At("TAIL")
    )
    private static void beyond_the_block$campfireAura(
            World world,
            BlockPos pos,
            BlockState state,
            CampfireBlockEntity campfire,
            CallbackInfo ci
    ) {
        if (world.isClient()) return;

        /* --------------------
           Player regeneration
        --------------------- */
        Box regenBox = new Box(pos).expand(8);
        List<PlayerEntity> players = world.getEntitiesByClass(
                PlayerEntity.class,
                regenBox,
                p -> !p.isSpectator()
        );

        for (PlayerEntity player : players) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.REGENERATION,
                    40, // 2 seconds, refreshed
                    0,
                    true,
                    false,
                    true
            ));
        }

        /* --------------------
           Burn hostile mobs
        --------------------- */
        Box burnBox = new Box(pos).expand(30);
        List<HostileEntity> hostiles = world.getEntitiesByClass(
                HostileEntity.class,
                burnBox,
                e -> !e.isFireImmune()
        );

        for (HostileEntity hostile : hostiles) {
            hostile.setOnFireFor(2);
        }
    }
}


