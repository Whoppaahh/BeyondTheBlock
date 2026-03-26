package net.ryan.beyond_the_block.mixin.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {

    @Shadow @Nullable
    public abstract PlayerEntity getPlayerOwner();

    /**
     * Inject AFTER world.spawnEntity() is called inside FishingBobberEntity.use().
     * This catches freshly-spawned fishing loot (ItemEntity).
     */
    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
                    shift = At.Shift.AFTER
            )
    )
    private void btb$cookFishingLoot(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
        FishingBobberEntity self = (FishingBobberEntity)(Object)this;
        PlayerEntity owner = this.getPlayerOwner();

        if (!(owner instanceof ServerPlayerEntity serverPlayer)) return;

        ItemStack rod = owner.getMainHandStack();
        int level = EnchantmentHelper.getLevel(ModEnchantments.FISHING_COOKING, rod);

        if (level <= 0) return;

        World world = self.getWorld();

        // Find newly spawned loot: ItemEntities with age <= 1 tick
        world.getEntitiesByClass(ItemEntity.class,
                self.getBoundingBox().expand(1.5),
                e -> e.age <= 1
        ).forEach(itemEntity -> {
            ItemStack original = itemEntity.getStack();
            ItemStack cooked = cookFoodOnly(original, world);

            if (cooked != null) {
                itemEntity.setStack(cooked);
                spawnCookParticles((ServerWorld) world, itemEntity);
            }
        });
    }

    @Unique
    private void spawnCookParticles(ServerWorld world, ItemEntity itemEntity) {
        double x = itemEntity.getX();
        double y = itemEntity.getY() + 0.15;
        double z = itemEntity.getZ();

        // Steam (CLOUD)
        world.spawnParticles(
                ParticleTypes.CLOUD,
                x, y, z,
                8,             // count
                0.1, 0.1, 0.1, // spread
                0.02           // speed
        );

        // Light smoke
        world.spawnParticles(
                ParticleTypes.SMOKE,
                x, y, z,
                4,
                0.05, 0.05, 0.05,
                0.01
        );
    }

    // ------------------------------------------------------------
    //        Utility: Cook Only Edible Items via Furnace Recipes
    // ------------------------------------------------------------
    @Unique
    @Nullable
    private ItemStack cookFoodOnly(ItemStack stack, World world) {
        if (stack.isEmpty()) return null;

        Item raw = stack.getItem();

        // Only cook food items
        if (!raw.isFood()) return null;

        // Query smelting recipe
        Optional<SmeltingRecipe> match = world.getRecipeManager()
                .getFirstMatch(RecipeType.SMELTING, new SimpleInventory(stack), world);

        if (match.isEmpty()) return null;

        ItemStack result = match.get().getOutput();
        if (result.isEmpty()) return null;

        // Only accept if output is also food
        if (!result.getItem().isFood()) return null;

        return new ItemStack(result.getItem(), stack.getCount());
    }
}
