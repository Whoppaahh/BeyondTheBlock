package net.ryan.beyond_the_block.item.AnimatedItem;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.screen.Handlers.StaffScreenHandler;
import net.ryan.beyond_the_block.world.Dimension.ModDimensions;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static net.ryan.beyond_the_block.network.PacketIDs.TELEPORT_WITH_STAFF_ID;

public class AnimatedItem extends Item implements IAnimatable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static boolean wasPressedLastTick = false;

    public AnimatedItem(Settings settings) {
        super(settings);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(
                new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        // Check if player is in the cached dimension before allowing interaction
        if (!world.isClient) {
            NbtCompound nbt = stack.getOrCreateNbt();
            if (nbt.contains("ReturnDimension")) {
                // Retrieve the cached dimension from NBT
                String cachedDim = nbt.getString("ReturnDimension");
                RegistryKey<World> currentDim = world.getRegistryKey();

                // If the player is not in the cached dimension, prevent interaction
                if (!currentDim.getValue().toString().equals(cachedDim)) {
                    return TypedActionResult.fail(stack); // Lock the inventory slot
                }
            }
        }

        if (!world.isClient && player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.openHandledScreen(new NamedScreenHandlerFactory() {
                @Override
                public Text getDisplayName() {
                    return Text.of("Teleportation Staff");
                }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity p) {
                    return new StaffScreenHandler(syncId, inv, stack);
                }
            });

            return TypedActionResult.consume(stack);
        }

        return TypedActionResult.consume(stack);
    }


    public static void teleportWithStaff(ServerPlayerEntity serverPlayer, ItemStack heldStack) {

        World world = serverPlayer.getWorld();
        if (world.isClient) return;


        MinecraftServer server = world.getServer();

        NbtCompound nbt = heldStack.getOrCreateNbt();
        RegistryKey<World> currentDim = world.getRegistryKey();
        RegistryKey<World> destinationKey = null;

        // Check if there is a tool inside the staff's inventory (slot 0)
        ItemStack stored = ItemStack.EMPTY;
        if (heldStack.hasNbt() && nbt.contains("StoredTool")) {
             stored = ItemStack.fromNbt(nbt.getCompound("StoredTool"));

            // Check for Pickaxe or Axe in the staff's inventory
            if (stored.getItem().getTranslationKey().contains("pickaxe")) {
                destinationKey = ModDimensions.MINING_DIM_KEY;
            } else if (stored.getItem().getTranslationKey().contains("axe")) {
                destinationKey = ModDimensions.CHOPPING_DIM_KEY;
            }
        }

        // If no tool found in the staff's inventory, don't teleport
        if (destinationKey == null) return;


        // If already in destination and "ReturnDimension" exists, go back
        if (currentDim.equals(destinationKey) && nbt.contains("ReturnDimension")) {
            Identifier returnId = new Identifier(nbt.getString("ReturnDimension"));
            RegistryKey<World> returnKey = RegistryKey.of(Registry.WORLD_KEY, returnId);
            ServerWorld returnWorld = server.getWorld(returnKey);

            if (returnWorld != null) {
                double x = nbt.getDouble("ReturnX");
                double y = nbt.getDouble("ReturnY");
                double z = nbt.getDouble("ReturnZ");
                nbt.remove("ReturnDimension");
                nbt.remove("ReturnX");
                nbt.remove("ReturnY");
                nbt.remove("ReturnZ");

                serverPlayer.teleport(returnWorld, x, y, z, serverPlayer.getYaw(), serverPlayer.getPitch());
            }
        } else {
            // Save current dimension before going to target dimension
            BlockPos pos = serverPlayer.getBlockPos();
            nbt.putString("ReturnDimension", currentDim.getValue().toString());
            nbt.putDouble("ReturnX", pos.getX() + 0.5);
            nbt.putDouble("ReturnY", pos.getY());
            nbt.putDouble("ReturnZ", pos.getZ() + 0.5);

            ServerWorld targetWorld = server.getWorld(destinationKey);
            if (targetWorld != null) {
                BlockPos dest = getDest(pos, targetWorld, true);
                serverPlayer.teleport(targetWorld, dest.getX() + 0.5, dest.getY(), dest.getZ() + 0.5,
                        serverPlayer.getYaw(), serverPlayer.getPitch());

                // 💥 Handle tool durability
                if (stored.isDamageable()) {
                    if (currentDim == World.OVERWORLD) {
                        // Allow breakage
                        stored.damage(1, serverPlayer, p -> p.sendToolBreakStatus(Hand.MAIN_HAND));
                    } else {
                        // Prevent breakage: damage only if current > 1
                        if (stored.getDamage() < stored.getMaxDamage() - 1) {
                            stored.setDamage(stored.getDamage() + 1);
                        }
                    }
                    // Save updated tool back into NBT
                    nbt.put("StoredTool", stored.writeNbt(new NbtCompound()));
                }
            }
        }
    }


    public static BlockPos getDest(BlockPos pos, World destWorld, boolean isInDimension) {
        double y = 61;

        if (!isInDimension) {
            y = pos.getY();
        }

        BlockPos destPos = new BlockPos(pos.getX(), y, pos.getZ());
        int tries = 0;
        while ((!destWorld.getBlockState(destPos).isAir() && !destWorld.getBlockState(destPos)
                .canBucketPlace(Fluids.WATER)) &&
                (!destWorld.getBlockState(destPos.up()).isAir() && !destWorld.getBlockState(destPos.up())
                        .canBucketPlace(Fluids.WATER)) && tries < 25) {
            destPos = destPos.up(2);
            tries++;
        }

        return destPos;
    }

    public static void handleLeftClick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (client.currentScreen != null) return; // Don't trigger in GUI

        long window = client.getWindow().getHandle();
        boolean isPressed = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS;

        // Detect fresh press, not hold
        if (isPressed && !wasPressedLastTick) {
            ItemStack heldStack = client.player.getMainHandStack();

            if (heldStack.getItem() instanceof AnimatedItem) {
                //EmeraldEmpire.LOGGER.info("Left Clicked Air With Animated Item");

                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                ClientPlayNetworking.send(TELEPORT_WITH_STAFF_ID, buf);
            }
        }
        wasPressedLastTick = isPressed;
    }
}
