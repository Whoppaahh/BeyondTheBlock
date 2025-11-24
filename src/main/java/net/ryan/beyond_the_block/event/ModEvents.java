package net.ryan.beyond_the_block.event;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.block.Entity.InfiFurnaceBlockEntity;
import net.ryan.beyond_the_block.block.ModBlocks;
import net.ryan.beyond_the_block.block.PlayerVaultBlock;
import net.ryan.beyond_the_block.effect.FreezeEffectLayer;
import net.ryan.beyond_the_block.effect.ModEffects;
import net.ryan.beyond_the_block.enchantment.Armour.helmets.IronCladVisionEnchantment;
import net.ryan.beyond_the_block.enchantment.Armour.helmets.MindWardEnchantment;
import net.ryan.beyond_the_block.enchantment.Armour.helmets.ShadowsVeilEnchantment;
import net.ryan.beyond_the_block.enchantment.Armour.leggings.NightstrideEnchantment;
import net.ryan.beyond_the_block.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.enchantment.MyEnchantmentHelper;
import net.ryan.beyond_the_block.entity.CupidArrowEntity;
import net.ryan.beyond_the_block.entity.SheepColours;
import net.ryan.beyond_the_block.item.ModItems;
import net.ryan.beyond_the_block.utils.Accessors.FurnaceAccessor;
import net.ryan.beyond_the_block.utils.FlameTrailPoint;
import net.ryan.beyond_the_block.utils.GUI.FollowerHudRenderer;
import net.ryan.beyond_the_block.utils.Helpers.*;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static net.ryan.beyond_the_block.utils.GUI.FollowerHudRenderer.getFollowers;

public class ModEvents {

    public static void register() {
        registerPlayerEvents();
        registerBlockConversions();
        registerServerTickEvents();
        registerEntityEvents();
    }

    private static void registerPlayerEvents() {
        AttackBlockCallback.EVENT.register(ModEvents::onBlockMined);
        AttackEntityCallback.EVENT.register(ModEvents::onEntityAttacked);
        PlayerBlockBreakEvents.BEFORE.register(ModEvents::onBlockBreak);
        UseBlockCallback.EVENT.register(ModEvents::onBlockUsed);
        UseEntityCallback.EVENT.register(ModEvents::onEntityUsed);

        PlayerBlockBreakEvents.BEFORE.register(PlayerVaultBlock::handleBreak);
    }

