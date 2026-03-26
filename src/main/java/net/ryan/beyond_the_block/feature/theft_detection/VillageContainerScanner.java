package net.ryan.beyond_the_block.feature.theft_detection;

import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.structure.Structure;

import java.util.List;

public class VillageContainerScanner {

    private static List<Structure> cachedVillageStructures;

    // All vanilla village biome-specific structure IDs
    private static final Identifier[] VILLAGE_IDS = new Identifier[] {
            new Identifier("minecraft", "village_plains"),
            new Identifier("minecraft", "village_snowy"),
            new Identifier("minecraft", "village_savanna"),
            new Identifier("minecraft", "village_desert"),
            new Identifier("minecraft", "village_taiga")
    };

    public static void scanChunk(ServerWorld world, ChunkPos chunkPos) {
        WorldChunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
        if (chunk == null) return;

        StructureAccessor structureAccessor = world.getStructureAccessor();

        for (BlockEntity be : chunk.getBlockEntities().values()) {
            if (!(be instanceof ChestBlockEntity || be instanceof BarrelBlockEntity)) continue;



            BlockPos pos = be.getPos();

            // Check each village biome structure to see if chest is inside
            for (Identifier villageId : VILLAGE_IDS) {
                Structure structure = world.getRegistryManager()
                        .get(Registry.STRUCTURE_KEY)
                        .get(villageId);

                if (structure == null) continue;


                StructureStart start = structureAccessor.getStructureAt(pos, structure);

                if (start != null && start.hasChildren()) {
                    VillageContainerTagger.get(world).markVillageContainer(pos.toImmutable());
                    break; // no need to check others once tagged
                }
            }
        }
    }
}
