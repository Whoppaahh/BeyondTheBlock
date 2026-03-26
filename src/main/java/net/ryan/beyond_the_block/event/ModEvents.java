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
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.client.hud.BreedingHUDRenderer;
import net.ryan.beyond_the_block.client.hud.FollowerHudRenderer;
import net.ryan.beyond_the_block.client.hud.TrajectoryHUD;
import net.ryan.beyond_the_block.client.visual.Glowable;
import net.ryan.beyond_the_block.content.block.Cauldrons.DyedWaterCauldronBlock;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.block.PlayerVaultBlock;
import net.ryan.beyond_the_block.content.blockentity.DyedWaterCauldronBlockEntity;
import net.ryan.beyond_the_block.content.blockentity.InfiFurnaceBlockEntity;
import net.ryan.beyond_the_block.content.effect.FreezeEffectLayer;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import net.ryan.beyond_the_block.content.enchantment.Armour.helmets.IronCladVisionEnchantment;
import net.ryan.beyond_the_block.content.enchantment.Armour.helmets.MindWardEnchantment;
import net.ryan.beyond_the_block.content.enchantment.Armour.helmets.ShadowsVeilEnchantment;
import net.ryan.beyond_the_block.content.enchantment.Armour.leggings.NightstrideEnchantment;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.content.enchantment.MyEnchantmentHelper;
import net.ryan.beyond_the_block.content.entity.CupidArrowEntity;
import net.ryan.beyond_the_block.content.entity.ModEntities;
import net.ryan.beyond_the_block.content.entity.SheepColours;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.event.entity.EntityEventRegistrar;
import net.ryan.beyond_the_block.event.player.PlayerEventRegistrar;
import net.ryan.beyond_the_block.event.server.ServerLifecycleRegistrar;
import net.ryan.beyond_the_block.event.world.WorldEventRegistrar;
import net.ryan.beyond_the_block.feature.cauldrons.*;
import net.ryan.beyond_the_block.feature.paths.PathSpeedHelper;
import net.ryan.beyond_the_block.feature.projectile.ArrowHitsAccess;
import net.ryan.beyond_the_block.utils.Accessors.FurnaceAccessor;
import net.ryan.beyond_the_block.utils.FlameTrailPoint;
import net.ryan.beyond_the_block.utils.Helpers.*;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static net.ryan.beyond_the_block.client.hud.FollowerHudRenderer.getFollowers;

public class ModEvents {

    public static void register() {
        PlayerEventRegistrar.register();
        WorldEventRegistrar.register();
        EntityEventRegistrar.register();
        ServerLifecycleRegistrar.register();
        HiveTracker.init();
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



}
