package net.ryan.beyond_the_block.mixin.Entities;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin extends AnimalEntity {

    protected ChickenEntityMixin(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void beyond$dropFeathers(CallbackInfo ci) {
        if (this.world.isClient) return;

        var cfg = AutoConfig.getConfigHolder(ModConfig.class).getConfig().passiveDropsConfig;

        if (!cfg.enableChickenFeathers) return;

        // Every N ticks, do final chance check
        if (this.age % cfg.chickenFeatherInterval == 0) {
            if (this.random.nextFloat() < cfg.chickenFeatherChance) {
                this.dropStack(new ItemStack(Items.FEATHER));
            }
        }
    }
}

