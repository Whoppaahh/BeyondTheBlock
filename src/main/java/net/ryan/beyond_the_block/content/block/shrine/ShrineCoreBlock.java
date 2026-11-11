package net.ryan.beyond_the_block.content.block.shrine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.ShrineCoreBlockEntity;
import net.ryan.beyond_the_block.content.blockentity.ShrineDecorBlockEntity;
import net.ryan.beyond_the_block.content.registry.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShrineCoreBlock extends AbstractShrineBlock implements BlockEntityProvider {

    public ShrineCoreBlock(Settings settings) {
        super(settings);
    }

    private static final VoxelShape SHAPE =
            Block.createCuboidShape(2, 0, 2, 14, 13, 14);


    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("The heart of an ancient shrine.").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Right-click to receive the Shrine Rules.").formatted(Formatting.GOLD));
        tooltip.add(Text.literal("Sneak + Right-click to open its inventory.").formatted(Formatting.DARK_PURPLE));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ShrineCoreBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ModBlockEntities.SHRINE_CORE_BLOCK_ENTITY, ShrineCoreBlockEntity::tick);
    }



    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ShrineCoreBlockEntity shrineCoreBlockEntity) {
                if (!isStructureValid(world, pos)) {
                    player.sendMessage(Text.literal("The shrine structure is incomplete!").formatted(Formatting.RED), true);
                    return ActionResult.SUCCESS;
                }

                if (player.isSneaking()) {
                    player.openHandledScreen(shrineCoreBlockEntity);
                }else{
                    ItemStack book = createRulesBook();
                    // Give the book to the player
                    if (!player.getInventory().insertStack(book)) {
                        player.dropItem(book, false);
                    }
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    private boolean isStructureValid(World world, BlockPos pos) {
        List<ShrineDecorBlockEntity> decorBlocks = new ArrayList<>();
        int searchRadius = 8;

        for (BlockPos searchPos : BlockPos.iterate(pos.add(-searchRadius, -2, -searchRadius), pos.add(searchRadius, 2, searchRadius))) {
            BlockEntity be = world.getBlockEntity(searchPos);
            if (be instanceof ShrineDecorBlockEntity decor) {
                decorBlocks.add(decor);
            }
        }

        if (decorBlocks.size() < 4) {
            return false;
        }

        for (ShrineDecorBlockEntity decor : decorBlocks) {
            if (decor.getUseCount() < 1) {
                return false;
            }
        }

        return true;
    }

    private ItemStack createRulesBook() {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        NbtCompound tag = new NbtCompound();
        tag.putString("title", "Shrine Rules");
        tag.putString("author", "The Ancients");

        NbtList pages = new NbtList();
        pages.add(NbtString.of(Text.Serializer.toJson(Text.literal("Welcome to the Shrine!"))));
        pages.add(NbtString.of(Text.Serializer.toJson(Text.literal("Solve the riddles..."))));
        pages.add(NbtString.of(Text.Serializer.toJson(Text.literal("Offer items to gain rewards."))));
        pages.add(NbtString.of(Text.Serializer.toJson(Text.literal("One riddle per day, per player."))));

        tag.put("pages", pages);
        book.setNbt(tag);

        return book;
    }
}