    private static ActionResult onEntityUsed(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        if (world.isClient) return ActionResult.PASS;

        if (!(entity instanceof ArmorStandEntity armorStand)) return ActionResult.PASS;

        if (player.isSneaking()) {
            transferArmourPartial(player, armorStand, world);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private static void registerBlockConversions() {
        BlockConversionHandler.register();
        UseBlockCallback.EVENT.register(ModEvents::handleBlockConversion);

    }

    private static void registerServerTickEvents() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            NightstrideEnchantment.registerTickHandler(world);
            IronCladVisionEnchantment.registerTickHandler(world);
            MindWardEnchantment.registerTickHandler(world);
            ShadowsVeilEnchantment.registerTickHandler(world);
          //  SpiderCobwebTrailGoal.decayCobwebs(world);
           // SnowHelper.tickSnow(world);
            RestoreManager.tick(world);
        });
        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
            try {
                Path worldDatapacks = minecraftServer.getSavePath(WorldSavePath.DATAPACKS);
                Path targetDatapack = worldDatapacks.resolve("beyond_the_block");

                boolean installed = false;
                if (!Files.exists(targetDatapack)) {
                    Files.createDirectories(targetDatapack);

                    // Copy pack.mcmeta
                    copyResource("/beyond_the_block/pack.mcmeta", targetDatapack.resolve("pack.mcmeta"));

                    // Copy setup.mcfunction
                    Path dataFunctions = targetDatapack.resolve("data/death_counter/functions");
                    Files.createDirectories(dataFunctions);
                    copyResource("/beyond_the_block/data/beyond_the_block/functions/setup.mcfunction",
                            dataFunctions.resolve("setup.mcfunction"));

                    // Copy load.json
                    Path tagFunctions = targetDatapack.resolve("data/minecraft/tags/functions");
                    Files.createDirectories(tagFunctions);
                    copyResource("/beyond_the_block/data/minecraft/tags/functions/load.json",
                            tagFunctions.resolve("load.json"));

                    installed = true;
                    BeyondTheBlock.LOGGER.info("[DeathCounterMod] Datapack installed successfully!");
                }
                // Reload datapacks to ensure it's active immediately
                if (installed) {
                    minecraftServer.getCommandManager().executeWithPrefix(minecraftServer.getCommandSource(),
                            "reload");
                    BeyondTheBlock.LOGGER.info("[DeathCounterMod] Datapack reloaded!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
       // ServerTickEvents.END_SERVER_TICK.register(PathSpeedHelper::tickSpeed);
       // ServerChunkEvents.CHUNK_UNLOAD.register(SnowHelper::clearBitMark);
        ServerEntityEvents.ENTITY_LOAD.register(SheepColours::randomiseColours);
    }


    private static void copyResource(String resourcePath, Path target) throws IOException {
        try (InputStream in = BeyondTheBlock.class.getResourceAsStream(resourcePath)) {
            if (in == null) throw new IOException("Resource not found: " + resourcePath);
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }


    private static void registerEntityEvents() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(ModEvents::dropArrows);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(CupidArrowEntity::onDamage);
        ServerLivingEntityEvents.AFTER_DEATH.register(FreezeEffectLayer::onDeath);
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            List<LivingEntity> followers = getFollowers(client.player);
            if (followers.isEmpty()) return;

            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            int entryHeight = 32;
            int totalHeight = followers.size() * entryHeight;
            int startY = (screenHeight / 2) - (totalHeight / 2);
            int x = 15; // left margin

            for (LivingEntity entity : followers) {
                FollowerHudRenderer.renderFollower(context, entity, x, startY);
                startY += entryHeight;
            }
        });
    }

    // -------------------
    // BLOCK EVENT HANDLERS
    // -------------------
    private static ActionResult onBlockMined(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
        ItemStack stack = player.getStackInHand(hand);
        int miningLevel = EnchantmentHelper.getLevel(ModEnchantments.STONE_BREAKER, stack);
        if (miningLevel > 0) {
            MyEnchantmentHelper.handleInstantMining(player, stack, pos, world, world.getBlockState(pos));
        }
        return ActionResult.PASS;
    }

    private static ActionResult onBlockUsed(PlayerEntity player, World world, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockPos placePos = hit.getBlockPos().offset(hit.getSide());
            if (world.getFluidState(placePos).isOf(Fluids.LAVA) && world.getFluidState(placePos).isStill()) {
                queueAdjacentSand(world, placePos.toImmutable());
            }
        }

        ItemStack stack = player.getStackInHand(hand);
        int tillLevel = EnchantmentHelper.getLevel(ModEnchantments.DEEP_TILL, stack);
        if (tillLevel > 0) {
            MyEnchantmentHelper.tillArea(world, hit.getBlockPos(), tillLevel);
            return ActionResult.SUCCESS;
        }

        int barkskinLevel = EnchantmentHelper.getLevel(ModEnchantments.BARKSKIN, stack);
        if (barkskinLevel > 0) {
            MyEnchantmentHelper.handleTreeStripping(player, stack, hit.getBlockPos(), world);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private static void transferArmourPartial(PlayerEntity player, ArmorStandEntity armourStand, World world) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;

            ItemStack standItem = armourStand.getEquippedStack(slot);
            ItemStack playerItem = player.getEquippedStack(slot);

            // Armor stand → player (if player slot empty)
            if (!standItem.isEmpty() && playerItem.isEmpty()) {
                player.equipStack(slot, standItem.copy());
                armourStand.equipStack(slot, ItemStack.EMPTY);
            }
            // Player → armor stand (if armor stand slot empty)
             else if (standItem.isEmpty() && !playerItem.isEmpty()) {
                armourStand.equipStack(slot, playerItem.copy());
                player.equipStack(slot, ItemStack.EMPTY);
            }
            // Swap
             else if (!standItem.isEmpty() && !playerItem.isEmpty()) {
                ItemStack temp = playerItem.copy();
                player.equipStack(slot, standItem.copy());
                armourStand.equipStack(slot, temp);
            }
        }

