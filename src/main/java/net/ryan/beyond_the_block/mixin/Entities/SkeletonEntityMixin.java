package net.ryan.beyond_the_block.mixin.Entities;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class SkeletonEntityMixin extends HostileEntity {

    protected SkeletonEntityMixin(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void beyond$dropBones(CallbackInfo ci) {
        if (this.world.isClient) return;

        var cfg = AutoConfig.getConfigHolder(ModConfig.class).getConfig().passiveDropsConfig;
        if (!cfg.enableSkeletonBones) return;

        if (this.age % cfg.skeletonBoneInterval == 0) {
            if (this.random.nextFloat() < cfg.skeletonBoneChance) {
                this.dropStack(new ItemStack(Items.BONE));
            }
        }
    }
}
