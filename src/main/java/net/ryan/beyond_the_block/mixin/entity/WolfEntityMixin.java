package net.ryan.beyond_the_block.mixin.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.entity.WolfArmourHolder;
import net.ryan.beyond_the_block.content.item.armour.WolfArmourItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends TameableEntity implements WolfArmourHolder {
    @Shadow public abstract boolean isBreedingItem(ItemStack stack);

    @Unique
    private static final TrackedData<ItemStack> BTB_WOLF_ARMOUR =
            DataTracker.registerData(WolfEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    @Unique
    private static final UUID BTB_WOLF_ARMOUR_MODIFIER_UUID =
            UUID.fromString("7b85b1f7-0199-47a1-935d-672d1fe67f4a");

    protected WolfEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void btb$initWolfArmourTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(BTB_WOLF_ARMOUR, ItemStack.EMPTY);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void btb$writeWolfArmour(NbtCompound nbt, CallbackInfo ci) {
        ItemStack armour = this.btb$getWolfArmour();

        if (!armour.isEmpty()) {
            nbt.put("BTBWolfArmour", armour.writeNbt(new NbtCompound()));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void btb$readWolfArmour(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("BTBWolfArmour", 10)) {
            this.btb$setWolfArmour(ItemStack.fromNbt(nbt.getCompound("BTBWolfArmour")));
        } else {
            this.btb$setWolfArmour(ItemStack.EMPTY);
        }
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void btb$interactWolfArmour(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack held = player.getStackInHand(hand);
        ItemStack equipped = this.btb$getWolfArmour();
        WolfEntity self = (WolfEntity) (Object) this;

        if (!self.isTamed() || self.isBaby()) {
            return;
        }

        @Nullable LivingEntity owner = this.getOwner();
        boolean isOwner = owner != null && owner.getUuid().equals(player.getUuid());

        if (!isOwner) {
            return;
        }

        if (held.isOf(Items.SHEARS) && !equipped.isEmpty()) {
            if (!this.getWorld().isClient) {
                this.btb$dropWolfArmour();
                held.damage(1, player, p -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }

            cir.setReturnValue(ActionResult.success(this.getWorld().isClient));
            return;
        }

        if (!equipped.isEmpty()
                && equipped.getItem() instanceof WolfArmourItem wolfArmourItem
                && equipped.isDamaged()
                && wolfArmourItem.canRepair(equipped, held)) {
            if (!this.getWorld().isClient) {
                equipped.setDamage(Math.max(0, equipped.getDamage() - 32));
                this.btb$setWolfArmour(equipped.copy());

                if (!player.getAbilities().creativeMode) {
                    held.decrement(1);
                }

                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.NEUTRAL, 0.7F, 1.4F);
            }

            cir.setReturnValue(ActionResult.success(this.getWorld().isClient));
            return;
        }

        if (held.getItem() instanceof WolfArmourItem && equipped.isEmpty()) {
            if (!this.getWorld().isClient) {
                ItemStack toEquip = held.copy();
                toEquip.setCount(1);
                this.btb$setWolfArmour(toEquip);

                if (!player.getAbilities().creativeMode) {
                    held.decrement(1);
                }

                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }

            cir.setReturnValue(ActionResult.success(this.getWorld().isClient));
        }
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void btb$damageWolfArmourBeforeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.getWorld().isClient) return;
        if (amount <= 0.0F) return;
        if (source.isOutOfWorld()) return;

        ItemStack armour = this.btb$getWolfArmour();
        if (armour.isEmpty()) return;

        int itemDamage = Math.max(1, (int) Math.floor(amount));
        armour.damage(itemDamage, (LivingEntity) (Object) this, entity -> {});

        if (armour.isEmpty() || armour.getDamage() >= armour.getMaxDamage()) {
            this.btb$setWolfArmour(ItemStack.EMPTY);
            this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        } else {
            this.btb$setWolfArmour(armour.copy());
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void btb$dropArmourOnDeath(DamageSource source, CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            this.btb$dropWolfArmour();
        }
    }



    @Override
    public ItemStack btb$getWolfArmour() {
        return this.dataTracker.get(BTB_WOLF_ARMOUR);
    }

    @Override
    public void btb$setWolfArmour(ItemStack stack) {
        this.dataTracker.set(BTB_WOLF_ARMOUR, stack.copy());
        this.btb$refreshArmourAttribute();
    }

    @Override
    public boolean btb$hasWolfArmour() {
        return !this.btb$getWolfArmour().isEmpty();
    }

    @Unique
    private void btb$dropWolfArmour() {
        ItemStack armour = this.btb$getWolfArmour();
        if (armour.isEmpty()) return;

        this.dropStack(armour.copy());
        this.btb$setWolfArmour(ItemStack.EMPTY);
    }

    @Unique
    private void btb$refreshArmourAttribute() {
        EntityAttributeInstance attribute = this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
        if (attribute == null) return;

        EntityAttributeModifier existing = attribute.getModifier(BTB_WOLF_ARMOUR_MODIFIER_UUID);
        if (existing != null) {
            attribute.removeModifier(existing);
        }

        ItemStack armour = this.btb$getWolfArmour();

        if (!armour.isEmpty() && armour.getItem() instanceof WolfArmourItem wolfArmourItem) {
            attribute.addPersistentModifier(new EntityAttributeModifier(
                    BTB_WOLF_ARMOUR_MODIFIER_UUID,
                    "btb_wolf_armour",
                    wolfArmourItem.getProtection(),
                    EntityAttributeModifier.Operation.ADDITION
            ));
        }
    }
}