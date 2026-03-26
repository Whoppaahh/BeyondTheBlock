package net.ryan.beyond_the_block.feature.theft_detection;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.HashSet;
import java.util.Set;

public class VillageContainerTagger extends PersistentState {

    private final Set<BlockPos> villageContainers = new HashSet<>();

    public static VillageContainerTagger get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                VillageContainerTagger::readNbt,
                VillageContainerTagger::new,
                "village_container_tracker"
        );
    }

    public void markVillageContainer(BlockPos pos) {
        if (villageContainers.add(pos.toImmutable())) {
            markDirty();
            BeyondTheBlock.LOGGER.info("Tagged Village Container at: " + pos);
        }
    }

    public void unmarkVillageContainer(BlockPos pos) {
        if (villageContainers.remove(pos)) {
            markDirty();
        }
    }

    public boolean isVillageContainer(BlockPos pos) {
        return villageContainers.contains(pos);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();
        for (BlockPos pos : villageContainers) {
            list.add(new NbtIntArray(new int[] { pos.getX(), pos.getY(), pos.getZ() }));
        }
        nbt.put("Containers", list);
        return nbt;
    }

    public static VillageContainerTagger readNbt(NbtCompound nbt) {
        VillageContainerTagger tracker = new VillageContainerTagger();
        NbtList list = nbt.getList("Containers", NbtElement.INT_ARRAY_TYPE);
        for (NbtElement element : list) {
            int[] coords = ((NbtIntArray) element).getIntArray();
            if (coords.length == 3) {
                tracker.villageContainers.add(new BlockPos(coords[0], coords[1], coords[2]));
            }
        }
        return tracker;
    }
}
