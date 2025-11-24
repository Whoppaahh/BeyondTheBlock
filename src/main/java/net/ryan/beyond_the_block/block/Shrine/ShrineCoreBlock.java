package net.ryan.beyond_the_block.block.Shrine;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.block.Entity.ModBlockEntities;
import net.ryan.beyond_the_block.block.Entity.ShrineEntity.ShrineCoreBlockEntity;

public class ShrineCoreBlock extends BlockWithEntity implements BlockEntityProvider {

    public ShrineCoreBlock(Settings settings) {
        super(settings);
    }

    private static final VoxelShape SHAPE =
            Block.createCuboidShape(2, 0, 2, 14, 13, 14);


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
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ShrineCoreBlockEntity shrineCoreBlockEntity) {
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

