package net.ryan.beyond_the_block.mixin.feature.enchantments;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.ryan.beyond_the_block.utils.ProjectileHelpers.ArrowHitsAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityArrowMixin implements ArrowHitsAccess {
    @Unique
    private final List<NbtCompound> arrowdrops$arrowHits = new ArrayList<>();

    @Override
    public List<NbtCompound> beyondTheBlock$getArrowHits() {
        return arrowdrops$arrowHits;
    }

    // Save
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void arrowdrops$writeArrowData(NbtCompound nbt, CallbackInfo ci) {
        NbtList list = new NbtList();
        for (NbtCompound data : arrowdrops$arrowHits) {
            list.add(data.copy());
        }
        nbt.put("ArrowDropsData", list);
    }

    // Load
    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void arrowdrops$readArrowData(NbtCompound nbt, CallbackInfo ci) {
        arrowdrops$arrowHits.clear();
        if (nbt.contains("ArrowDropsData", NbtElement.LIST_TYPE)) {
            NbtList list = nbt.getList("ArrowDropsData", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < list.size(); i++) {
                arrowdrops$arrowHits.add(list.getCompound(i));
            }
        }
    }
}
