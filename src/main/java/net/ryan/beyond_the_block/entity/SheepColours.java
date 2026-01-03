package net.ryan.beyond_the_block.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.ryan.beyond_the_block.utils.Naming.EntityTagManager;

public class SheepColours {
    public static void randomiseColours(Entity entity, ServerWorld serverWorld) {
        if (entity instanceof SheepEntity sheep && Math.random() < 0.1f) {
            // Now sheep is automatically typed as SheepEntity
            sheep.setColor(DyeColor.values()[(int)(Math.random() * DyeColor.values().length)]);

            // Optional jeb_ rainbow
           // if (Math.random() < 0.1) {
                sheep.setCustomName(Text.literal("jeb_"));
                sheep.setCustomNameVisible(false);
            ((EntityTagManager) sheep).beyondTheBlock$setHideName(true);
          //  }
        }
    }
}
