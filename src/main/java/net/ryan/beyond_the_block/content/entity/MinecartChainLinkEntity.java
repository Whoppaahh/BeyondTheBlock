package net.ryan.beyond_the_block.content.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class MinecartChainLinkEntity extends Entity {

    private static final TrackedData<Optional<UUID>> FIRST_CART =
            DataTracker.registerData(MinecartChainLinkEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    private static final TrackedData<Optional<UUID>> SECOND_CART =
            DataTracker.registerData(MinecartChainLinkEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    public MinecartChainLinkEntity(EntityType<? extends MinecartChainLinkEntity> type, World world) {
        super(type, world);
        this.noClip = true;
    }

    public void setFirstCart(UUID uuid) {
        this.dataTracker.set(FIRST_CART, Optional.ofNullable(uuid));
    }

    public void setSecondCart(UUID uuid) {
        this.dataTracker.set(SECOND_CART, Optional.ofNullable(uuid));
    }

    public UUID getFirstCart() {
        return this.dataTracker.get(FIRST_CART).orElse(null);
    }

    public UUID getSecondCart() {
        return this.dataTracker.get(SECOND_CART).orElse(null);
    }

    public Entity getFirstCartEntity() {
        UUID uuid = getFirstCart();
        if (uuid == null || !(this.getWorld() instanceof ServerWorld serverWorld)) {
            return null;
        }
        return serverWorld.getEntity(uuid);
    }

    public Entity getSecondCartEntity() {
        UUID uuid = getSecondCart();
        if (uuid == null || !(this.getWorld() instanceof ServerWorld serverWorld)) {
            return null;
        }
        return serverWorld.getEntity(uuid);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(FIRST_CART, Optional.empty());
        this.dataTracker.startTracking(SECOND_CART, Optional.empty());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("FirstCart")) {
            setFirstCart(nbt.getUuid("FirstCart"));
        }
        if (nbt.containsUuid("SecondCart")) {
            setSecondCart(nbt.getUuid("SecondCart"));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        UUID first = getFirstCart();
        UUID second = getSecondCart();

        if (first != null) {
            nbt.putUuid("FirstCart", first);
        }
        if (second != null) {
            nbt.putUuid("SecondCart", second);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            return;
        }

        Entity first = getFirstCartEntity();
        Entity second = getSecondCartEntity();

        if (first == null || second == null || !first.isAlive() || !second.isAlive()) {
            this.discard();
            return;
        }

        double x = (first.getX() + second.getX()) * 0.5D;
        double y = (first.getY() + second.getY()) * 0.5D;
        double z = (first.getZ() + second.getZ()) * 0.5D;

        this.setPosition(x, y, z);
    }


    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}
