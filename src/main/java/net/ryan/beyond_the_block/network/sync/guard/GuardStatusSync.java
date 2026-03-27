package net.ryan.beyond_the_block.network.sync.guard;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import net.ryan.beyond_the_block.content.entity.villager.guard.GuardEntity;

import static net.ryan.beyond_the_block.network.packets.PacketIDs.GUARD_STATS_SYNC_PACKET_ID;

public class GuardStatusSync {
    public static void syncGuardStatus(ServerPlayerEntity player, GuardEntity guard) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(guard.getId());
        buf.writeBoolean(guard.hasStatusEffect(StatusEffects.POISON));
        buf.writeBoolean(guard.hasStatusEffect(StatusEffects.WITHER));
        buf.writeBoolean(guard.hasStatusEffect(ModEffects.FREEZE) || guard.isFrozen());

        //EmeraldEmpire.LOGGER.info("Syncing Guard Stats");
        ServerPlayNetworking.send(player, GUARD_STATS_SYNC_PACKET_ID, buf);
    }
}
