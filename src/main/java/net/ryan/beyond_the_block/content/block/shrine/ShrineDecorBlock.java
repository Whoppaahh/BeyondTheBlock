package net.ryan.beyond_the_block.content.block.shrine;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
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
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.blockentity.ShrineDecorBlockEntity;
import net.ryan.beyond_the_block.content.registry.ModBlockEntities;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.content.registry.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ShrineDecorBlock extends AbstractShrineBlock implements BlockEntityProvider {

    private final Random random = new Random();
    private static final VoxelShape SHAPE =
            Block.createCuboidShape(2, 0, 2, 14, 13, 14);

    public ShrineDecorBlock(Settings settings) {
        super(settings);
    }

    private List<ItemStack> getRewards() {
        List<ItemStack> rewardStacks = new ArrayList<>();
        var config = Configs.server().features.shrines;

        for (String id : config.customRewards) {
            Optional<Item> item = Registry.ITEM.getOrEmpty(new net.minecraft.util.Identifier(id));
            item.ifPresent(i -> rewardStacks.add(i.getDefaultStack()));
        }

        if (rewardStacks.isEmpty()) {
            rewardStacks.add(ModItems.ASTRACINDER.getDefaultStack());
        }
        return rewardStacks;
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
            var config = Configs.server().features.shrines;
            Item activationItem = Registry.ITEM.get(new net.minecraft.util.Identifier(config.activationItemId));

            if (heldItem.getItem().equals(activationItem)) {
                int currentCount = shrineDecorBlockEntity.getUseCount();
                int requirement = config.activationCountRequirement;

                // Even if we only need 1 for activation, we still allow up to 'requirement' for rewards
                if (currentCount < requirement) {
                    shrineDecorBlockEntity.incrementUseCount();
                    shrineDecorBlockEntity.markDirty();
                    shrineDecorBlockEntity.sync();
                    
                    int updatedCount = shrineDecorBlockEntity.getUseCount();

                    if (updatedCount < requirement) {
                        ParticleEffect particle = (updatedCount % 2 == 0) ? ParticleTypes.HAPPY_VILLAGER : ParticleTypes.ENCHANT;
                        if(!world.isClient) {
                            ((ServerWorld) world).spawnParticles(particle, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 10, 0.2, 0.3, 0.2, 0.01);
                        }
                        world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1f, 1f + updatedCount * 0.1f);
                        heldItem.decrement(1);
                    } else {
                        // Give reward when reaching requirement
                        List<ItemStack> currentRewards = getRewards();
                        ItemStack reward = currentRewards.get(random.nextInt(currentRewards.size())).copy();
                        if (!player.getInventory().insertStack(reward)) {
                            player.dropItem(reward, false);
                        }
                        if(player instanceof ServerPlayerEntity) {
                            player.currentScreenHandler.sendContentUpdates();
                        }
                        heldItem.decrement(1);
                        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 2f);
                        world.playSound(null, pos, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.BLOCKS, 0.5f, 1.5f);

                        if(!world.isClient) {
                            ((ServerWorld) world).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 20, 0.5, 0.5, 0.5, 0.1);
                        }

                        // Reset count
                        shrineDecorBlockEntity.resetUseCount();
                        shrineDecorBlockEntity.markDirty();
                        shrineDecorBlockEntity.sync();
                    }
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ModBlockEntities.SHRINE_DECOR_BLOCK_ENTITY, ShrineDecorBlockEntity::tick);
    }
}

