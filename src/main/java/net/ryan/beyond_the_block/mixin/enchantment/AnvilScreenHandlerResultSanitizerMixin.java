package net.ryan.beyond_the_block.mixin.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerResultSanitizerMixin {

    @Shadow @Final private Property levelCost;

    @Inject(method = "updateResult", at = @At("TAIL"))
    private void beyond_the_block$sanitizeInvalidResults(CallbackInfo ci) {
        AnvilScreenHandler self = (AnvilScreenHandler) (Object) this;
        Slot outputSlot = self.getSlot(ForgingScreenHandler.OUTPUT_SLOT_INDEX);
        ItemStack result = outputSlot.getStack();

        if (result.isEmpty()) {
            return;
        }

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(result);
        if (enchantments.isEmpty()) {
            return;
        }

        boolean changed = false;

        Iterator<Map.Entry<Enchantment, Integer>> iterator = enchantments.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Enchantment, Integer> entry = iterator.next();
            if (!entry.getKey().isAcceptableItem(result)) {
                iterator.remove();
                changed = true;
            }
        }

        if (!changed) {
            return;
        }

        if (enchantments.isEmpty()) {
            outputSlot.setStack(ItemStack.EMPTY);
            this.levelCost.set(0);
        } else {
            EnchantmentHelper.set(enchantments, result);
            outputSlot.setStack(result);
        }
    }
}