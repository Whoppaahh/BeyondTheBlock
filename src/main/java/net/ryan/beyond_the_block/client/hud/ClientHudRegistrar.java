package net.ryan.beyond_the_block.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.feature.paths.PathPreviewState;
import net.ryan.beyond_the_block.feature.paths.PathToolHelper;

import java.util.List;

public class ClientHudRegistrar {
    public static void register(){
        FloatingXPManager.register();
    }
}
