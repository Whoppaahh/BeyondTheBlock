package net.ryan.beyond_the_block.entity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.SlimeEntity;

import java.util.EnumSet;
import java.util.List;

public class SlimeMergeGoal extends Goal {
    private final SlimeEntity slime;
    private SlimeEntity target;

    public SlimeMergeGoal(SlimeEntity slime) {
        this.slime = slime;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (slime.getSize() >= 4 || slime.isAiDisabled()) return false;

        List<SlimeEntity> nearby = slime.world.getEntitiesByClass(
                SlimeEntity.class,
                slime.getBoundingBox().expand(8.0),
                s -> s != slime && s.getSize() == slime.getSize() && !s.isRemoved()
        );

        if (!nearby.isEmpty()) {
            target = nearby.get(0);
            return true;
        }

        return false;
    }

    @Override
    public void start() {
        slime.getNavigation().startMovingTo(target, 1.0);
    }

    @Override
    public boolean shouldContinue() {
        return target != null &&
                !target.isRemoved() &&
                slime.squaredDistanceTo(target) > 1.0 &&
                slime.getSize() < 4;
    }

    @Override
    public void stop() {
        target = null;
        slime.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (target != null) {
            slime.getNavigation().startMovingTo(target, 1.0);
        }
    }
}
