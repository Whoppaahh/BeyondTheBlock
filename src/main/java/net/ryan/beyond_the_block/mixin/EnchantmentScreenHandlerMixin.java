package net.ryan.beyond_the_block.mixin;

import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.utils.helpers.EnchantingPowerHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin {

    @Shadow @Final private Inventory inventory;
    @Shadow @Final private ScreenHandlerContext context;
    @Shadow @Final private Random random;
    @Shadow @Final private Property seed;
    @Shadow @Final public int[] enchantmentPower;
    @Shadow @Final public int[] enchantmentId;
    @Shadow @Final public int[] enchantmentLevel;

    @Shadow
    private List<EnchantmentLevelEntry> generateEnchantments(ItemStack stack, int slot, int level) {
        throw new AssertionError();
    }

    /**
     * @author Ryan
     * @reason Custom enchanting power support for chiseled bookshelves.
     */
    @Overwrite
    public void onContentChanged(Inventory inventory) {
        if (inventory == this.inventory) {
            ItemStack itemStack = inventory.getStack(0);
            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
                this.context.run((world, pos) -> {
                    int power = 0;
                    Iterator<?> iterator = EnchantingTableBlock.BOOKSHELF_OFFSETS.iterator();

                    while (iterator.hasNext()) {
                        BlockPos offset = (BlockPos) iterator.next();
                        if (EnchantingPowerHelper.canAccessCustomBookshelf(world, pos, offset)) {
                            BlockPos providerPos = pos.add(offset);
                            power += EnchantingPowerHelper.getBookshelfPower(world, providerPos);
                        }
                    }

                    // Clamp to vanilla cap so enchanting balance stays sane.
                    if (power > 15) {
                        power = 15;
                    }

                    this.random.setSeed((long) this.seed.get());

                    for (int j = 0; j < 3; ++j) {
                        this.enchantmentPower[j] =
                                EnchantmentHelper.calculateRequiredExperienceLevel(this.random, j, power, itemStack);
                        this.enchantmentId[j] = -1;
                        this.enchantmentLevel[j] = -1;

                        if (this.enchantmentPower[j] < j + 1) {
                            this.enchantmentPower[j] = 0;
                        }
                    }

                    for (int j = 0; j < 3; ++j) {
                        if (this.enchantmentPower[j] > 0) {
                            List<EnchantmentLevelEntry> list =
                                    this.generateEnchantments(itemStack, j, this.enchantmentPower[j]);

                            if (list != null && !list.isEmpty()) {
                                EnchantmentLevelEntry entry =
                                        list.get(this.random.nextInt(list.size()));
                                this.enchantmentId[j] =
                                        Registry.ENCHANTMENT.getRawId(entry.enchantment);
                                this.enchantmentLevel[j] = entry.level;
                            }
                        }
                    }

                    ((net.minecraft.screen.ScreenHandler)(Object)this).sendContentUpdates();
                });
            } else {
                for (int i = 0; i < 3; ++i) {
                    this.enchantmentPower[i] = 0;
                    this.enchantmentId[i] = -1;
                    this.enchantmentLevel[i] = -1;
                }
            }
        }
    }
}