package net.ryan.beyond_the_block.utils.Helpers;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class PathSpeedHelper {
        private static final UUID PATH_SPEED_UUID =
                UUID.fromString("d5c7c317-bb68-4f02-a1a2-bb91cc2e2c14");

        // +15% movement speed
        private static final EntityAttributeModifier PATH_SPEED_MODIFIER =
                new EntityAttributeModifier(PATH_SPEED_UUID, "Path speed boost", 0.4, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);


    public static void tickSpeed(MinecraftServer server) {
        for(PlayerEntity player : server.getWorld(World.OVERWORLD).getPlayers()) {
            try {
                var speedAttr = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if (speedAttr == null) continue;

                BlockPos pos = new BlockPos(player.getX(), player.getY() - 0.5, player.getZ());
                BlockState under = player.getWorld().getBlockState(pos);
                if (under.isOf(Blocks.DIRT_PATH)) {
                    if (speedAttr.getModifier(PATH_SPEED_UUID) == null) {
                        speedAttr.addPersistentModifier(PATH_SPEED_MODIFIER);
                    }
                } else {
                    if (speedAttr.getModifier(PATH_SPEED_UUID) != null) {
                        speedAttr.removeModifier(PATH_SPEED_UUID);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
