package net.ryan.beyond_the_block.content.enchantment.armour.boots;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class LeapOfFaithEnchantment extends Enchantment {
    public LeapOfFaithEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armour && armour.getSlotType() == EquipmentSlot.FEET || stack.isOf(Items.BOOK);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }


    public static void tryPerformDoubleJump(ServerPlayerEntity player) {
        ItemStack boots = player.getEquippedStack(EquipmentSlot.FEET);
        int level = EnchantmentHelper.getLevel(ModEnchantments.LEAP_OF_FAITH, boots);
        if (level <= 0) return;
        LeapOfFaithTracker tracker = (LeapOfFaithTracker) player;
        int current = tracker.getAirJumpCount();
        int max = 1 + level;

        if (player.isOnGround() || player.getAbilities().flying) return;
        BeyondTheBlock.LOGGER.info("Jump :{}", current);
        if (current >= max || current < 1) return;
        BeyondTheBlock.LOGGER.info("Performing double jump :{}", current);
        ((ServerWorld) player.world).spawnParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(), 10, 0.2, 0.1, 0.2, 0.01);
        Vec3d velocity = player.getVelocity();
        double jumpStrength = 0.42D + (0.05D * (current - 1));
        player.setVelocity(velocity.x, jumpStrength, velocity.z);

        player.fallDistance = 0.0F;
        player.velocityDirty = true;
        player.velocityModified = true;
        ((ServerWorld) player.world).getChunkManager().sendToNearbyPlayers(player, new EntityVelocityUpdateS2CPacket(player));


        tracker.setAirJumpCount(current + 1);
    }


}
