package net.ryan.beyond_the_block.content.block.shrine;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.ShrineHeadsBlockEntity;
import net.ryan.beyond_the_block.content.registry.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShrineHeadsBlock extends AbstractShrineBlock implements BlockEntityProvider {

    public static final EnumProperty<Direction> FACING = EnumProperty.of("facing", Direction.class);

    public ShrineHeadsBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH)); // Default to facing north
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("A vessel for the spirits of fallen players.").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Automatically collects player heads and delivers riddles.").formatted(Formatting.GOLD));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // Get the player's direction and set the facing property accordingly
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ModBlockEntities.SHRINE_HEADS_BLOCK_ENTITY, ShrineHeadsBlockEntity::tick);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }


    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ShrineHeadsBlockEntity(pos, state);
    }



}

