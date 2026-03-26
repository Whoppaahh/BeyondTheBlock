package net.ryan.beyond_the_block.mixin.feature.projectiles;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileMixin {

    @Inject(method = "onBlockHit", at = @At("TAIL"))
    private void beyond_the_block$onTargetBlockHit(BlockHitResult hitResult, CallbackInfo ci) {
        PersistentProjectileEntity projectile = (PersistentProjectileEntity)(Object)this;
        World world = projectile.getWorld();

        if (world.isClient) return;

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (!state.isOf(Blocks.TARGET)) return;

        // Vanilla-accurate target center calculation (2D on hit face)
        Vec3d hitPos = hitResult.getPos();
        Vec3d center = Vec3d.ofCenter(pos);

        double dx = Math.abs(hitPos.x - center.x);
        double dy = Math.abs(hitPos.y - center.y);
        double dz = Math.abs(hitPos.z - center.z);

        double dist;
        Direction.Axis axis = hitResult.getSide().getAxis();

        if (axis == Direction.Axis.X) {
            dist = Math.max(dy, dz);
        } else if (axis == Direction.Axis.Y) {
            dist = Math.max(dx, dz);
        } else {
            dist = Math.max(dx, dy);
        }

        // Bullseye threshold (matches vanilla redstone strength 15)
        if (dist > 0.125D) return;

        spawnFireworks((ServerWorld) world, pos);

        Entity owner = projectile.getOwner();
        if (owner instanceof PlayerEntity player) {
            player.addExperience(5);
        }
    }

    @Unique
    private void spawnFireworks(ServerWorld world, BlockPos pos) {
        ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);

        NbtCompound fireworks = new NbtCompound();
        NbtList explosions = new NbtList();

        NbtCompound explosion = new NbtCompound();
        explosion.putByte("Type", (byte) 1); // Star
        explosion.putIntArray("Colors", new int[]{0xFF0000, 0xFFFF00, 0xFFFFFF});

        explosions.add(explosion);
        fireworks.put("Explosions", explosions);
        fireworks.putByte("Flight", (byte) 1);

        stack.getOrCreateSubNbt("Fireworks").copyFrom(fireworks);

        FireworkRocketEntity rocket = new FireworkRocketEntity(
                world,
                pos.getX() + 0.5,
                pos.getY() + 1.1,
                pos.getZ() + 0.5,
                stack
        );

        world.spawnEntity(rocket);
    }
}
