package net.ryan.beyond_the_block.mixin.Blocks;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.ryan.beyond_the_block.config.ModConfig;
import net.ryan.beyond_the_block.utils.Helpers.CobwebDecayScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Inject(
            method = "scheduledTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void btw_handleCobwebDecay(
            BlockState state,
            ServerWorld world,
            BlockPos pos,
            Random random,
            CallbackInfo ci
    ) {
        if (!state.isOf(Blocks.COBWEB)) return;

        ModConfig cfg = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        int light = world.getLightLevel(pos);
        float chance = cfg.webConfig.baseDecayChance;

        if (light > 11)
            chance += cfg.webConfig.lightDecayBonus;
        if (light < 6)
            chance -= cfg.webConfig.darknessDecayReduction;

        chance = MathHelper.clamp(chance, 0.05f, 0.95f);

        if (random.nextFloat() < chance) {
            world.breakBlock(pos, false);
            ci.cancel();
            return;
        }

        CobwebDecayScheduler.schedule(world, pos);
        ci.cancel();
    }

}


