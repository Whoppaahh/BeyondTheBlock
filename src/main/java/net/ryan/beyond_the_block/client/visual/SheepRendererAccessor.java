package net.ryan.beyond_the_block.client.visual;

import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.entity.passive.SheepEntity;

public interface SheepRendererAccessor {
    SheepEntityModel<SheepEntity> beyondTheBlock$getSheepModel();
}