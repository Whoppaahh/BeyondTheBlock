package net.ryan.beyond_the_block.utils;

import com.google.gson.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class FilteredEnchantFunction extends ConditionalLootFunction {
    public static final LootFunctionType TYPE = new LootFunctionType(new FilteredEnchantFunction.Serializer());
    private final List<String> allowedNamespaces;

    protected FilteredEnchantFunction(LootCondition[] conditions, List<String> allowedNamespaces) {
        super(conditions);
        this.allowedNamespaces = allowedNamespaces;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        if (!stack.isEnchantable()) return stack;

        List<Enchantment> allowed = Registry.ENCHANTMENT.stream()
                .filter(enchantment -> enchantment.isAcceptableItem(stack))
                .filter(enchantment -> {
                    if(allowedNamespaces.isEmpty()) return true; // Allow all if not specified
                    Identifier id = Registry.ENCHANTMENT.getId(enchantment);
                    return id != null && allowedNamespaces.contains(id.getNamespace());
                })
                .toList();

        if (!allowed.isEmpty()) {
            Enchantment chosen = Util.getRandom(allowed, context.getRandom());
            int level = 1 + context.getRandom().nextInt(chosen.getMaxLevel());
            stack.addEnchantment(chosen, level);
        }

        return stack;
    }

    @Override
    public LootFunctionType getType() {
        return TYPE;
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<FilteredEnchantFunction> {
        @Override
        public void toJson(JsonObject json, FilteredEnchantFunction function, JsonSerializationContext context) {
            super.toJson(json, function, context);
            JsonArray array = new JsonArray();
            for (String ns : function.allowedNamespaces) {
                array.add(ns);
            }
            json.add("namespaces", array);
        }

        @Override
        public FilteredEnchantFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
            List<String> namespaces = new ArrayList<>();
            if (json.has("namespaces")) {
                for (JsonElement el : json.getAsJsonArray("namespaces")) {
                    namespaces.add(el.getAsString());
                }
            }
            return new FilteredEnchantFunction(conditions, namespaces);
        }
    }
}

