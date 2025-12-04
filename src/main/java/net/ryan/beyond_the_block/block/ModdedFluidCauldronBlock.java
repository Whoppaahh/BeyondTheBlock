package net.ryan.beyond_the_block.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.block.Entity.ModdedFluidCauldronBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ModdedFluidCauldronBlock extends LeveledCauldronBlock implements BlockEntityProvider {

    public static final EnumProperty<Content> CONTENT =
            EnumProperty.of("content", Content.class);

    public ModdedFluidCauldronBlock(Settings settings) {
        super(settings, p -> false, CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR);

        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(LEVEL, 1)
                        .with(CONTENT, Content.SLIME)
        );
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        ModdedFluidCauldronBlock.Content content = state.get(CONTENT);
        int level = state.get(LeveledCauldronBlock.LEVEL);

        // No content, no animation
        if (level == 0) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.25 + (0.2 * level); // higher for fuller cauldrons
        double z = pos.getZ() + 0.5;

        switch (content) {

            // ===============================
            // SLIME — gentle bubbling + puffs
            // ===============================
            case SLIME -> {
                // Light bubbling
                if (random.nextFloat() < 0.10f) {
                    world.addParticle(
                            ParticleTypes.ITEM_SLIME,
                            x + random.nextGaussian() * 0.1,
                            y,
                            z + random.nextGaussian() * 0.1,
                            0, 0.02, 0
                    );
                }

                // Small puff (rare)
                if (random.nextFloat() < 0.03f) {
                    world.addParticle(
                            ParticleTypes.SNEEZE,   // actually good for a slime puff effect
                            x,
                            y + 0.1,
                            z,
                            0, 0.02, 0
                    );
                }
            }

            // ======================================
            // HONEY — very slow bubbles + golden wisp
            // ======================================
            case HONEY -> {
                if (random.nextFloat() < 0.06f) {
                    world.addParticle(
                            ParticleTypes.FALLING_HONEY,
                            x + random.nextGaussian() * 0.06,
                            y,
                            z + random.nextGaussian() * 0.06,
                            0, 0.01, 0
                    );
                }

                // ambient honey vapor wisp
                if (random.nextFloat() < 0.02f) {
                    world.addParticle(
                            ParticleTypes.DRIPPING_HONEY, // repurposed as golden vapor
                            x,
                            y + 0.15,
                            z,
                            0, 0.02, 0
                    );
                }
            }

            // =======================================
            // MAGMA — intense bubbling + heat shimmer
            // =======================================
            case MAGMA -> {
                // Lava pop bubbles
                if (random.nextFloat() < 0.12f) {
                    world.addParticle(
                            ParticleTypes.LAVA,
                            x + random.nextGaussian() * 0.12,
                            y,
                            z + random.nextGaussian() * 0.12,
                            0, 0.04, 0
                    );
                }

                // rising flame (rare)
                if (random.nextFloat() < 0.04f) {
                    world.addParticle(
                            ParticleTypes.FLAME,
                            x,
                            y + 0.10,
                            z,
                            0, 0.02, 0
                    );
                }

                // heat shimmer (ambient)
                if (random.nextFloat() < 0.08f) {
                    world.addParticle(
                            ParticleTypes.SMOKE,
                            x + random.nextGaussian() * 0.08,
                            y + 0.05,
                            z + random.nextGaussian() * 0.08,
                            0, 0.01, 0
                    );
                }
            }
        }
    }


    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, net.minecraft.entity.Entity entity) {
        int level = state.get(LEVEL);
        Content content = state.get(CONTENT);

        if (level < 3) return;

        switch (content) {
            case SLIME -> {
                if (entity.fallDistance > 0) entity.fallDistance = 0;

                if (entity.getVelocity().y < 0) {
                    entity.setVelocity(entity.getVelocity().x, 0.6, entity.getVelocity().z);
                }
            }

            case HONEY -> {
                entity.setVelocity(entity.getVelocity().multiply(0.3, 0.2, 0.3));

                if (world.random.nextFloat() < 0.05f) {
                    world.playSound(null, pos, SoundEvents.BLOCK_HONEY_BLOCK_STEP, SoundCategory.BLOCKS, 0.6f, 1.2f);
                }
            }

            case MAGMA -> {
                entity.setOnFireFor(2);

                if (world.random.nextFloat() < 0.1f && world instanceof ServerWorld sw) {
                    sw.spawnParticles(
                            ParticleTypes.FLAME,
                            entity.getX(), entity.getY() + 0.1, entity.getZ(),
                            2,
                            0.1, 0.05, 0.1,
                            0.01
                    );
                }
            }
        }
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CONTENT);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ModdedFluidCauldronBlockEntity(pos, state);
    }



    public enum Content implements StringIdentifiable {
        SLIME("slime"),
        HONEY("honey"),
        MAGMA("magma");

        private final String name;

        Content(String name) { this.name = name; }

        @Override
        public String asString() {
            return name;
        }
    }


}

