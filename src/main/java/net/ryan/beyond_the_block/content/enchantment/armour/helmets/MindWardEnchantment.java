package net.ryan.beyond_the_block.content.enchantment.armour.helmets;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.content.enchantment.MyEnchantmentHelper;
import net.ryan.beyond_the_block.utils.helpers.GlowPacketHelper;

import java.util.*;

public class MindWardEnchantment extends Enchantment {

    private static final Map<UUID, Set<UUID>> playerToGlowing = new HashMap<>();

    public MindWardEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armour && armour.getSlotType() == EquipmentSlot.HEAD || stack.isOf(Items.BOOK);
    }

    public static void registerTickHandler(ServerWorld world) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            if (!MyEnchantmentHelper.hasArmorEnchantment(player, ModEnchantments.MIND_WARD)) {
                // Remove all glows if previously tracked
                Set<UUID> previouslyGlowing = playerToGlowing.getOrDefault(player.getUuid(), Set.of());
                for (UUID entityId : previouslyGlowing) {
                    Entity entity = world.getEntity(entityId);
                    if (entity != null) {
                        GlowPacketHelper.setGlowing(player, entity, false);
                    }
                }
                playerToGlowing.remove(player.getUuid());
                continue;
            }

            Set<UUID> currentlyGlowing = new HashSet<>();
            List<Entity> invisibles = world.getOtherEntities(player, player.getBoundingBox().expand(10), Entity::isInvisible);

            for (Entity entity : invisibles) {
                GlowPacketHelper.setGlowing(player, entity, true);
                currentlyGlowing.add(entity.getUuid());
            }

            // Remove glow from entities no longer qualifying
            Set<UUID> previous = playerToGlowing.getOrDefault(player.getUuid(), Set.of());
            for (UUID oldUuid : previous) {
                if (!currentlyGlowing.contains(oldUuid)) {
                    Entity entity = world.getEntity(oldUuid);
                    if (entity != null) {
                        GlowPacketHelper.setGlowing(player, entity, false);
                    }
                }
            }

            playerToGlowing.put(player.getUuid(), currentlyGlowing);
        }
    }
}