package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.Configs;
import net.ryan.beyond_the_block.entity.SpiderCobwebTrailGoal;
import net.ryan.beyond_the_block.entity.SpiderWebAttackGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpiderEntity.class)
public abstract class SpiderEntityMixin extends HostileEntity {

    protected SpiderEntityMixin(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addWebGoal(EntityType<? extends SpiderEntity> entityType, World world, CallbackInfo ci) {
        this.goalSelector.add(3, new SpiderWebAttackGoal((SpiderEntity) (Object) this));
        this.goalSelector.add(4, new SpiderCobwebTrailGoal((SpiderEntity) (Object) this));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void beyond$dropSpiderMaterials(CallbackInfo ci) {
        if (this.world.isClient) return;

        if (!Configs.server().features.drops.enableSpiderDrops) return;

        if (this.age % Configs.server().features.drops.spiderDropInterval == 0) {
            if (this.random.nextFloat() < Configs.server().features.drops.spiderDropChance) {

                if (this.random.nextFloat() < Configs.server().features.drops.cobwebWeight) {
                    this.dropStack(new ItemStack(Items.COBWEB));
                } else {
                    this.dropStack(new ItemStack(Items.STRING));
                }
            }
        }
    }

}
