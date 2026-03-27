package net.ryan.beyond_the_block.content.blockentity;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.riddles.Riddle;
import net.ryan.beyond_the_block.content.riddles.RiddleDataManager;
import net.ryan.beyond_the_block.core.bootstrap.ContentRegistrar;
import net.ryan.beyond_the_block.screen.handler.RiddleCoreScreenHandler;
import net.ryan.beyond_the_block.utils.visual.ImplementedInventory;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static net.ryan.beyond_the_block.network.packets.PacketIDs.SYNC_RIDDLES_ID;

public class ShrineCoreBlockEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory {
    private final DefaultedList<ItemStack> item = DefaultedList.ofSize(1, Items.WRITTEN_BOOK.getDefaultStack());
    private float rotation = 0;
    private boolean initialised = false;

    // Constructor
    public ShrineCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHRINE_CORE_BLOCK_ENTITY, pos, state);
    }

    public float getRenderingRotation() {
        rotation += 0.5f;
        if(rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    public static void tick(World world, BlockPos pos, net.minecraft.block.BlockState state, ShrineCoreBlockEntity blockEntity) {
        if (!blockEntity.initialised && !world.isClient) {
            blockEntity.initialised = true;
            if (world instanceof ServerWorld serverWorld) {
                RiddleDataManager handler = RiddleDataManager.get(serverWorld, ContentRegistrar.RIDDLE_COMPONENTS);
                handler.setShrinePos(pos);
            }
        }
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return item;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, item);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, item);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeBlockPos(this.pos);
    }


    @Override
    public Text getDisplayName() {
        return Text.literal("Shrine Core");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeBlockPos(this.pos);
        ServerPlayNetworking.send((ServerPlayerEntity) player, SYNC_RIDDLES_ID, createRiddleSyncPacket());

        return new RiddleCoreScreenHandler(syncId, inv, packetByteBuf);
    }
    private PacketByteBuf createRiddleSyncPacket() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        RiddleDataManager handler = RiddleDataManager.get((ServerWorld) world, ContentRegistrar.RIDDLE_COMPONENTS);
        // Active riddles
        Map<UUID, Riddle> active = handler.getActiveRiddles();
        buf.writeInt(active.size());  // Write the size of active riddles

        for (Map.Entry<UUID, Riddle> entry : active.entrySet()) {
            UUID playerId = entry.getKey();
            Riddle riddle = entry.getValue();

            buf.writeUuid(playerId);       // ✅ Write the player UUID first
            buf.writeUuid(riddle.id()); // Then the riddle ID

            List<String> pages = riddle.pages();
            buf.writeCollection(pages, PacketByteBuf::writeString);

            buf.writeCollection(
                    riddle.requiredItems().stream()
                            .map(item -> Registry.ITEM.getId(item).toString())
                            .toList(),
                    PacketByteBuf::writeString
            );
        }

        // Completed riddles
        Map<UUID, Set<UUID>> completed = handler.getCompletedRiddles();
        buf.writeInt(completed.size());  // Write the number of players with completed riddles
        for (Map.Entry<UUID, Set<UUID>> entry : completed.entrySet()) {
            buf.writeUuid(entry.getKey());  // Write player UUID
            Set<UUID> completedRiddles = entry.getValue();
            buf.writeInt(completedRiddles.size());  // Write the number of completed riddles for the player
            for (UUID riddleId : completedRiddles) {
                buf.writeUuid(riddleId);  // Write the riddle ID
            }
        }

        return buf;
    }

}
