package net.ryan.beyond_the_block.feature.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

public class ArrowRecoveryHandler {
    public static void dropArrows(ServerWorld world, Entity entity, LivingEntity killed) {
        if(!(killed instanceof ArrowHitsAccess acc)) return;
        if (acc.beyondTheBlock$getArrowHits().isEmpty()) return;

        for (NbtCompound data : acc.beyondTheBlock$getArrowHits()) {
            ItemStack drop = switch (data.getString("type")) {
                case "spectral" -> new ItemStack(Items.SPECTRAL_ARROW);
                case "tipped" -> new ItemStack(Items.TIPPED_ARROW);
                default -> new ItemStack(Items.ARROW);
            };
            if (!drop.isEmpty()) world.spawnEntity(new ItemEntity(world, killed.getX(), killed.getY(), killed.getZ(), drop));
        }
        acc.beyondTheBlock$getArrowHits().clear();
    }
}
