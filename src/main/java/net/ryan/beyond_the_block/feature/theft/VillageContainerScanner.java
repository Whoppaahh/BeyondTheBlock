package net.ryan.beyond_the_block.feature.theft;

import net.minecraft.block.entity.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.structure.Structure;
import net.ryan.beyond_the_block.utils.ModTags;

import java.util.ArrayList;
import java.util.List;

public final class VillageContainerScanner {


    private VillageContainerScanner() {}

    public static void scanChunk(ServerWorld world, ChunkPos chunkPos) {
        WorldChunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
        if (chunk == null || chunk.getBlockEntities().isEmpty()) {
            return;
        }

        StructureAccessor structureAccessor = world.getStructureAccessor();
        VillageContainerTagger tagger = VillageContainerTagger.get(world);
        Registry<Structure> structureRegistry = world.getRegistryManager().get(Registry.STRUCTURE_KEY);

        // Pre-filter structures that actually have a start in this chunk to avoid redundant checks
        List<Structure> structuresInChunk = new ArrayList<>();
        for (var entry : structureRegistry.iterateEntries(ModTags.Blocks.VILLAGE_STRUCTURES)) {
            Structure structure = entry.value();
            // Check if the structure has any start in this chunk using the chunk's stored structure starts
            if (chunk.getStructureStart(structure) != null && chunk.getStructureStart(structure).hasChildren()) {
                structuresInChunk.add(structure);
            }
        }

        if (structuresInChunk.isEmpty()) {
            return;
        }

        int taggedCount = 0;
        for (BlockEntity be : chunk.getBlockEntities().values()) {
            if (!(be instanceof ChestBlockEntity || be instanceof BarrelBlockEntity
            || be instanceof FurnaceBlockEntity || be instanceof BlastFurnaceBlockEntity || be instanceof SmokerBlockEntity)) {
                continue;
            }

            BlockPos pos = be.getPos();

            for (Structure structure : structuresInChunk) {
                StructureStart start = structureAccessor.getStructureAt(pos, structure);
                if (start != null && start.hasChildren()) {
                    tagger.markVillageContainer(pos.toImmutable());
                    taggedCount++;
                    break;
                }
            }
        }
        if (taggedCount > 0) {
            net.ryan.beyond_the_block.core.BeyondTheBlock.LOGGER.info("Scanned chunk at {}: tagged {} containers", chunkPos, taggedCount);
        }
    }
}