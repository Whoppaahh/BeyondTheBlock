package net.ryan.beyond_the_block.content.block;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.ryan.beyond_the_block.content.blockentity.PlayerVaultBlockEntity;
import org.jetbrains.annotations.Nullable;

public class PlayerVaultBlock extends BarrelBlock implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;
    private ServerPlayerEntity serverPlayer;

    public PlayerVaultBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
    }

    @Override
    public float getBlastResistance() {
        return 3600000.0F;
    }

    @Override
    public boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }


    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {return new PlayerVaultBlockEntity(pos, state);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PlayerVaultBlockEntity vault) {
                if(serverPlayer == null)return;
                // Check if the player is the owner
                if (vault.isOwner(serverPlayer.getUuid())) {
                    // Create an ItemStack for the vault block
                    ItemStack vaultStack = new ItemStack(this.asItem());

                    // Save the block entity's data into the item
                    NbtCompound nbt = new NbtCompound();
                    vault.writeNbt(nbt);
                    vaultStack.getOrCreateNbt().put("BlockEntityTag", nbt);

                    // Optionally store the owner's name for tooltip display
                    vaultStack.setCustomName(Text.literal(player.getName().getString() + "'s Vault"));

                    // Drop the item with inventory preserved
                    ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), vaultStack);
                } else {
                    // Not owner – scatter items normally
                    super.onBreak(world, pos, state, player);
                }
            }
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
//        BlockEntity be = world.getBlockEntity(pos);
//        if (be instanceof PlayerVaultBlockEntity vault) {
//            if(serverPlayer == null)return;
//            if (vault.isOwner(serverPlayer.getUuid())) {
//                super.onStateReplaced(state, world, pos, newState, moved);
//            }
//        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }else{
            serverPlayer = (ServerPlayerEntity) player;
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof PlayerVaultBlockEntity vault) {
                if (vault.isOwner(player.getUuid()) || vault.canPlayerUse(serverPlayer)) {
                    player.openHandledScreen(vault);
                } else {
                    player.sendMessage(Text.literal("You don't have access to this Personal Vault"), true);
                }
            }
            return ActionResult.CONSUME;
        }
    }


    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PlayerVaultBlockEntity) {
            ((PlayerVaultBlockEntity)blockEntity).tick();
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            if(placer instanceof PlayerEntity player) {
                serverPlayer = (ServerPlayerEntity) player;
                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof PlayerVaultBlockEntity vault) {
                    vault.setOwner(player.getUuid(), player.getName().getString());
                    ItemStack head = createPlayerHeadItem(serverPlayer);
                    vault.getDisplayItem().set(0, head);
                    vault.markDirty();
                    world.updateListeners(pos, state, state, NOTIFY_ALL);
                    //EmeraldEmpire.LOGGER.info(vault.getDisplayItem().toString());
                }
            }
            if(itemStack.hasNbt() && itemStack.getNbt().contains("BlockEntityTag")){
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof PlayerVaultBlockEntity vault) {
                    NbtCompound nbt = itemStack.getNbt().getCompound("BlockEntityTag");
                    vault.readNbt(nbt);
                }
            }
        }

    }

    private ItemStack createPlayerHeadItem(ServerPlayerEntity player) {
        ItemStack head = new ItemStack(Items.PLAYER_HEAD);
        NbtCompound nbt = head.getOrCreateNbt();

        // Set up GameProfile with UUID and name
        GameProfile profile = new GameProfile(player.getUuid(), player.getName().getString());

        // Fill in texture data (this is the key part)
        SkullBlockEntity.loadProperties(profile, loaded -> {
            NbtCompound skullOwner = new NbtCompound();
            NbtHelper.writeGameProfile(skullOwner, loaded);
            nbt.put("SkullOwner", skullOwner);
        });

        return head;
    }

    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof PlayerVaultBlockEntity vault) {
            if (!vault.isOwner(player.getUuid())) {
                return 0.0f; // Can't break
            }
        }
        return super.calcBlockBreakingDelta(state, player, world, pos);
    }

    public static boolean handleBreak(World world, PlayerEntity player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity) {
        if (blockEntity instanceof PlayerVaultBlockEntity vault) {
            if (!vault.isOwner(player.getUuid())) {
                if (!world.isClient) {
                    player.sendMessage(Text.literal("You cannot break a Vault you don't own!"), true);
                } else {
                    // On client, reset block breaking animation by sending a block update
                    world.updateListeners(blockPos, blockState, blockState, PlayerVaultBlock.NOTIFY_ALL);
                }
                return false;  // cancel the break
            }
        }
        return true;  // allow break if owner
    }

}
