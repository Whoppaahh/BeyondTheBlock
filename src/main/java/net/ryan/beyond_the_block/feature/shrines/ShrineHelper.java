package net.ryan.beyond_the_block.feature.shrines;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.block.Shrine.ShrineCoreBlock;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ShrineHelper {


    public static Optional<BlockPos> findShrineCore(World world, BlockPos startPos) {
        // You could define a standard pattern here to locate the core
        for (BlockPos pos : BlockPos.iterate(startPos.add(-2, -2, -2), startPos.add(2, 2, 2))) {
            if (world.getBlockState(pos).getBlock() instanceof ShrineCoreBlock) {
                return Optional.of(pos.toImmutable());
            }
        }
        return Optional.empty();
    }

    public static void applyRandomHarmfulEffect(PlayerEntity player) {
        List<StatusEffect> harmfulEffects = Registry.STATUS_EFFECT.stream()
                .filter(effect -> effect.getCategory() == StatusEffectCategory.HARMFUL)
                .toList();

        if (!harmfulEffects.isEmpty()) {
            Random random = new Random();
            StatusEffect chosen = harmfulEffects.get(random.nextInt(harmfulEffects.size()));

            int duration = 100; // 5 seconds
            int amplifier = 0; // Level 1

            player.addStatusEffect(new StatusEffectInstance(chosen, duration, amplifier));
        }
    }
    public static ItemStack createRewardShulkerBox() {
        // Create a Shulker Box item stack
        DyeColor[] colors = DyeColor.values();
        DyeColor randomColor = colors[new Random().nextInt(colors.length)];
        Item shulkerBoxItem = Registry.ITEM.get(Registry.BLOCK.getId(ShulkerBoxBlock.get(randomColor)));
        ItemStack shulkerBox = new ItemStack(shulkerBoxItem);

        // Define the loot table
        Identifier lootTableID;

        if (Configs.server().features.shrines.rewardsIncludeVanillaItems && Configs.server().features.shrines.rewardsIncludeModdedItems) {
            // Create a combined loot table ID
            lootTableID = new Identifier(BeyondTheBlock.MOD_ID, "shrine/shulker_rewards_combined");
        } else if (Configs.server().features.shrines.rewardsIncludeModdedItems) {
            lootTableID = new Identifier(BeyondTheBlock.MOD_ID, "shrine/shulker_rewards_modded");
        } else {
            lootTableID = new Identifier(BeyondTheBlock.MOD_ID, "shrine/shulker_rewards_vanilla");
        }


        BeyondTheBlock.LOGGER.info("Loot Table: " + lootTableID);
        // Create a BlockEntityTag to hold the loot table info
        NbtCompound blockEntityTag = new NbtCompound();
        blockEntityTag.putString("LootTable", lootTableID.toString());
        blockEntityTag.putLong("LootTableSeed", new Random().nextLong()); // Random seed to vary loot

        // Attach the loot table to the shulker box's block entity tag
        shulkerBox.getOrCreateNbt().put("BlockEntityTag", blockEntityTag);

        // Optionally set a custom name
        shulkerBox.setCustomName(Text.literal("Shrine's Blessing"));

        return shulkerBox;
    }
    public static ItemStack getRandomEnchantedModItem() {
        List<Item> potentialItems = new ArrayList<>();

        // Load all your tagged items (from mod tags)
        Registry.ITEM.getEntryList(new TagKey<>(Registry.ITEM_KEY, new Identifier("emerald-empire", "mod_weapons")))
                .ifPresent(entries -> entries.forEach(entry -> potentialItems.add(entry.value())));
        Registry.ITEM.getEntryList(new TagKey<>(Registry.ITEM_KEY, new Identifier("emerald-empire", "mod_tools")))
                .ifPresent(entries -> entries.forEach(entry -> potentialItems.add(entry.value())));
        Registry.ITEM.getEntryList(new TagKey<>(Registry.ITEM_KEY, new Identifier("emerald-empire", "mod_armour")))
                .ifPresent(entries -> entries.forEach(entry -> potentialItems.add(entry.value())));

        if (potentialItems.isEmpty()) return ItemStack.EMPTY;

        Item randomItem = potentialItems.get(new Random().nextInt(potentialItems.size()));
        ItemStack stack = new ItemStack(randomItem);

        // Choose enchantments based on item type
        if (stack.getItem() instanceof SwordItem) {
            applyRandomEnchantment(stack, List.of(
                    "resilient_strike", "phantom_slash", "life_steal", "temporal_slice"
            ));
        } else if (stack.getItem() instanceof PickaxeItem) {
            applyRandomEnchantment(stack, List.of(
                    "shadow_mining", "stone_breaker", "temporal_efficiency"
            ));
        } else if (stack.getItem() instanceof AxeItem) {
            applyRandomEnchantment(stack, List.of(
                    "nightfall_cleave", "timber_cut", "temporal_efficiency"
            ));
        } else if (stack.getItem() instanceof ShovelItem) {
            applyRandomEnchantment(stack, List.of(
                    "dark_dig", "earth_shatter", "fertility", "temporal_efficiency"
            ));
        } else if (stack.getItem() instanceof HoeItem) {
            applyRandomEnchantment(stack, List.of(
                    "deep_till", "gardens_bounty", "night_cultivation", "temporal_efficiency"
            ));
        } else if (stack.getItem() instanceof ArmorItem armor) {
            switch (armor.getSlotType()) {
                case HEAD -> applyRandomEnchantment(stack, List.of("radiant_aura", "shadows_veil", "ironclad", "mind_ward", "warding_glyph"));
                case CHEST -> applyRandomEnchantment(stack, List.of("durability_boost", "echo_guard", "warding_glyph"));
                case LEGS -> applyRandomEnchantment(stack, List.of("frozen_momentum", "graceful_movement", "nightstride", "warding_glyph"));
                case FEET -> applyRandomEnchantment(stack, List.of("grounded_resistance", "silent_steps", "floral_leap", "warding_glyph"));
            }
        }

        return stack;
    }

    private static void applyRandomEnchantment(ItemStack stack, List<String> enchantmentIds) {
        if (enchantmentIds.isEmpty()) return;

        String chosenId = enchantmentIds.get(new Random().nextInt(enchantmentIds.size()));
        Identifier enchantId = new Identifier(BeyondTheBlock.MOD_ID, chosenId);
        Enchantment enchantment = Registry.ENCHANTMENT.get(enchantId);

        if (enchantment != null) {
            stack.addEnchantment(enchantment, 1 + new Random().nextInt(enchantment.getMaxLevel()));
        }
    }

}

