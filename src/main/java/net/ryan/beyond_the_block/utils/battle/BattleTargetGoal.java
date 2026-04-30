package net.ryan.beyond_the_block.utils.battle;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;

public class BattleTargetGoal extends Goal {
    private final MobEntity mob;
    private final LivingEntity target;

    public BattleTargetGoal(MobEntity mob, LivingEntity target) {
        this.mob = mob;
        this.target = target;
    }

    @Override
    public boolean canStart() {
        return target.isAlive();
    }

    @Override
    public boolean shouldContinue() {
        return target.isAlive();
    }

    @Override
    public void start() {
        mob.setTarget(target);
    }

    @Override
    public void tick() {
        mob.setTarget(target);
        mob.getNavigation().startMovingTo(target, 1.2D);
    }
}