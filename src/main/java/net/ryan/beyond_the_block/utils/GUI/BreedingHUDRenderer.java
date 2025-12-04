package net.ryan.beyond_the_block.utils.GUI;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.utils.Helpers.BreedingSyncState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BreedingHUDRenderer implements HudRenderCallback {

    private static final double MAX_DISTANCE = 12.0;

    // Result for one frame
    private record BreedingInfo(
            String mainText,
            @Nullable String extra1,
            @Nullable String extra2,
            @Nullable String extra3,
            int color
    ) {}

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.world == null) return;

        // Only render when crosshair is on an entity
        HitResult hit = client.crosshairTarget;
        if (!(hit instanceof EntityHitResult ehr)) return;

        Entity target = ehr.getEntity();
        if (!(target instanceof AnimalEntity animal)) return;

        // Distance check
        Vec3d cam = client.player.getCameraPosVec(tickDelta);
        if (cam.distanceTo(animal.getPos()) > MAX_DISTANCE) return;

        // Build HUD data
        BreedingInfo info = getInfo(animal);

        TextRenderer tr = client.textRenderer;

        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();

        int baseY = sh / 2 + 22;
        int textX = sw / 2 - 38;


        // Main text
        tr.drawWithShadow(matrices, info.mainText(), textX, baseY, info.color());

        // Extra lines
        int y = baseY + 12;

        if (info.extra1() != null) {
            tr.drawWithShadow(matrices, info.extra1(), textX, y, 0xDDDDDD);
            y += 12;
        }
        if (info.extra2() != null) {
            tr.drawWithShadow(matrices, info.extra2(), textX, y, 0xDDDDDD);
            y += 12;
        }
        if (info.extra3() != null) {
            tr.drawWithShadow(matrices, info.extra3(), textX, y, 0xDDDDDD);
        }
    }


    private BreedingInfo getInfo(AnimalEntity a) {

        // The REAL age value synced from server (full 24000 / 6000 ticks)
        int age = BreedingSyncState.getAge(a.getId());

        boolean isBaby = a.isBaby(); // correct in all mappings

        int color;
        String main;
        String extra1 = null;
        String extra2 = null;
        String extra3 = null;

        // ---------- BABY ----------
        if (isBaby || age < 0) {
            int remaining = Math.max(-age, 0); // convert negative ticks to positive remaining

            color = 0xFFAA00;
            main = "Growing: " + formatTime(remaining);

            extra1 = favoriteFoodText(a);

            return new BreedingInfo(main, extra1, extra2, extra3, color);
        }

        // ---------- ADULT / COOLDOWN ----------
        if (age > 0) {
            color = 0xFF5555;
            main = "Cooldown: " + formatTime(age);

            extra1 = favoriteFoodText(a);
            extra3 = "Partner nearby: " + (hasPartnerNearby(a) ? "Yes" : "No");

            return new BreedingInfo(main, extra1, extra2, extra3, color);
        }

        // ---------- ADULT / READY ----------
        color = 0x00FF66;
        main = "Ready to breed";

        extra1 = favoriteFoodText(a);
        extra3 = "Partner nearby: " + (hasPartnerNearby(a) ? "Yes" : "No");

        return new BreedingInfo(main, extra1, extra2, extra3, color);
    }

    private boolean hasPartnerNearby(AnimalEntity self) {
        // Partner = same species AND adult AND not on cooldown (age == 0)
        List<AnimalEntity> list = self.world.getEntitiesByClass(
                AnimalEntity.class,
                new Box(self.getBlockPos()).expand(8),
                a -> a != self
                        && a.getClass() == self.getClass()
                        && !a.isBaby()
                        && BreedingSyncState.getAge(a.getId()) == 0
        );
        return !list.isEmpty();
    }

    private String favoriteFoodText(AnimalEntity a) {
        Item fav = findFavoriteFood(a);
        return fav != null ? "Favorite: " + fav.getName().getString() : null;
    }

    private @Nullable Item findFavoriteFood(AnimalEntity a) {
        // Common vanilla breeding foods
        Item[] food = {
                Items.WHEAT, Items.CARROT, Items.POTATO, Items.BEETROOT,
                Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS,
                Items.MELON_SEEDS, Items.PUMPKIN_SEEDS,
                Items.PORKCHOP, Items.BEEF, Items.CHICKEN, Items.MUTTON,
                Items.COD, Items.SALMON,
                Items.APPLE, Items.SWEET_BERRIES
        };
        for (Item i : food) {
            if (a.isBreedingItem(new ItemStack(i))) {
                return i;
            }
        }
        return null;
    }

    private String formatTime(int ticks) {
        if (ticks <= 0) return "0s";
        int sec = ticks / 20;
        if (sec < 60) return sec + "s";
        int min = sec / 60;
        int rem = sec % 60;
        return min + "m " + rem + "s";
    }
}
