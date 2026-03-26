package net.ryan.beyond_the_block.client.tooltip;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientTooltipRegistrar {
    private static final Map<String, Text> ENCHANTMENT_DESC_CACHE = new HashMap<>();
    private static final Map<String, Formatting> GEM_COLORS = Map.ofEntries(
            Map.entry("miranite", Formatting.GREEN),
            Map.entry("chromite", Formatting.LIGHT_PURPLE),
            Map.entry("nocturnite", Formatting.DARK_PURPLE),
            Map.entry("amberine", Formatting.GOLD),
            Map.entry("azuros", Formatting.AQUA),
            Map.entry("indigra", Formatting.BLUE),
            Map.entry("rosette", Formatting.RED)
    );

    public static void register() {
        ItemTooltipCallback.EVENT.register(ClientTooltipRegistrar::handleTooltip);
    }

    private static void handleTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("GemList", NbtElement.LIST_TYPE))
            appendGemInfo(nbt, tooltip);
        appendEnchantmentInfo(stack, tooltip);
    }

    private static void appendGemInfo(NbtCompound nbt, List<Text> tooltip) {
        NbtList gems = nbt.getList("GemList", NbtElement.STRING_TYPE);
        if (gems.isEmpty()) return;

        tooltip.add(Text.literal("Gems Applied:").formatted(Formatting.GRAY));
        for (NbtElement element : gems) {
            String gem = element.asString();
            tooltip.add(Text.literal(gem).formatted(getGemColor(gem)));
        }
    }

    private static void appendEnchantmentInfo(ItemStack stack, List<Text> tooltip) {
        if (!stack.hasEnchantments()) return;

        if (Screen.hasShiftDown()) {
            for (NbtElement element : stack.getEnchantments()) {
                if (!(element instanceof NbtCompound tag)) continue;
                String idStr = tag.getString("id");
                Text cached = ENCHANTMENT_DESC_CACHE.computeIfAbsent(idStr, ClientTooltipRegistrar::buildEnchantmentDescription);
                if (cached != null) tooltip.add(cached);
            }
        } else {
            tooltip.add(Text.literal("Hold SHIFT for more info").formatted(Formatting.DARK_GRAY));
        }
    }

    private static Text buildEnchantmentDescription(String idStr) {
        Identifier id = Identifier.tryParse(idStr);
        if (id == null) return null;
        String key = "enchantment." + id.getNamespace() + "." + id.getPath() + ".desc";
        return I18n.hasTranslation(key)
                ? Text.translatable(key).formatted(Formatting.GRAY)
                : null;
    }

    private static Formatting getGemColor(String gem) {
        return GEM_COLORS.getOrDefault(gem.toLowerCase(), Formatting.GRAY);
    }

}
