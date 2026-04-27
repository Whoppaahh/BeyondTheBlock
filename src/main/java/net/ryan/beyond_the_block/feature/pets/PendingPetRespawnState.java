package net.ryan.beyond_the_block.feature.pets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.ryan.beyond_the_block.content.block.PetBedBlock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PendingPetRespawnState extends PersistentState {

    private static final String DATA_NAME = "btb_pending_pet_respawns";

    private final List<Entry> entries = new ArrayList<>();

    public static PendingPetRespawnState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                PendingPetRespawnState::fromNbt,
                PendingPetRespawnState::new,
                DATA_NAME
        );
    }

    public void add(TameableEntity pet, BlockPos homePos) {
        if (pet.getOwnerUuid() == null) return;

        NbtCompound entityNbt = new NbtCompound();
        if (!pet.saveNbt(entityNbt)) return;

        entityNbt.putFloat("Health", pet.getMaxHealth());

        long currentDay = pet.getWorld().getTimeOfDay() / 24000L;

        // Force respawn next day, not same dawn
        long respawnDay = currentDay + 1;

        entries.add(new Entry(
                entityNbt,
                homePos.toImmutable(),
                pet.getName().getString(),
                respawnDay,
                pet.getOwnerUuid()
        ));

        markDirty();
    }

    public void respawnReadyPets(ServerWorld world) {
        long currentDay = world.getTimeOfDay() / 24000L;

        Iterator<Entry> iterator = entries.iterator();

        while (iterator.hasNext()) {
            Entry entry = iterator.next();

            if (currentDay < entry.respawnDay()) {
                continue;
            }

            if (!(world.getBlockState(entry.homePos()).getBlock() instanceof PetBedBlock)) {
                iterator.remove();
                markDirty();
                continue;
            }

            BlockPos spawnPos = findSafeSpawnPos(world, entry.homePos());

            // Pre-particles
            world.spawnParticles(
                    net.minecraft.particle.ParticleTypes.PORTAL,
                    spawnPos.getX() + 0.5D,
                    spawnPos.getY() + 0.5D,
                    spawnPos.getZ() + 0.5D,
                    40,
                    0.4, 0.4, 0.4,
                    0.05
            );

            Entity entity = EntityType.loadEntityWithPassengers(
                    entry.entityNbt().copy(),
                    world,
                    loaded -> {
                        loaded.refreshPositionAndAngles(
                                spawnPos.getX() + 0.5D,
                                spawnPos.getY(),
                                spawnPos.getZ() + 0.5D,
                                loaded.getYaw(),
                                loaded.getPitch()
                        );
                        return loaded;
                    }
            );

            if (entity != null) {
                world.spawnNewEntityAndPassengers(entity);

                // Post-particles
                world.spawnParticles(
                        net.minecraft.particle.ParticleTypes.PORTAL,
                        entity.getX(),
                        entity.getY() + 0.5D,
                        entity.getZ(),
                        40,
                        0.4, 0.4, 0.4,
                        0.05
                );

                world.playSound(
                        null,
                        spawnPos,
                        net.minecraft.sound.SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                        net.minecraft.sound.SoundCategory.BLOCKS,
                        0.8F,
                        1.2F
                );

                // Notify owner
                var player = world.getServer().getPlayerManager().getPlayer(entry.ownerUuid());
                if (player != null) {
                    player.sendMessage(
                            net.minecraft.text.Text.literal(entry.name() + " has returned home."),
                            false
                    );
                }
            }

            iterator.remove();
            markDirty();
        }
    }

    private static BlockPos findSafeSpawnPos(ServerWorld world, BlockPos bedPos) {
        BlockPos above = bedPos.up();

        if (world.getBlockState(above).isAir() && world.getBlockState(above.up()).isAir()) {
            return above;
        }

        for (BlockPos candidate : BlockPos.iterateOutwards(bedPos, 2, 1, 2)) {
            BlockPos pos = candidate.toImmutable();

            if (world.getBlockState(pos).isAir() && world.getBlockState(pos.up()).isAir()) {
                return pos;
            }
        }

        return above;
    }

    public static PendingPetRespawnState fromNbt(NbtCompound nbt) {
        PendingPetRespawnState state = new PendingPetRespawnState();

        NbtList list = nbt.getList("Pets", 10);

        for (int i = 0; i < list.size(); i++) {
            NbtCompound entryNbt = list.getCompound(i);

            NbtCompound entityNbt = entryNbt.getCompound("Entity");
            String name = entryNbt.getString("Name");
            long respawnDay = entryNbt.getLong("RespawnDay");
            UUID owner = entryNbt.getUuid("Owner");

            BlockPos homePos = new BlockPos(
                    entryNbt.getInt("HomeX"),
                    entryNbt.getInt("HomeY"),
                    entryNbt.getInt("HomeZ")
            );

            state.entries.add(new Entry(entityNbt, homePos, name, respawnDay, owner));
        }

        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();

        for (Entry entry : entries) {
            NbtCompound entryNbt = new NbtCompound();

            entryNbt.put("Entity", entry.entityNbt());
            entryNbt.putString("Name", entry.name());
            entryNbt.putLong("RespawnDay", entry.respawnDay());
            entryNbt.putUuid("Owner", entry.ownerUuid());

            entryNbt.putInt("HomeX", entry.homePos().getX());
            entryNbt.putInt("HomeY", entry.homePos().getY());
            entryNbt.putInt("HomeZ", entry.homePos().getZ());

            list.add(entryNbt);
        }

        nbt.put("Pets", list);
        return nbt;
    }

    private record Entry(
            NbtCompound entityNbt,
            BlockPos homePos,
            String name,
            long respawnDay,
            java.util.UUID ownerUuid
    ) {}
}