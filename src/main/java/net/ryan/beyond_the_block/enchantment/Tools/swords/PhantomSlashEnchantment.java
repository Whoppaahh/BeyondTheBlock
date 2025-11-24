package net.ryan.beyond_the_block.enchantment.Tools.swords;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PhantomSlashEnchantment extends Enchantment {
    public PhantomSlashEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || stack.isOf(Items.BOOK);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user instanceof PlayerEntity player && target instanceof LivingEntity livingTarget) {
            World world = player.world;

            // Phantom Slash bypasses armor by dealing a percentage of damage directly to health
            float damage = ((SwordItem) player.getMainHandStack().getItem()).getAttackDamage() + level * 2.0F;  // Base damage + bonus per level
            float phantomDamage = damage * 0.25F; // 25% of damage bypasses armor (adjustable)

            // Apply the "phantom" damage directly to health, bypassing the target's armor
            livingTarget.damage(DamageSource.GENERIC, phantomDamage);

            // Calculate the teleportation distance based on the level of the enchantment
            BlockPos targetPos = getBlockPos(level, player);

            // Check if the target position is valid (not inside blocks, for example)
            if (world.isAir(targetPos) && world.getBlockState(targetPos.down()).isFullCube(world, targetPos)) {
                // Teleport the player to the new position
                player.teleport(targetPos.getX(), targetPos.getY(), targetPos.getZ());

                // Optional: Add a visual effect or sound to indicate teleportation (for feedback)
                 player.world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
        }
    }

    private static @NotNull BlockPos getBlockPos(int level, PlayerEntity player) {
        double teleportDistance = 2.0 * level;  // 2 blocks per level

        // Get the direction the player is facing
        Vec3d playerLookVec = player.getRotationVector();

        // Calculate the target position for teleportation
        // Adjusted Y to prevent teleportation to weird heights
        return new BlockPos(player.getX() + playerLookVec.x * teleportDistance,
                player.getY() + 0.5, // Adjusted Y to prevent teleportation to weird heights
                player.getZ() + playerLookVec.z * teleportDistance);
    }

    @Override
    public int getMaxLevel() {
        return 3; // Max level of Phantom Slash
    }

    @Override
    public int getMinLevel() {
        return 1; // Min level of Phantom Slash
    }
}