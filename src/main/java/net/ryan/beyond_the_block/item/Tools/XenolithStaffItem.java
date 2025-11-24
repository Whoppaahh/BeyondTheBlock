package net.ryan.beyond_the_block.item.Tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.ryan.beyond_the_block.block.ModBlocks;

public class XenolithStaffItem extends Item {
    public XenolithStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            BlockPos playerPos = player.getBlockPos();
            Chunk chunk = world.getChunk(playerPos);
            int chunkX = chunk.getPos().x * 16;
            int chunkZ = chunk.getPos().z * 16;

            Block[] targetOres = {
                    ModBlocks.RUBY_ORE,
                    ModBlocks.DEEPSLATE_RUBY_ORE,
                    ModBlocks.AMBERINE_ORE,
                    ModBlocks.DEEPSLATE_AMBERINE_ORE,
                    ModBlocks.AZUROS_ORE,
                    ModBlocks.END_AZUROS_ORE,
                    ModBlocks.DEEPSLATE_AZUROS_ORE,
                    ModBlocks.CHROMITE_ORE,
                    ModBlocks.END_CHROMITE_ORE,
                    ModBlocks.DEEPSLATE_CHROMITE_ORE,
                    ModBlocks.INDIGRA_ORE,
                    ModBlocks.END_INDIGRA_ORE,
                    ModBlocks.DEEPSLATE_INDIGRA_ORE,
                    ModBlocks.MIRANITE_ORE,
                    ModBlocks.END_MIRANITE_ORE,
                    ModBlocks.DEEPSLATE_MIRANITE_ORE,
                    ModBlocks.NOCTURNITE_ORE,
                    ModBlocks.END_NOCTURNITE_ORE,
                    ModBlocks.DEEPSLATE_NOCTURNITE_ORE,
                    ModBlocks.ROSETTE_ORE,
                    ModBlocks.END_ROSETTE_ORE,
                    ModBlocks.DEEPSLATE_ROSETTE_ORE,
                    ModBlocks.XIRION_ORE,
                    ModBlocks.END_XIRION_ORE,
                    ModBlocks.DEEPSLATE_XIRION_ORE
            };

            // Iterate through the chunk
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = world.getBottomY(); y < world.getTopY(); y++) {
                        BlockPos pos = new BlockPos(chunkX + x, y, chunkZ + z);
                        BlockState state = world.getBlockState(pos);
                        for (Block ore : targetOres) {
                            if (state.isOf(ore)) {
                                player.sendMessage(Text.literal("Found " + ore.getName().getString() + " at " + pos.toShortString()), false);
                            }
                        }
                    }
                }
            }
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }
}