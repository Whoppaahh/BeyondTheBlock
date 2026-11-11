package net.ryan.beyond_the_block.content.item.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.ryan.beyond_the_block.advancements.ModCriteria;
import net.ryan.beyond_the_block.content.sound.ModSounds;
import net.ryan.beyond_the_block.utils.ModTags;

import java.util.List;

public class XenolithStaffItem extends Item {
    public XenolithStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.XENOLITH_STAFF_PULSE, SoundCategory.PLAYERS, 1.0f, 1.0f);

        if (world.isClient) {
            spawnPulseParticles(world, player);
        }

        if (!world.isClient) {
            BlockPos playerPos = player.getBlockPos();
            Chunk chunk = world.getChunk(playerPos);
            int chunkX = chunk.getPos().x * 16;
            int chunkZ = chunk.getPos().z * 16;

            int oresFound = 0;
            int radius = 32; // Limit Y scan range for performance

            // Iterate through the chunk
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int minY = Math.max(world.getBottomY(), (int)player.getY() - radius);
                    int maxY = Math.min(world.getTopY(), (int)player.getY() + radius);

                    for (int y = minY; y < maxY; y++) {
                        BlockPos pos = new BlockPos(chunkX + x, y, chunkZ + z);
                        BlockState state = world.getBlockState(pos);
                        if (state.isIn(ModTags.Blocks.MOD_ORES)) {
                            oresFound++;
                            if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                                net.minecraft.particle.ParticleEffect particle = getParticleForBlock(state.getBlock());
                                serverWorld.spawnParticles(
                                        particle,
                                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                                        10, 0.2, 0.2, 0.2, 0.05
                                );
                            }
                        }
                    }
                }
            }

            if (oresFound > 0) {
                player.sendMessage(Text.literal("Staff pulse found " + oresFound + " ores in this chunk."), true);
                player.getItemCooldownManager().set(this, 200); // 10 second cooldown
            } else {
                player.sendMessage(Text.literal("No ores detected in this chunk."), true);
                player.getItemCooldownManager().set(this, 40); // 2 second cooldown on fail
            }

            if (player instanceof ServerPlayerEntity serverPlayer) {
                ModCriteria.STAFF_USAGE.trigger(serverPlayer, oresFound);
            }
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.beyond_the_block.xenolith_staff.tooltip.1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.beyond_the_block.xenolith_staff.tooltip.2").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.literal("Scans the current chunk for ores (±32 blocks Y).").formatted(Formatting.AQUA));
        tooltip.add(Text.literal("Cooldown: 10 seconds (2s on failure).").formatted(Formatting.DARK_GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }

    private void spawnPulseParticles(World world, PlayerEntity player) {
        for (int i = 0; i < 360; i += 10) {
            double angle = Math.toRadians(i);
            double x = Math.cos(angle);
            double z = Math.sin(angle);
            world.addParticle(net.minecraft.particle.ParticleTypes.END_ROD,
                    player.getX() + x, player.getY() + 0.1, player.getZ() + z,
                    x * 0.2, 0, z * 0.2);
        }
    }

    private net.minecraft.particle.ParticleEffect getParticleForBlock(Block block) {
        if (block == net.ryan.beyond_the_block.content.registry.ModBlocks.RUBY_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.DEEPSLATE_RUBY_ORE)
            return net.ryan.beyond_the_block.content.registry.ModParticles.RUBY_PARTICLE;
        if (block == net.ryan.beyond_the_block.content.registry.ModBlocks.AMBERINE_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.DEEPSLATE_AMBERINE_ORE)
            return net.ryan.beyond_the_block.content.registry.ModParticles.AMBERINE_PARTICLE;
        if (block == net.ryan.beyond_the_block.content.registry.ModBlocks.AZUROS_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.DEEPSLATE_AZUROS_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.END_AZUROS_ORE)
            return net.ryan.beyond_the_block.content.registry.ModParticles.AZUROS_PARTICLE;
        if (block == net.ryan.beyond_the_block.content.registry.ModBlocks.CHROMITE_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.DEEPSLATE_CHROMITE_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.END_CHROMITE_ORE)
            return net.ryan.beyond_the_block.content.registry.ModParticles.CHROMITE_PARTICLE;
        if (block == net.ryan.beyond_the_block.content.registry.ModBlocks.MIRANITE_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.DEEPSLATE_MIRANITE_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.END_MIRANITE_ORE)
            return net.ryan.beyond_the_block.content.registry.ModParticles.MIRANITE_PARTICLE;
        if (block == net.ryan.beyond_the_block.content.registry.ModBlocks.NOCTURNITE_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.DEEPSLATE_NOCTURNITE_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.END_NOCTURNITE_ORE)
            return net.ryan.beyond_the_block.content.registry.ModParticles.NOCTURNITE_PARTICLE;
        if (block == net.ryan.beyond_the_block.content.registry.ModBlocks.INDIGRA_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.DEEPSLATE_INDIGRA_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.END_INDIGRA_ORE)
            return net.ryan.beyond_the_block.content.registry.ModParticles.INDIGRA_PARTICLE;
        if (block == net.ryan.beyond_the_block.content.registry.ModBlocks.ROSETTE_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.DEEPSLATE_ROSETTE_ORE || block == net.ryan.beyond_the_block.content.registry.ModBlocks.END_ROSETTE_ORE)
            return net.ryan.beyond_the_block.content.registry.ModParticles.ROSETTE_PARTICLE;

        return net.minecraft.particle.ParticleTypes.END_ROD;
    }
}