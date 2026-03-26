package net.ryan.beyond_the_block.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.utils.Helpers.CobwebDecayScheduler;

public class SpiderCobwebTrailGoal extends Goal {
    private final SpiderEntity spider;
    private int cooldown;

    public SpiderCobwebTrailGoal(SpiderEntity spider) {
        this.spider = spider;
        this.cooldown = 0;
    }

    @Override
    public boolean canStart() {
        return !spider.getWorld().isClient();
    }

    @Override
    public void tick() {
        if (spider.getWorld().isClient()) return;
        if (--cooldown > 0) return;
        cooldown = 20; // once per second

        ServerWorld world = (ServerWorld) spider.getWorld();
        BlockPos pos = spider.getBlockPos().down();

        if (!world.getBlockState(pos).isAir()) return;
        if (!canPlaceWeb(world, pos)) return;

        float chance = spider instanceof CaveSpiderEntity
                ? Configs.server().features.webs.caveSpiderRate
                : Configs.server().features.webs.normalSpiderRate;

        if (world.random.nextFloat() > chance) return;

        world.setBlockState(pos, Blocks.COBWEB.getDefaultState());
        CobwebDecayScheduler.schedule(world, pos);
    }

    private boolean canPlaceWeb(ServerWorld world, BlockPos pos) {
        int radius = Configs.server().features.webs.densityRadius;
        int limit = Configs.server().features.webs.densityLimit;

        int count = 0;
        for (BlockPos p : BlockPos.iterateOutwards(pos, radius, radius, radius)) {
            if (world.getBlockState(p).isOf(Blocks.COBWEB)) {
                if (++count > limit) return false;
            }
        }
        return true;
    }
}
