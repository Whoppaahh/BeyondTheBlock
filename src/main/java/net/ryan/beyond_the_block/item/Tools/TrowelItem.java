package net.ryan.beyond_the_block.item.Tools;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.screen.Handlers.TrowelScreenHandler;
import net.ryan.beyond_the_block.utils.GUI.TrowelInventory;

import java.util.List;
import java.util.stream.Collectors;

public class TrowelItem extends Item {

    public TrowelItem(Settings settings) {
        super(settings);
    }

    /**
     * Opens the trowel inventory GUI on right-click.
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!world.isClient) {
            player.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayerEntity p, PacketByteBuf buf) {
                    buf.writeItemStack(stack);
                }

                @Override
                public Text getDisplayName() {
                    return Text.literal("Trowel");
                }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new TrowelScreenHandler(syncId, inv, stack);
                }
            });
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    /**
     * Places a random block from the trowel's 5-slot inventory.
     */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;

        PlayerEntity player = context.getPlayer();
        ItemStack trowel = context.getStack();
        BlockPos pos = context.getBlockPos();
        Direction side = context.getSide();

        // Read internal trowel inventory
        DefaultedList<ItemStack> inventory = TrowelInventory.readStacks(trowel);

        // Collect valid block items
        List<ItemStack> candidates = inventory.stream()
                .filter(stack -> !stack.isEmpty() && stack.getItem() instanceof BlockItem)
                .collect(Collectors.toList());

        if (candidates.isEmpty()) {
            if (player != null) {
                player.sendMessage(Text.literal("Trowel is empty"), true);
            }
            return ActionResult.FAIL;
        }

        // Choose a random block to place
        ItemStack chosen = candidates.get(world.random.nextInt(candidates.size()));

        BlockItem blockItem = (BlockItem) chosen.getItem();

        // Create proper placement context
        ItemPlacementContext placementContext = new ItemPlacementContext(
                player,
                context.getHand(),
                chosen.copy(), // must pass a copy
                new BlockHitResult(context.getHitPos(), side, pos, context.hitsInsideBlock())
        );

        // Try to place the block
        ActionResult result = blockItem.place(placementContext);
        if (!result.isAccepted()) {
            return result;
        }

        // Placement succeeded: decrement item inside internal storage
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack invStack = inventory.get(i);
            if (!invStack.isEmpty() && invStack.getItem() == chosen.getItem()) {
                invStack.decrement(1);
                break;
            }
        }

        // Save the updated inventory to NBT
        TrowelInventory.writeStacks(trowel, inventory);

        return result;
    }
}
