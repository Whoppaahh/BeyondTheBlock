package net.ryan.beyond_the_block.content.riddles;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Riddle {
    private final UUID id;
    private final List<String> pages;
    private final List<Item> requiredItems;

    public Riddle(UUID id, List<String> pages, List<Item> requiredItems) {
        this.id = id;
        this.pages = pages;
        this.requiredItems = requiredItems;
    }

    public UUID getId() {
        return id;
    }

    public List<String> getPages() {
        return pages;
    }

    public List<Item> getRequiredItems() {
        return requiredItems;
    }

    public NbtCompound toNbt(UUID playerId) {
        NbtCompound tag = new NbtCompound();
        tag.putUuid("Player", playerId);
        tag.putUuid("Id", id);

        NbtList pagesList = new NbtList();
        for (String page : pages) {
            pagesList.add(NbtString.of(page));
        }
        tag.put("Pages", pagesList);

        NbtList itemsList = new NbtList();
        for (Item item : requiredItems) {
            itemsList.add(NbtString.of(Registry.ITEM.getId(item).toString()));
        }
        tag.put("Items", itemsList);

        return tag;
    }

    public static Riddle fromNbt(NbtCompound tag) {
        UUID riddleId = tag.getUuid("Id");

        List<String> pages = new ArrayList<>();
        NbtList pagesList = tag.getList("Pages", NbtList.STRING_TYPE);
        for (int i = 0; i < pagesList.size(); i++) {
            pages.add(pagesList.getString(i));
        }

        List<String> itemIds = new ArrayList<>();
        NbtList itemsList = tag.getList("Items", NbtList.STRING_TYPE);
        for (int i = 0; i < itemsList.size(); i++) {
            itemIds.add(itemsList.getString(i));
        }

        return fromStored(riddleId, pages, itemIds);
    }

    public static UUID getPlayerId(NbtCompound tag) {
        return tag.getUuid("Player");
    }

    // Static method to create a Riddle from stored data (NBT)
    public static Riddle fromStored(UUID riddleId, List<String> pages, List<String> itemIds) {
        List<Item> items = new ArrayList<>();
        for (String itemId : itemIds) {
            Identifier identifier = new Identifier(itemId);
            if (!Registry.ITEM.getIds().contains(identifier)) {
                BeyondTheBlock.LOGGER.warn("Item {} not found in Registry.ITEM", identifier);
                continue;
            }
            Item item = Registry.ITEM.get(identifier);
            if (item == Items.AIR) {
                BeyondTheBlock.LOGGER.warn("Item {} resolved to AIR, skipping", identifier);
                continue;
            }

            items.add(item);
        }
        return new Riddle(riddleId, pages, items);
    }

}
