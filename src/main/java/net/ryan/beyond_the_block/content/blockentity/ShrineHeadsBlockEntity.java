package net.ryan.beyond_the_block.content.blockentity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.riddles.Riddle;
import net.ryan.beyond_the_block.content.riddles.RiddleDataManager;
import net.ryan.beyond_the_block.content.sound.ModSounds;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.utils.GUI.ImplementedInventory;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ShrineHeadsBlockEntity extends BlockEntity implements ImplementedInventory {
    private DefaultedList<ItemStack> items = DefaultedList.ofSize(4, ItemStack.EMPTY);
    public boolean initialised = false;

    private final Map<Direction, BlockPos> lecternMap = new EnumMap<>(Direction.class);
    private final Map<Direction, BlockPos> inputMap = new EnumMap<>(Direction.class);
    private final Map<Integer, Direction> headDirections = new HashMap<>();
    public long lastSynced = 0;

    // Constructor
    public ShrineHeadsBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHRINE_HEADS_BLOCK_ENTITY, pos, state);
        BlockPos shrineBlockPos = pos.down(2);
        inputMap.put(Direction.NORTH, shrineBlockPos.add(0, -1, -1)); // north
        inputMap.put(Direction.SOUTH, shrineBlockPos.add(0, -1, 1));  // south
        inputMap.put(Direction.EAST, shrineBlockPos.add(1, -1, 0));  // east
        inputMap.put(Direction.WEST, shrineBlockPos.add(-1, -1, 0)); // west

        lecternMap.put(Direction.NORTH, shrineBlockPos.add(0, -2, -2)); // north
        lecternMap.put(Direction.SOUTH, shrineBlockPos.add(0, -2, 2));  // south
        lecternMap.put(Direction.EAST, shrineBlockPos.add(2, -2, 0));  // east
        lecternMap.put(Direction.WEST, shrineBlockPos.add(-2, -2, 0)); // west

        headDirections.put(0, Direction.NORTH);
        headDirections.put(1, Direction.SOUTH);
        headDirections.put(2, Direction.EAST);
        headDirections.put(3, Direction.WEST);

    }

    public Direction getHeadDirectionForSlot(int slot) {
        return headDirections.getOrDefault(slot, Direction.NORTH); // Fallback
    }

    public boolean needsSync(long handlerTimestamp) {
        return handlerTimestamp > lastSynced;
    }

    public void updateLastSynced(long timestamp) {
        this.lastSynced = timestamp;
    }

    public void loadShrineHeads(ServerPlayerEntity player) {
        // Track which players have already been assigned heads
        Set<UUID> assignedPlayers = new HashSet<>();
        for (ItemStack itemStack : items) {
            if (!itemStack.isEmpty()) {
                NbtCompound tag = itemStack.getOrCreateNbt();
                if (tag.contains("SkullOwner")) {
                    NbtCompound skullOwner = tag.getCompound("SkullOwner");
                    assignedPlayers.add(UUID.fromString(skullOwner.getString("Id")));
                    //     EmeraldEmpire.LOGGER.info("Assigned Head: {}", skullOwner.getString("Id"));
                }
            }
        }
        UUID playerID = player.getUuid();

        //  EmeraldEmpire.LOGGER.info("Player ID: {}", playerID);
        if (!assignedPlayers.contains(playerID)) {
            // Find an empty slot for this player's head if they haven't been assigned yet
            for (int i = 0; i < 4; i++) {
                if (items.get(i).isEmpty() && !assignedPlayers.contains(playerID)) {
                    //   items.set(i, createPlayerHeadItem(player));
                    setStack(i, createPlayerHeadItem(player));
                    //EmeraldEmpire.LOGGER.info("Created Head: {}", player.getUuid());
                    markDirty(); // Notify the block entity has changed
                    if (world != null && !world.isClient) {
                        ((ServerWorld) world).getChunkManager().markForUpdate(pos);
                    }

                    break;
                }
            }
        } else {
            //EmeraldEmpire.LOGGER.info("Player {} already has a head assigned.", player.getName().getString());
        }
        updatePlayerInputBlocks(player);
        addRiddleBookToLectern(player);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (!stack.isEmpty() && world != null && !world.isClient) {
            RiddleDataManager handler = RiddleDataManager.get((ServerWorld) world, BeyondTheBlock.RIDDLE_COMPONENTS);
            sync(); // auto-sync
            updateLastSynced(handler.getLastUpdated());
        }
    }

    public void clearPlayerHead(UUID playerID) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                NbtCompound tag = stack.getOrCreateNbt();
                //    EmeraldEmpire.LOGGER.info("Checking head slot {}: NBT = {}", i, tag);

                if (tag.contains("SkullOwner")) {
                    NbtCompound skullOwner = tag.getCompound("SkullOwner");
                    if (playerID.toString().equals(skullOwner.getString("Id"))) {
                        //items.set(i, ItemStack.EMPTY);
                        this.setStack(i, ItemStack.EMPTY);
                        headDirections.remove(i);
                        initialised = false;
                        markDirty();
                        sync();

                        // Force the block's visual state update here
                        if (world != null && !world.isClient) {
                            BlockState state = world.getBlockState(pos);
                            world.setBlockState(pos, state, Block.FORCE_STATE, Block.NOTIFY_ALL);
                            //   EmeraldEmpire.LOGGER.info("Force updated block state to sync player head visual.");
                        }

                        //   EmeraldEmpire.LOGGER.info("Cleared head for player: {}", playerID);
                        //  EmeraldEmpire.LOGGER.info("After clearing, getStack({}): {}", i, getStack(i));
                        if (getStack(i) != null && !getStack(i).isEmpty()) {
                            //EmeraldEmpire.LOGGER.warn("getStack({}) is not empty after clear! Stack: {}", i, getStack(i));
                        }
                        if (world != null) {
                            ((ServerWorld) world).spawnParticles(ParticleTypes.ENCHANT,
                                    pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                                    20, 0.3, 0.3, 0.3, 0.1
                            );
                        }
                        return;
                    }
                }
            }
        }
    }

    private Integer getSlotForPlayer(UUID playerID) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                NbtCompound tag = stack.getOrCreateNbt();
                if (tag.contains("SkullOwner")) {
                    NbtCompound skullOwner = tag.getCompound("SkullOwner");
                    if (playerID.toString().equals(skullOwner.getString("Id"))) {
                        //  EmeraldEmpire.LOGGER.info("Checking slot {}: SkullOwner = {}", i, tag);

                        return i;
                    }
                }
            }
        }
        return null;
    }


    private void updatePlayerInputBlocks(ServerPlayerEntity player) {
        if (world == null || world.isClient) return;
        UUID playerID = player.getUuid();

        RiddleDataManager handler = RiddleDataManager.get(player.getWorld(), BeyondTheBlock.RIDDLE_COMPONENTS);
        Riddle riddle = handler.getRiddle(playerID);

        Integer slot = getSlotForPlayer(playerID);
        Direction direction = slot != null ? headDirections.getOrDefault(slot, Direction.NORTH) : null;
        ;
        // EmeraldEmpire.LOGGER.info("Slot for player {} is {}", playerID, slot);


        if (riddle == null || direction == null) {
            //EmeraldEmpire.LOGGER.warn("Unable to place input block — missing riddle or direction");
            return;
        }
        int requiredItems = riddle.getRequiredItems().size();
        // Determine correct input block (Single or Double Lectern)
        BlockState inputLecternState = (requiredItems == 1)
                ? ModBlocks.SINGLE_INPUT_BLOCK.getDefaultState()
                : ModBlocks.DOUBLE_INPUT_BLOCK.getDefaultState();

        BlockPos inputLecternPos = inputMap.get(direction);
        if (inputLecternPos != null) {
            //EmeraldEmpire.LOGGER.info("Placing input lectern at " + inputLecternPos + " for direction " + direction);
            world.setBlockState(inputLecternPos, inputLecternState);


            BlockEntity be = world.getBlockEntity(inputLecternPos);
            if (be instanceof SingleInputBlockEntity single) {
                single.setRequiredItem(riddle.getRequiredItems().get(0));
                single.markDirty();
            } else if (be instanceof DoubleInputBlockEntity dbl) {
                dbl.setRequiredItems(riddle.getRequiredItems().get(0), riddle.getRequiredItems().get(1));
                dbl.markDirty();
            }

            markDirty();
            this.sync();
        }
    }

    private void addRiddleBookToLectern(ServerPlayerEntity player) {
        if (world == null || world.isClient) return;
        UUID playerID = player.getUuid();

        RiddleDataManager handler = RiddleDataManager.get(player.getWorld(), BeyondTheBlock.RIDDLE_COMPONENTS);
        Riddle riddle = handler.getRiddle(playerID);
        Integer slot = getSlotForPlayer(playerID);
        Direction direction = slot != null ? headDirections.getOrDefault(slot, Direction.NORTH) : null;
        ;

        if (riddle == null || direction == null) {
            //EmeraldEmpire.LOGGER.warn("Unable to place riddle book — missing riddle or direction");
            return;
        }

        ItemStack riddleBook = createRiddleBook(riddle);
        BlockPos lecternPos = lecternMap.get(direction);
        BlockState state = world.getBlockState(lecternPos);
        BlockEntity entity = world.getBlockEntity(lecternPos);


        if (state.getBlock() instanceof LecternBlock
                && entity instanceof LecternBlockEntity lectern) {
            placeBookInLectern(lectern, lecternPos, state, riddleBook);
        }else {
            placeNewLecternWithBook(lecternPos, riddleBook);
        }
     //   EmeraldEmpire.LOGGER.warn("Riddle: {}, Slot: {}, Direction: {}", riddle, slot, direction);
    }

    private void placeBookInLectern(LecternBlockEntity lectern, BlockPos pos, BlockState state, ItemStack book) {
        lectern.setBook(book.copy());
        lectern.markDirty();
        if (world != null) {
            world.setBlockState(pos, state.with(LecternBlock.HAS_BOOK, true), Block.NOTIFY_ALL);
        }
        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
        //EmeraldEmpire.LOGGER.info("Set riddle book at: {}", pos);
    }

    private void placeNewLecternWithBook(BlockPos pos, ItemStack book) {
        BlockState newState = Blocks.LECTERN.getDefaultState();
        if (world != null) {
            world.setBlockState(pos, newState);
        }

        BlockEntity newEntity = world.getBlockEntity(pos);
        if (newEntity instanceof LecternBlockEntity lectern) {
            lectern.setBook(book.copy());
            lectern.markDirty();
            BlockState updatedState = world.getBlockState(pos);
            world.setBlockState(pos, updatedState.with(LecternBlock.HAS_BOOK, true), Block.NOTIFY_ALL);
            world.updateListeners(pos, updatedState, updatedState, Block.NOTIFY_ALL);
           // EmeraldEmpire.LOGGER.info("Placed new lectern and set riddle book at: {}", pos);
        } else {
           // EmeraldEmpire.LOGGER.error("Failed to create LecternBlockEntity at: {}", pos);
        }
    }


    private ItemStack createRiddleBook(Riddle riddle) {
        ItemStack bookItem = new ItemStack(Items.WRITTEN_BOOK);
        NbtCompound tag = new NbtCompound();
        NbtList pageList = new NbtList();

        for (String pageText : riddle.getPages()) {
            pageList.add(NbtString.of(Text.Serializer.toJson(Text.literal(pageText))));
        }

        tag.putString("title", "Riddle");
        tag.putString("author", "The Ancients");
        tag.put("pages", pageList);
        bookItem.setNbt(tag);
        world.playSound(null, pos, ModSounds.RIDDLE_GENERATED, SoundCategory.BLOCKS, 1f, 1f);
        return bookItem;
    }


    public static void tick(World world, BlockPos pos, BlockState state, ShrineHeadsBlockEntity blockEntity) {
        if (world.isClient) return;
        if (!(world instanceof ServerWorld serverWorld)) return;

        RiddleDataManager handler = RiddleDataManager.get(serverWorld, BeyondTheBlock.RIDDLE_COMPONENTS);

        if (!blockEntity.initialised || blockEntity.needsSync(handler.getLastUpdated())) {
            blockEntity.initialised = true;
            int index = 0;


            // Get the active riddles for all players
            for (UUID uuid : handler.getActiveRiddles().keySet()) {
                // Avoid exceeding the number of slots available for heads (e.g., 4)
                if (index >= blockEntity.items.size()) break;

                for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                    UUID id = player.getUuid();

                    if (id.equals(uuid)) {
                        blockEntity.loadShrineHeads(player);
                        blockEntity.markDirty();
                        blockEntity.sync();
                        //   EmeraldEmpire.LOGGER.info("Updating Player Head: {}", player.getName().getString());
                        index++; // Increment index to move to the next slot
                        break;
                    }
                }
            }
            blockEntity.markDirty();
            blockEntity.sync();
            blockEntity.updateLastSynced(handler.getLastUpdated());
        }
    }


    public static ItemStack createPlayerHeadItem(ServerPlayerEntity player) {
        GameProfile profile = player.getGameProfile(); // Contains UUID and name
        MinecraftServer server = player.getServer();

        // Fill profile with texture properties (skin)
        if (server != null) {
            server.getSessionService().fillProfileProperties(profile, true);
        }

        ItemStack head = new ItemStack(Items.PLAYER_HEAD);
        NbtCompound tag = new NbtCompound();

        NbtCompound skullOwner = new NbtCompound();
        if (profile.getId() != null) skullOwner.putString("Id", profile.getId().toString());
        if (profile.getName() != null) skullOwner.putString("Name", profile.getName());

        if (profile.getProperties().containsKey("textures")) {
            NbtCompound properties = new NbtCompound();
            NbtList texturesList = new NbtList();

            for (Property texture : profile.getProperties().get("textures")) {
                NbtCompound textureTag = new NbtCompound();
                textureTag.putString("Value", texture.getValue());
                if (texture.getSignature() != null) {
                    textureTag.putString("Signature", texture.getSignature());
                }
                texturesList.add(textureTag);
            }

            properties.put("textures", texturesList);
            skullOwner.put("Properties", properties);
        }

        tag.put("SkullOwner", skullOwner);
        head.setNbt(tag);
        return head;
    }


    public void sync() {
        if (world != null && !world.isClient) {
            // Mark the block as dirty in the world
            world.markDirty(pos);

            // Update listeners and notify the world of the change
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);

            // Mark the chunk for update if in a server world
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.getChunkManager().markForUpdate(pos);
            }

            // Sync block entity changes
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null) {
                blockEntity.markDirty();
                // Ensure listeners for the block entity are updated
                world.updateListeners(pos, blockEntity.getCachedState(), blockEntity.getCachedState(), Block.NOTIFY_ALL);
            }

            // Re-set the block state if needed (e.g., if you changed the state itself)
            BlockState state = world.getBlockState(pos);
            world.setBlockState(pos, state, Block.FORCE_STATE, Block.NOTIFY_ALL);

            // Log the state of the inventory slots after syncing
            //   System.out.println("Syncing head block entity, Slot 1: " + getStack(0) + ", Slot 2: " + getStack(1) + ", Slot 3: " + getStack(2) + ", Slot 4: " + getStack(3));
        }
    }


    @Override
    public DefaultedList<ItemStack> getItems() {
        // EmeraldEmpire.LOGGER.info("Items: {}", items.toString());
        return items;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, items);

        NbtList directionList = new NbtList();
        for (Map.Entry<Integer, Direction> entry : headDirections.entrySet()) {
            NbtCompound dirTag = new NbtCompound();
            dirTag.putInt("Slot", entry.getKey());
            dirTag.putString("Direction", entry.getValue().getName());
            directionList.add(dirTag);
        }

        nbt.put("HeadDirections", directionList);

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        items = DefaultedList.ofSize(4, ItemStack.EMPTY);
        //  EmeraldEmpire.LOGGER.info("Client readNbt: {}", nbt);

        Inventories.readNbt(nbt, items);

        headDirections.clear();
        if (nbt.contains("HeadDirections", NbtElement.LIST_TYPE)) {
            NbtList directionList = nbt.getList("HeadDirections", NbtElement.COMPOUND_TYPE);
            for (NbtElement element : directionList) {
                if (element instanceof NbtCompound dirTag) {
                    int slot = dirTag.getInt("Slot");
                    Direction direction = Direction.byName(dirTag.getString("Direction"));
                    if (direction != null) {
                        headDirections.put(slot, direction);
                    }
                }
            }
        }

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

}
