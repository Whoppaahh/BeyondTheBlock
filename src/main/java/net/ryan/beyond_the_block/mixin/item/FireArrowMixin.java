package net.ryan.beyond_the_block.mixin.item;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class FireArrowMixin {

    protected FireArrowMixin() {}

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "onEntityHit", at = @At("TAIL"))
    private void igniteEntity(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (!((Object)this instanceof ArrowEntity arrow) || !arrow.isOnFire()) return;

        if (entityHitResult.getEntity() instanceof LivingEntity living) {
            living.setOnFireFor(5);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "onBlockHit", at = @At("TAIL"))
    private void igniteAndShatter(BlockHitResult blockHitResult, CallbackInfo ci) {
        if (!((Object)this instanceof ArrowEntity arrow)) return;

        World world = arrow.getWorld();
        if (world.isClient) return;

        BlockPos hitPos = blockHitResult.getBlockPos();
        BlockPos firePos = hitPos.offset(blockHitResult.getSide());
        BlockState state = world.getBlockState(hitPos);

        // Fire placement for burning arrows
        if (arrow.isOnFire() && world.getBlockState(firePos).isAir() &&
                AbstractFireBlock.canPlaceAt(world, firePos, blockHitResult.getSide())) {
            world.setBlockState(firePos, AbstractFireBlock.getState(world, firePos), 11);
        }

        // Glass breaking for Piercing arrows
        if (arrow.getOwner() instanceof LivingEntity owner) {
            ItemStack weapon = owner.getMainHandStack();
            boolean isRanged = weapon.getItem() instanceof BowItem || weapon.getItem() instanceof CrossbowItem;

            if (isRanged && EnchantmentHelper.getLevel(Enchantments.PIERCING, weapon) > 0 &&
                    (state.isIn(ConventionalBlockTags.GLASS_BLOCKS) || state.isIn(ConventionalBlockTags.GLASS_PANES))) {

                // Sound & particles
                world.playSound(null, hitPos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(
                            new BlockStateParticleEffect(ParticleTypes.BLOCK, state),
                            hitPos.getX() + 0.5, hitPos.getY() + 0.5, hitPos.getZ() + 0.5,
                            20, 0.25, 0.25, 0.25, 0.1
                    );
                }

                // Break glass without drops
                world.breakBlock(hitPos, false, owner);

                // Drop arrow
                world.spawnEntity(new ItemEntity(
                        world,
                        hitPos.getX() + 0.5,
                        hitPos.getY() + 0.5,
                        hitPos.getZ() + 0.5,
                        new ItemStack(Items.ARROW)
                ));

                // Remove arrow
                arrow.discard();
            }
        }
    }
}
