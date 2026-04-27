package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.feature.pets.PetHomeAccessor;

public class PetBedBlock extends Block {

    public PetBedBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);

        if (world.isClient) return;
        if (!(world instanceof ServerWorld)) return;
        if (!(entity instanceof TameableEntity pet)) return;
        if (!pet.isTamed()) return;
        if (!(pet instanceof PetHomeAccessor homeAccessor)) return;

        if (homeAccessor.btb$hasPetHome() && pos.equals(homeAccessor.btb$getPetHomePos())) {
            return;
        }

        homeAccessor.btb$setPetHomePos(pos);

        if (pet.getOwner() instanceof PlayerEntity owner) {
            BeyondTheBlock.LOGGER.info("{}'s home has been set.", pet.getName().getString());
            owner.sendMessage(
                    Text.literal(pet.getName().getString() + "'s home has been set."),
                    true
            );
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return super.getCollisionShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return super.getOutlineShape(state, world, pos, context);
    }
}