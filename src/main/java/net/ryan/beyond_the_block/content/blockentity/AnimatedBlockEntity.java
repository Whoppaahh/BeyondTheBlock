package net.ryan.beyond_the_block.content.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.content.registry.ModBlockEntities;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class AnimatedBlockEntity extends BlockEntity implements IAnimatable {
    @SuppressWarnings("removal")
    private final AnimationFactory factory = new AnimationFactory(this);

    public AnimatedBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ANIMATED_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>
                (this, "controller", 0, this::predicate));
    }
    @SuppressWarnings("removal")
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));

        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
