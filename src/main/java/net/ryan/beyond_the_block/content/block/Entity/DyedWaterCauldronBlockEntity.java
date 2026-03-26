package net.ryan.beyond_the_block.content.block.Entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class DyedWaterCauldronBlockEntity extends BlockEntity {

    // Default vanilla water tint 0x3F76E4
    private int color = 0x3F76E4;

    // Accumulators for Bedrock-style dye mixing
    private int totalR = 63;
    private int totalG = 118;
    private int totalB = 228;
    private int mixCount = 1;

    public DyedWaterCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DYED_WATER_CAULDRON_BLOCK_ENTITY, pos, state);
    }

    public int getColor() {
        return color;
    }

    /** Reset the mixing state when the cauldron empties */
    public void reset() {
        color = 0x3F76E4;
        totalR = 63;
        totalG = 118;
        totalB = 228;
        mixCount = 1;
        markDirty();
        if (world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }

    /**
     * Bedrock-style dye mixing:
     * - Each dye contributes its RGB amount
     * - Average is computed over total dyes used
     * - Saturation boosted to prevent washed-out colours
     */
    public void mixDye(int newColor) {

        int r = (newColor >> 16) & 0xFF;
        int g = (newColor >> 8) & 0xFF;
        int b = newColor & 0xFF;

        // Add new dye into accumulation
        totalR += r;
        totalG += g;
        totalB += b;
        mixCount++;

        // Compute averaged RGB
        int avgR = totalR / mixCount;
        int avgG = totalG / mixCount;
        int avgB = totalB / mixCount;

        // Saturation boost (prevents grey/muddy blends)
        float boost = 1.15f;  // raising this increases dye intensity
        avgR = Math.min(255, (int)(avgR * boost));
        avgG = Math.min(255, (int)(avgG * boost));
        avgB = Math.min(255, (int)(avgB * boost));

        // Save final colour
        color = (avgR << 16) | (avgG << 8) | avgB;

        markDirty();
        if (world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }

    // -------------------------------------------------------------------------
    // NBT
    // -------------------------------------------------------------------------
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Color", color);
        nbt.putInt("TotalR", totalR);
        nbt.putInt("TotalG", totalG);
        nbt.putInt("TotalB", totalB);
        nbt.putInt("MixCount", mixCount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        color = nbt.getInt("Color");
        totalR = nbt.getInt("TotalR");
        totalG = nbt.getInt("TotalG");
        totalB = nbt.getInt("TotalB");
        mixCount = nbt.getInt("MixCount");
    }

    // -------------------------------------------------------------------------
    // Networking
    // -------------------------------------------------------------------------

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
