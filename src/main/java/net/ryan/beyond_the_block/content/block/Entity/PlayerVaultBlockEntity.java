package net.ryan.beyond_the_block.content.block.Entity;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.content.block.PlayerVaultBlock;
import net.ryan.beyond_the_block.screen.Handlers.PlayerVaultScreenHandler;
import net.ryan.beyond_the_block.sound.ModSounds;
import net.ryan.beyond_the_block.utils.GUI.ImplementedInventory;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerVaultBlockEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory {

    private UUID owner;
    private GameProfile skullProfile;
    private float rotation = 0;
    private final DefaultedList<ItemStack> display = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(54, ItemStack.EMPTY);

    private final ViewerCountManager stateManager = new ViewerCountManager() {
        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
            PlayerVaultBlockEntity.this.playSound(state, ModSounds.PLAYER_VAULT_OPEN);
            PlayerVaultBlockEntity.this.setOpen(state, true);
            BeyondTheBlock.LOGGER.info("Opened vault");
        }

        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
            PlayerVaultBlockEntity.this.playSound(state, ModSounds.PLAYER_VAULT_OPEN);
            PlayerVaultBlockEntity.this.setOpen(state, false);
        }

        @Override
        protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        }

        @Override
        protected boolean isPlayerViewing(PlayerEntity player) {
            if (player.currentScreenHandler instanceof PlayerVaultScreenHandler) {
                Inventory inventory = ((PlayerVaultScreenHandler) player.currentScreenHandler).getInventory();
                return inventory == PlayerVaultBlockEntity.this;
            } else {
                return false;
            }
        }
    };

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        // Always allow the owner
        if (player.getUuid().equals(owner)) return true;

        // If there’s no skull profile set, no access check possible
        if (this.skullProfile == null) return false;

        // Check all equipped armor (helmet etc.)
        for (ItemStack armorStack : player.getArmorItems()) {
            if (armorStack.isOf(Items.PLAYER_HEAD)) {
                NbtCompound tag = armorStack.getNbt();
                if (tag != null && tag.contains("SkullOwner", NbtElement.COMPOUND_TYPE)) {
                    NbtCompound skullOwner = tag.getCompound("SkullOwner");
                    // Check for matching UUID (direct match)
                    if (skullOwner.containsUuid("Id") && skullOwner.getUuid("Id").equals(owner)
                            || skullOwner.contains("Name") && skullOwner.getString("Name").equals(skullProfile.getName())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public int getMaxCountPerStack() {
        return 512;
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.isRemoved() && !player.isSpectator()) {
            this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
            markDirty();
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.isRemoved() && !player.isSpectator()) {
            this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
            markDirty();
        }
    }

    public void tick() {
        if (!this.isRemoved()) {
            this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    void setOpen(BlockState state, boolean open) {
        this.world.setBlockState(this.getPos(), state.with(PlayerVaultBlock.OPEN, open), Block.NOTIFY_ALL);
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.get(PlayerVaultBlock.FACING).getVector();
        double d = this.pos.getX() + 0.5 + vec3i.getX() / 2.0;
        double e = this.pos.getY() + 0.5 + vec3i.getY() / 2.0;
        double f = this.pos.getZ() + 0.5 + vec3i.getZ() / 2.0;
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }

    public PlayerVaultBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PLAYER_VAULT_BLOCK_ENTITY, pos, state);
    }

    public void setOwner(UUID playerID, String name) {
        this.owner = playerID;
        this.skullProfile = new GameProfile(playerID, name);
        SkullBlockEntity.loadProperties(skullProfile, loaded -> {
            this.skullProfile = loaded;
            this.markDirty();

            if (this.world != null && !this.world.isClient) {
                BlockState state = world.getBlockState(pos);
                world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            }
        });
    }

    public boolean isOwner(UUID playerID) {
        return playerID.equals(this.owner);
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("Owner")) {
            this.owner = nbt.getUuid("Owner");
        }
        if (nbt.contains("SkullOwner", NbtElement.COMPOUND_TYPE)) {
            this.skullProfile = NbtHelper.toGameProfile(nbt.getCompound("SkullOwner"));
        }

        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        readLargeStackInventory(nbt, this.inventory);
        NbtList display = nbt.getList("Display", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < display.size(); i++) {
            this.display.set(i, ItemStack.fromNbt(display.getCompound(i)));
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.owner != null) {
            nbt.putUuid("Owner", this.owner);
        }
        if (this.skullProfile != null) {
            NbtCompound skullNbt = new NbtCompound();
            NbtHelper.writeGameProfile(skullNbt, this.skullProfile);
            nbt.put("SkullOwner", skullNbt);
        }

        writeLargeStackInventory(nbt, this.inventory);
        NbtList display = new NbtList();
        this.display.forEach(stack -> display.add(stack.writeNbt(new NbtCompound())));
        nbt.put("Display", display);
    }

    public static void writeLargeStackInventory(NbtCompound nbt, DefaultedList<ItemStack> items) {
        NbtList list = new NbtList();
        for (int i = 0; i < items.size(); ++i) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                NbtCompound stackNbt = new NbtCompound();
                stackNbt.putInt("Slot", i);
                stack.writeNbt(stackNbt);
                // overwrite "Count" as int
                stackNbt.putInt("Count", stack.getCount());
                list.add(stackNbt);
            }
        }
        nbt.put("Items", list);
    }

    public static void readLargeStackInventory(NbtCompound nbt, DefaultedList<ItemStack> items) {
        NbtList list = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); ++i) {
            NbtCompound stackNbt = list.getCompound(i);
            int slot = stackNbt.getInt("Slot");
            if (slot >= 0 && slot < items.size()) {
                ItemStack stack = ItemStack.fromNbt(stackNbt);
                // restore the real count if saved as int
                if (stackNbt.contains("Count", NbtElement.INT_TYPE)) {
                    stack.setCount(stackNbt.getInt("Count"));
                }
                items.set(slot, stack);
            }
        }
    }


    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    public DefaultedList<ItemStack> getDisplayItem() {
        return this.display;
    }

    public float getRenderingRotation() {
        rotation += 0.25f;
        rotation %= 360;
        return rotation;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeBlockPos(this.pos);
        packetByteBuf.writeString(this.skullProfile.getName());
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Personal Vault");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeBlockPos(this.pos);
        packetByteBuf.writeString(this.skullProfile.getName());
        return new PlayerVaultScreenHandler(syncId, inv, packetByteBuf);
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

    public GameProfile getSkullProfile() {
        return this.skullProfile;
    }
}
