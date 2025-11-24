package net.ryan.beyond_the_block.block.Shrine;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.block.Entity.ShrineEntity.ShrineDecorBlockEntity;
import net.ryan.beyond_the_block.block.ModBlocks;
import net.ryan.beyond_the_block.item.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShrineDecorBlock extends BlockWithEntity implements BlockEntityProvider {

    private final List<Item> rewards = new ArrayList<>();
    private final Random random = new Random();
    private static final VoxelShape SHAPE =
            Block.createCuboidShape(2, 0, 2, 14, 13, 14);

    public ShrineDecorBlock(Settings settings) {
        super(settings);
        rewards.add(ModItems.ASTRACINDER);
        rewards.add(ModItems.XENOLITH_STAFF);
        rewards.add(ModItems.NOCTURNITE_ITEM);
        rewards.add(ModBlocks.RAW_AZUROS_BLOCK.asItem());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ShrineDecorBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof ShrineDecorBlockEntity shrineDecorBlockEntity) {

                ItemStack heldItem = player.getStackInHand(hand);

                if (heldItem.getItem().equals(ModItems.RUBY_ITEM)) {
                    int currentCount = shrineDecorBlockEntity.getUseCount();

                    if (currentCount < 2) {
                        shrineDecorBlockEntity.incrementUseCount();
                        shrineDecorBlockEntity.markDirty();
                        world.updateListeners(pos, state, state, 3);

                        ParticleEffect particle = switch (currentCount) {
                            case 0 -> ParticleTypes.HAPPY_VILLAGER;
                            case 1 -> ParticleTypes.ENCHANT;
                            default -> ParticleTypes.SMOKE;
                        };
                        if(!world.isClient) {
                            ((ServerWorld) world).spawnParticles(particle, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 10, 0.2, 0.3, 0.2, 0.01);
                        }
                    } else {
                        // Give reward
                        ItemStack reward = rewards.get(random.nextInt(rewards.size())).getDefaultStack();
                        if (!player.getInventory().insertStack(reward)) {
                            player.dropItem(reward, false); // drop if inventory is full
                        }
                        if(player instanceof ServerPlayerEntity serverPlayer) {
                            player.currentScreenHandler.sendContentUpdates();
                        }
                        heldItem.decrement(1); // remove ruby
                        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 2f);

                        // Reset count
                        shrineDecorBlockEntity.resetUseCount();
                        shrineDecorBlockEntity.markDirty();
                        world.updateListeners(pos, state, state, 3);

                    }
                    return ActionResult.SUCCESS;
                }
            }
        return ActionResult.PASS;
    }
}

