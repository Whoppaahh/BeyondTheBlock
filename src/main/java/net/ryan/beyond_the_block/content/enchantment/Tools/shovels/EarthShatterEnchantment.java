package net.ryan.beyond_the_block.content.enchantment.Tools.shovels;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EarthShatterEnchantment extends Enchantment {
    public EarthShatterEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ShovelItem || stack.isOf(Items.BOOK);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (!(user instanceof PlayerEntity)) return;
        World world = user.getWorld();
        if (world.isClient) return;

        BlockPos center = target.getBlockPos();
        // 1 = 3x3, 2 = 5x5, 3 = 7x7

        for (BlockPos pos : BlockPos.iterateOutwards(center.down(), 1, level, 1)) {
            BlockState state = world.getBlockState(pos);
            if (state.isIn(BlockTags.SHOVEL_MINEABLE) && state.getHardness(world, pos) >= 0) {
                world.breakBlock(pos, true); // Drop item
            }
        }
        if(target instanceof LivingEntity livingEntity) {
            Vec3d centerPos = Vec3d.ofCenter(center);
            livingEntity.setPosition(centerPos.x, livingEntity.getY(), centerPos.z);
            livingEntity.setVelocity(Vec3d.ZERO);
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 30, 1, false, false, false)); // duration, amplifier
        }

        for (Entity nearby : world.getOtherEntities(null, target.getBoundingBox().expand(level))) {
            if (nearby instanceof LivingEntity le && nearby != user && nearby != target) {
                Vec3d centerPos = Vec3d.ofCenter(center);

                le.setPosition(centerPos.x, nearby.getY(), centerPos.z);

                le.setVelocity(Vec3d.ZERO);
                le.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 30, 1, false, false, false)); // duration, amplifier

            }
        }

        // Play effect
        world.playSound(null, center, net.minecraft.sound.SoundEvents.ENTITY_GENERIC_EXPLODE,
                net.minecraft.sound.SoundCategory.PLAYERS, 1.0f, 1.2f);
        ((ServerWorld) world).spawnParticles(
                net.minecraft.particle.ParticleTypes.POOF,
                center.getX() + 0.5, center.getY(), center.getZ() + 0.5,
                20, level, 0.1, level, 0.01);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
