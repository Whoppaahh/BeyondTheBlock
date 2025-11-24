package net.ryan.beyond_the_block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class SpiderCobwebTrailGoal extends Goal {
    private final SpiderEntity spider;
    private final Random random = new Random();
    private BlockPos lastPos = null;
    public static final Set<BlockPos> spiderPlacedCobwebs = new HashSet<>();


    public SpiderCobwebTrailGoal(SpiderEntity spider) {
        this.spider = spider;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return true; // always active
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

    @Override
    public void tick() {
        BlockPos currentPos = spider.getBlockPos();

        if (!currentPos.equals(lastPos)) {
            lastPos = currentPos;

            if (random.nextDouble() < 0.3) { // 30% chance per block
                synchronized (spiderPlacedCobwebs) {
                    if (spider.world.isAir(currentPos)) {
                        spider.world.setBlockState(currentPos, Blocks.COBWEB.getDefaultState());
                        spiderPlacedCobwebs.add(currentPos);
                    }
                }
            }
        }
    }

    public static void decayCobwebs(ServerWorld world) {
        int lifetimeTicks = 100 + new Random().nextInt(301);
        double perTickChance = 1.0 / lifetimeTicks;
        synchronized (spiderPlacedCobwebs) {
            Iterator<BlockPos> iterator = spiderPlacedCobwebs.iterator();
            while (iterator.hasNext()) {
                BlockPos pos = iterator.next();

                // 1% chance per tick to disappear
                if (world.getBlockState(pos).isOf(Blocks.COBWEB) && Math.random() < perTickChance) {
                    world.removeBlock(pos, false);

                    // 10% chance to drop string
                    if (Math.random() < 0.1) {
                        Block.dropStack(world, pos, new ItemStack(Items.STRING));
                    }

                    iterator.remove();
                }
            }
        }
    }
}