        // Optional feedback sound
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1f, 1f);
    }


    private static boolean onBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

        int choppingLevel = EnchantmentHelper.getLevel(ModEnchantments.TIMBER_CUT, stack);
        if (choppingLevel > 0) {
            MyEnchantmentHelper.handleTreeBreaking(player, stack, pos, world);
            return false;
        }

        int flameSweepLevel = EnchantmentHelper.getLevel(ModEnchantments.FLAME_SWEEP, stack);
        if (flameSweepLevel > 0 && stack.getItem() instanceof AxeItem && state.isIn(BlockTags.LOGS_THAT_BURN)) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            Block.dropStack(world, pos, new ItemStack(Items.CHARCOAL, flameSweepLevel));
            if (world instanceof ServerWorld sw) {
                sw.spawnParticles(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, 0.2, 0.2, 0.2, 0.05);
            }
        }
        return true;
    }

    private static ActionResult handleBlockConversion(PlayerEntity player, World world, Hand hand, BlockHitResult hit) {
        ActionResult result;

        result = convertToSnow(player, world, hand, hit);
        if (result != ActionResult.PASS) return result;

        result = convertDirtPath(player, world, hand, hit);
        if (result != ActionResult.PASS) return result;

        result = convertCraftingTable(player, world, hand, hit);
        if (result != ActionResult.PASS) return result;

        result = convertFurnace(player, world, hand, hit);
        return result;
    }
    private static ActionResult convertToSnow(PlayerEntity player, World world, Hand hand, BlockHitResult hit) {
        BlockPos clickedPos = hit.getBlockPos();
        BlockState clicked = world.getBlockState(clickedPos);
        ItemStack stack = player.getStackInHand(hand);

        if (world.isClient) return ActionResult.PASS;

        // Check if clicked block is a snow layer
        if (clicked.isOf(Blocks.SNOW)) {
            BlockPos below = clickedPos.down();
            BlockState base = world.getBlockState(below);

            // Only replace if the base block is replaceable or a standard ground block
            if (base.getMaterial().isReplaceable() || base.isOf(Blocks.GRASS_BLOCK) || base.isOf(Blocks.DIRT) || base.isOf(Blocks.SAND)) {
                world.setBlockState(below, Blocks.SNOW_BLOCK.getDefaultState(), 3);
            }
        }

        // Only act if holding a snow block
        if (!(stack.getItem() instanceof BlockItem item && item.getBlock().getDefaultState().isIn(BlockTags.SNOW))) {
            return ActionResult.PASS;
        }

        BlockPos basePos;

        // If clicked block is a “top block” we want to preserve, replace the block below
        if (clicked.isIn(SnowHelper.SNOW_CAN_COVER)) {
            basePos = clickedPos.down();
        } else {
            basePos = clickedPos;
        }

        BlockState baseState = world.getBlockState(basePos);

        // Only replace if base block is replaceable or in SNOW_CAN_COVER
        if (baseState.getMaterial().isReplaceable() || baseState.isIn(SnowHelper.SNOW_CAN_COVER)) {
            world.setBlockState(basePos, Blocks.SNOW_BLOCK.getDefaultState(), 3);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
    private static ActionResult convertDirtPath(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult) {
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);
        BlockState stateBelow = world.getBlockState(pos.down());
        ItemStack stack = playerEntity.getStackInHand(hand);

        // 1. Placing fence/wall on top of path
        if (state.isAir() && stateBelow.isOf(Blocks.DIRT_PATH) && stack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof FenceBlock || block instanceof FenceGateBlock || block instanceof WallBlock) {
                world.setBlockState(pos, block.getDefaultState(), 3); // place fence/wall on top
                return ActionResult.SUCCESS;
            }
        }

        if (stack.getItem() instanceof ShovelItem) {
            // 2. Convert dirt/grass under existing fence/wall to path
            if (!world.isClient && (state.isOf(Blocks.DIRT) || state.isOf(Blocks.GRASS_BLOCK))) {
                BlockState above = world.getBlockState(pos.up());
                if (above.getBlock() instanceof FenceBlock || above.getBlock() instanceof FenceGateBlock || above.getBlock() instanceof WallBlock) {
                    world.setBlockState(pos, Blocks.DIRT_PATH.getDefaultState(), 3);
                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.PASS;
    }
    private static ActionResult convertCraftingTable(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (!world.isClient() && player.isSneaking()) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);

            if (state.getBlock() == Blocks.CRAFTING_TABLE) {
                int requiredXpLevels = 15; // change as needed
                if (player.experienceLevel >= requiredXpLevels) {
                    // Consume XP
                    player.addExperienceLevels(-requiredXpLevels);

                    // Replace with custom table
                    world.setBlockState(pos, ModBlocks.DECRAFTER_BLOCK.getDefaultState());

                    // Optional: play sound/particles
                    world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    ((ServerWorld) world).spawnParticles(ParticleTypes.ENCHANT,
                            pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                            20, 0.3, 0.5, 0.3, 0.1);

                    return ActionResult.SUCCESS;
                } else {
                    player.sendMessage(Text.literal("You need " + requiredXpLevels + " experience levels to upgrade this table.").formatted(Formatting.RED), true);
                    return ActionResult.FAIL;
                }
            }
        }
        return ActionResult.PASS;
    }
    private static ActionResult convertFurnace(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (!(state.getBlock() instanceof FurnaceBlock)) return ActionResult.PASS;
        if (world.isClient()) return ActionResult.SUCCESS;

        ItemStack heldStack = player.getStackInHand(hand);

        // Check for specific item
        if (!(heldStack.isOf(ModItems.ECLIPSED_BLOOM) || heldStack.isOf(Items.NETHERRACK))) return ActionResult.PASS;
        if (!player.isSneaking()) return ActionResult.PASS;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof FurnaceBlockEntity furnaceBE)) return ActionResult.PASS;

        // Check the fuel slot (slot 1 in FurnaceBlockEntity)
        ItemStack fuelStack = furnaceBE.getStack(1);
        if (!(fuelStack.isOf(Items.LAVA_BUCKET))) return ActionResult.PASS;

        ItemStack inputStack = furnaceBE.getStack(0).copy();
        ItemStack outputStack = furnaceBE.getStack(2).copy();

        int cookTime = 0, cookTimeTotal = 0;
        if (furnaceBE instanceof FurnaceAccessor accessor) {
            cookTime = accessor.btb$getCookTime();
            cookTimeTotal = accessor.btb$getCookTimeTotal();
        }

        // Replace the block
        Direction facing = state.get(FurnaceBlock.FACING);
        BlockState newState = ModBlocks.INFI_FURNACE_BLOCK.getDefaultState().with(FurnaceBlock.FACING, facing);
        world.setBlockState(pos, newState, FurnaceBlock.NOTIFY_ALL);

        // Transfer block entity data
        BlockEntity newBE = world.getBlockEntity(pos);
        if (newBE instanceof InfiFurnaceBlockEntity customBE) {
            customBE.setStack(0, inputStack);
            customBE.setStack(1, outputStack);

            // Set cook times if your custom furnace supports it
            customBE.getPropertyDelegate().set(0, cookTime);
            customBE.getPropertyDelegate().set(1, cookTimeTotal);
            customBE.markDirty();
        }

        // Consume the item (optional)
        if (!player.isCreative()) {
            heldStack.decrement(1);
        }

        return ActionResult.SUCCESS;
    }
    // -------------------
    // ENTITY EVENT HANDLERS
    // -------------------
    private static ActionResult onEntityAttacked(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hit) {
        if (!(entity instanceof LivingEntity target)) return ActionResult.PASS;

        ItemStack stack = player.getStackInHand(hand);

        if (EnchantmentHelper.getLevel(ModEnchantments.TEMPORAL_SLICE, stack) > 0) {
            target.addStatusEffect(new StatusEffectInstance(ModEffects.TIME_DILATION, 100, 0, false, false, false));
            return ActionResult.SUCCESS;
        }

        int flameSweepLevel = EnchantmentHelper.getLevel(ModEnchantments.FLAME_SWEEP, stack);
        if (flameSweepLevel > 0 && !world.isClient) {
            target.setOnFireFor(3 + 2 * flameSweepLevel);

            double range = 1.0 + 0.5 * flameSweepLevel;
            world.getEntitiesByClass(LivingEntity.class, entity.getBoundingBox().expand(range, 0.25, range),
                            e -> e != player && e != entity && player.canSee(e))
                    .forEach(e -> e.setOnFireFor(3 + 2 * flameSweepLevel));

            if (world instanceof ServerWorld serverWorld)
                spawnFlameSweepParticles(serverWorld, player, flameSweepLevel, stack);

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private static final List<FlameTrailPoint> TRAIL_POINTS = new ArrayList<>();

    public static void spawnFlameSweepParticles(ServerWorld world, PlayerEntity player, int level, ItemStack stack) {
        if (!player.handSwinging) return;

        float progress = player.getAttackCooldownProgress(0.5F);
        if (progress <= 0) return;

        float baseYaw = player.getYaw();
        double radius = 1.5 + 0.25 * level + (stack.getItem() instanceof AxeItem ? 0.3 : 0);
        float arc = stack.getItem() instanceof AxeItem ? 120F + 25F * level : 100F + 20F * level;
        int particleCount = 3 + level * 2 + (stack.getItem() instanceof AxeItem ? 3 : 0);

        // Dynamic sweep arc
        float swingAngle = -arc / 2F + arc * progress + baseYaw;

        for (int i = 0; i < particleCount; i++) {
            float offset = swingAngle + (world.random.nextFloat() - 0.5F) * 10F;
            double rad = Math.toRadians(offset);

            double x = player.getX() - Math.sin(rad) * radius;
            double y = player.getBodyY(0.5) + (world.random.nextDouble() * 0.3);
            double z = player.getZ() + Math.cos(rad) * radius;

            // --- SWEEP_ATTACK PARTICLE (vanilla sweep arc) ---
            world.spawnParticles(ParticleTypes.SWEEP_ATTACK, x, y, z, 1, 0, 0, 0, 0);

            // --- Add trail point for flame particle ---
            TRAIL_POINTS.add(new FlameTrailPoint(new Vec3d(x, y, z), 5)); // flames last 5 ticks
        }

        // --- Tick existing trail points for fading flames ---
        TRAIL_POINTS.removeIf(point -> point.tick(world));
    }

    // -------------------
    // UTILITIES
    // -------------------
    public static void queueAdjacentSand(World world, BlockPos lavaPos) {
        if (!(world instanceof ServerWorld)) return;
        for (BlockPos neighbor : BlockPos.iterateOutwards(lavaPos, 1, 1, 1)) {
            BlockState state = world.getBlockState(neighbor);
            if (state.isOf(Blocks.SAND) || state.isOf(Blocks.RED_SAND)) {
                    BeyondTheBlock.LOGGER.info("Queuing {} at {}", state.getBlock(), neighbor);
                    SandToGlassManager.queueSand(neighbor.toImmutable(), state);
            }
        }
    }

    private static void dropArrows(ServerWorld world, Entity entity, LivingEntity killed) {
        ArrowHitsAccess acc = (ArrowHitsAccess) killed;
        if (acc.beyondTheBlock$getArrowHits().isEmpty()) return;

        for (NbtCompound data : acc.beyondTheBlock$getArrowHits()) {
            ItemStack drop = switch (data.getString("type")) {
                case "spectral" -> new ItemStack(Items.SPECTRAL_ARROW);
                case "tipped" -> new ItemStack(Items.TIPPED_ARROW);
                default -> new ItemStack(Items.ARROW);
            };
            if (!drop.isEmpty()) world.spawnEntity(new ItemEntity(world, killed.getX(), killed.getY(), killed.getZ(), drop));
        }
        acc.beyondTheBlock$getArrowHits().clear();
    }

}
