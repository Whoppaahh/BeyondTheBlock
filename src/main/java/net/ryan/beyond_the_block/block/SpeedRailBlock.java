package net.ryan.beyond_the_block.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MinecartItem;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.utils.Accessors.MinecartSpeedAccessor;
import net.ryan.beyond_the_block.utils.Helpers.SpeedRailValues;

public class SpeedRailBlock extends PoweredRailBlock {

    public static final IntProperty SPEED_LEVEL =
            IntProperty.of("speed", 0, 4);

    public SpeedRailBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.getStateManager().getDefaultState()
                        .with(POWERED, false)
                        .with(SHAPE, RailShape.NORTH_SOUTH)
                        .with(SPEED_LEVEL, 1)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SPEED_LEVEL);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx)
                .with(SPEED_LEVEL, 1);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {

        ItemStack stack = player.getStackInHand(hand);

        // Preserve vanilla minecart placement
        if (stack.getItem() instanceof MinecartItem) {
            return super.onUse(state, world, pos, player, hand, hit);
        }

        if (!world.isClient) {
            int next;
            if (player.isSneaking()) {
                next = 0;
            } else {
                next = (state.get(SPEED_LEVEL) + 1) % 5;
            }

            world.setBlockState(
                    pos,
                    state.with(SPEED_LEVEL, next),
                    Block.NOTIFY_ALL
            );


            player.sendMessage(
                    Text.literal(SpeedRailValues.DISPLAY[next]),
                    true
            );
        }

        return ActionResult.SUCCESS;
    }


}
