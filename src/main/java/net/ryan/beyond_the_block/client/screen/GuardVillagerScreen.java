package net.ryan.beyond_the_block.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.entity.villager.guard.GuardEntity;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.screen.handler.Guard.GuardVillagerScreenHandler;
import net.ryan.beyond_the_block.utils.visual.GuardGUIButton;

import static net.ryan.beyond_the_block.network.packets.PacketIDs.GUARD_FOLLOW_PACKET_ID;
import static net.ryan.beyond_the_block.network.packets.PacketIDs.GUARD_PATROL_PACKET_ID;

public class GuardVillagerScreen extends HandledScreen<GuardVillagerScreenHandler> {

    private static final Identifier GUARD_GUI_TEXTURES = new Identifier("minecraft", "textures/gui/container/inventory.png");
    private static final Identifier GUARD_FOLLOWING_ICON = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/following_icons.png");
    private static final Identifier GUARD_NOT_FOLLOWING_ICON = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/not_following_icons.png");
    private static final Identifier PATROL_ICON = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/patrollingui.png");
    private static final Identifier NOT_PATROLLING_ICON = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/notpatrollingui.png");

    private final PlayerEntity player;
    private final GuardEntity guardEntity;
    private float mousePosX;
    private float mousePosY;
    private boolean isFollowing;
    private boolean isPatrolling;

    public GuardVillagerScreen(GuardVillagerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, handler.guardEntity.getDisplayName());
        this.titleX = 110;
        this.playerInventoryTitleX = 118;
        this.player = inventory.player;
        guardEntity = handler.guardEntity;
    }

    @Override
    protected void init() {
        super.init();
        isFollowing = guardEntity.isFollowing();
        isPatrolling = guardEntity.isPatrolling();

        if (!Configs.server().features.guards.followHero || player.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE)) {
            this.addDrawableChild(new GuardGUIButton(this.x + 120, this.height / 2 - 40 + 5,
                    20, 18, 0, 0, 19,
                    GUARD_FOLLOWING_ICON, GUARD_NOT_FOLLOWING_ICON, true, guardEntity,
                    (button) -> {
                        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                        buf.writeInt(guardEntity.getId());
                        buf.writeBoolean(isFollowing);
                        ClientPlayNetworking.send(GUARD_FOLLOW_PACKET_ID, buf);
                    })
            );
        }
        if (!Configs.server().features.guards.setGuardPatrolHotv || player.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE)) {
            this.addDrawableChild(new GuardGUIButton(this.x + 145, this.height / 2 - 40 + 5,
                    20, 18, 0, 0, 19,
                    PATROL_ICON, NOT_PATROLLING_ICON, false, guardEntity,
                    (button) -> {
                        isPatrolling = !isPatrolling;
                        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                        buf.writeInt(guardEntity.getId());
                        buf.writeBoolean(isPatrolling);
                        ClientPlayNetworking.send(GUARD_PATROL_PACKET_ID, buf);
                    })
            );
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrixStack, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUARD_GUI_TEXTURES);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrixStack, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        InventoryScreen.drawEntity(i + 52, j + 73, 30, (float) (i + 51) - this.mousePosX, (float) (j + 75 - 50) - this.mousePosY, this.guardEntity);
    }

    @Override
    protected void drawForeground(MatrixStack matrixStack, int x, int y) {
        super.drawForeground(matrixStack, x, y);
        renderHearts(matrixStack);
        renderArmour(matrixStack);
    }

    private void renderHearts(MatrixStack matrixStack) {

        int health = MathHelper.ceil(guardEntity.getHealth());
        int fullHearts = health / 2;
        boolean hasHalfHeart = health % 2 == 1;

        Identifier fullHeart;
        Identifier halfHeart;

        // Choose heart style based on status effects
//        if (guardEntity.hasPoisonClient()) {
//            fullHeart = ICON_FULL_HEART_POISON;
//            halfHeart = ICON_HALF_HEART_POISON;
//        } else if (guardEntity.hasWitherClient()) {
//            fullHeart = ICON_FULL_HEART_WITHER;
//            halfHeart = ICON_HALF_HEART_WITHER;
//        } else if (guardEntity.hasFreezeClient()) {
//            fullHeart = ICON_FULL_HEART_FROZEN;
//            halfHeart = ICON_HALF_HEART_FROZEN;
//        } else {
//            fullHeart = ICON_FULL_HEART;
//            halfHeart = ICON_HALF_HEART;
//        }

        for (int i = 0; i < 10; i++) {
            int drawX = 81 + i * 9;

            matrixStack.push();
            matrixStack.translate(drawX, 18, 0);
            matrixStack.scale(9.0f / 36.0f, 9.0f / 36.0f, 1.0f);

//            RenderSystem.setShaderTexture(0, ICON_EMPTY_HEART);
//            drawTexture(matrixStack, 0, 0, 0, 0, 36, 36, 36, 36);
//
//            if (i < fullHearts) {
//                RenderSystem.setShaderTexture(0, fullHeart);
//                drawTexture(matrixStack, 0, 0, 0, 0, 36, 36, 36, 36);
//            } else if (i == fullHearts && hasHalfHeart) {
//                RenderSystem.setShaderTexture(0, halfHeart);
//                drawTexture(matrixStack, 0, 0, 0, 0, 36, 36, 36, 36);
//            }

            matrixStack.pop();
        }
    }

    private void renderArmour(MatrixStack matrixStack) {
        int armour = guardEntity.getArmor();
        int fullArmour = armour / 2;
        boolean hasHalfArmour = armour % 2 == 1;

        for (int i = 0; i < 10; i++) {
            int drawX = 81 + i * 9;

            matrixStack.push();
            matrixStack.translate(drawX, 28, 0);
            matrixStack.scale(9.0f / 36.0f, 9.0f / 36.0f, 1.0f);

//            RenderSystem.setShaderTexture(0, ICON_EMPTY_ARMOR);
//            drawTexture(matrixStack, 0, 0, 0, 0, 36, 36, 36, 36);
//
//            if (i < fullArmour) {
//                RenderSystem.setShaderTexture(0, ICON_FULL_ARMOR);
//                drawTexture(matrixStack, 0, 0, 0, 0, 36, 36, 36, 36);
//            } else if (i == fullArmour && hasHalfArmour) {
//                RenderSystem.setShaderTexture(0, ICON_HALF_ARMOR);
//                drawTexture(matrixStack, 0, 0, 0, 0, 36, 36, 36, 36);
//            }

            matrixStack.pop();
        }
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.mousePosX = (float) mouseX;
        this.mousePosY = (float) mouseY;
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(matrixStack, mouseX, mouseY);
    }

}
