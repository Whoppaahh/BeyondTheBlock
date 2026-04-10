package net.ryan.beyond_the_block.content.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.registry.ModEntities;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.content.registry.family.ModBoatVariant;

public class BoatEntity extends net.minecraft.entity.vehicle.BoatEntity {
    private static final TrackedData<Integer> VARIANT =
            DataTracker.registerData(BoatEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public BoatEntity(EntityType<? extends net.minecraft.entity.vehicle.BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public BoatEntity(World world, double x, double y, double z) {
        this(ModEntities.MOD_BOAT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, 0);
    }

    public void setVariant(ModBoatVariant variant) {
        this.dataTracker.set(VARIANT, variant.ordinal());
    }

    public ModBoatVariant getVariant() {
        return ModBoatVariant.byId(this.dataTracker.get(VARIANT));
    }

    @Override
    public Item asItem() {
        return switch (this.getVariant()) {
            case CHERRY -> ModItems.CHERRY_BOAT;
            case PALE_OAK -> ModItems.PALE_OAK_BOAT;
            case BAMBOO -> ModItems.BAMBOO_RAFT;
        };
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
            String name = nbt.getString("Variant");
            for (ModBoatVariant variant : ModBoatVariant.values()) {
                if (variant.asString().equals(name)) {
                    this.setVariant(variant);
                    return;
                }
            }
        }
        this.setVariant(ModBoatVariant.CHERRY);
    }
}
