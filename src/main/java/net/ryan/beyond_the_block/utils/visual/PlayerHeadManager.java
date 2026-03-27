package net.ryan.beyond_the_block.utils.visual;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class PlayerHeadManager {
    private static final Set<UUID> loadedUUIDs = ConcurrentHashMap.newKeySet();
    private static final Map<UUID, ItemStack> headCache = new ConcurrentHashMap<>();
    public static final ItemStack FALLBACK_HEAD;

    static {
        FALLBACK_HEAD = new ItemStack(Items.PLAYER_HEAD);
        NbtCompound fallbackNbt = FALLBACK_HEAD.getOrCreateNbt();
        NbtCompound skullOwner = new NbtCompound();
        skullOwner.putString("Name", "Steve");
        fallbackNbt.put("SkullOwner", skullOwner);
    }

    public static void tick() {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return;

        for (AbstractClientPlayerEntity player : world.getPlayers()) {
            UUID uuid = player.getUuid();
            String name = player.getName().getString();
            if (!loadedUUIDs.contains(uuid)) {
                preloadHead(uuid, name);
            }
        }
    }

    public static void preloadHead(UUID uuid, String name) {
        loadedUUIDs.add(uuid);
        GameProfile profile = new GameProfile(uuid, name);
        SkullBlockEntity.loadProperties(profile, loaded -> {
            try {
                ItemStack head = new ItemStack(Items.PLAYER_HEAD);
                NbtCompound skullOwner = new NbtCompound();
                NbtHelper.writeGameProfile(skullOwner, loaded);
                head.getOrCreateNbt().put("SkullOwner", skullOwner);
                headCache.put(uuid, head);
            } catch (Exception e) {
                headCache.put(uuid, FALLBACK_HEAD.copy());
            }
        });
    }

    public static ItemStack getHead(UUID uuid) {
        return headCache.getOrDefault(uuid, FALLBACK_HEAD.copy());
    }

    public static void getHeadAsync(UUID uuid, String name, Consumer<ItemStack> callback) {
        ItemStack cached = headCache.get(uuid);
        if (cached != null) {
            callback.accept(cached);
            return;
        }
        preloadHead(uuid, name);
        callback.accept(FALLBACK_HEAD.copy());
    }
}

