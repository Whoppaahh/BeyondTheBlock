package net.ryan.beyond_the_block.content.entity;

import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.seasonal.HolidayManager;

public class BatGoal extends ActiveTargetGoal<PlayerEntity> {
    public BatGoal(BatEntity bat) {
        super(bat, PlayerEntity.class, true);
    }

    @Override
    public boolean canStart() {
        return HolidayManager.Holiday.HALLOWEEN.isActive() && super.canStart();
    }
}
