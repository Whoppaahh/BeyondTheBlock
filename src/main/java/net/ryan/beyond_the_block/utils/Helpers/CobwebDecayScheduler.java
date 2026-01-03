package net.ryan.beyond_the_block.utils.Helpers;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickPriority;
import net.minecraft.world.tick.OrderedTick;
import net.ryan.beyond_the_block.config.ModConfig;

public final class CobwebDecayScheduler {

    private CobwebDecayScheduler() {}

    public static void schedule(ServerWorld world, BlockPos pos) {
        ModConfig cfg = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        int min = cfg.webConfig.minDecayTicks;
        int max = cfg.webConfig.maxDecayTicks;

        int delay = min + world.random.nextInt(max - min + 1);

        long triggerTick = world.getTime() + delay;
        long subTick = world.getTime();

        OrderedTick<Block> tick = new OrderedTick<>(
                Blocks.COBWEB,
                pos,
                triggerTick,
                TickPriority.NORMAL,
                subTick
        );

        world.getBlockTickScheduler().scheduleTick(tick);
    }
}

