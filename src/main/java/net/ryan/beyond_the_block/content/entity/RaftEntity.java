package net.ryan.beyond_the_block.content.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.registry.ModEntities;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.content.registry.family.ModBoatVariant;

public class RaftEntity extends BoatEntity {
    private static final TrackedData<Integer> VARIANT =
            DataTracker.registerData(RaftEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public RaftEntity(EntityType<? extends BoatEntity> type, World world) {
        super(type, world);
    }

    public RaftEntity(World world, double x, double y, double z) {
        this(ModEntities.MOD_RAFT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, ModBoatVariant.BAMBOO.ordinal());
    }

    public ModBoatVariant getVariant() {
        return ModBoatVariant.byId(this.dataTracker.get(VARIANT));
    }

    public void setVariant(ModBoatVariant variant) {
        this.dataTracker.set(VARIANT, variant.ordinal());
    }

    @Override
    public Item asItem() {
        return ModItems.BAMBOO_RAFT;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Variant", this.getVariant().asString());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Variant")) {
            String value = nbt.getString("Variant");
            for (ModBoatVariant variant : ModBoatVariant.values()) {
                if (variant.asString().equals(value)) {
                    this.setVariant(variant);
                    return;
                }
            }
        }
        this.setVariant(ModBoatVariant.BAMBOO);
    }

    @Override
    public void updatePassengerPosition(net.minecraft.entity.Entity passenger) {
        if (!this.hasPassenger(passenger)) {
            return;
        }

        int index = this.getPassengerList().indexOf(passenger);

        float xOffset = index == 0 ? 0.2F : -0.6F;
        float yOffset = 0.35F; // 👈 THIS is the important fix
        float zOffset = 0.0F;

        net.minecraft.util.math.Vec3d offset = new net.minecraft.util.math.Vec3d(xOffset, 0.0D, zOffset)
                .rotateY(-this.getYaw() * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));

        passenger.setPosition(
                this.getX() + offset.x,
                this.getY() + this.getMountedHeightOffset() + passenger.getHeightOffset() + yOffset,
                this.getZ() + offset.z
        );
    }
}