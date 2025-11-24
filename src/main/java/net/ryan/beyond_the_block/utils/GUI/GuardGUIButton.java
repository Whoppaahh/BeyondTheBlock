package net.ryan.beyond_the_block.utils.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.village.GuardVillager.GuardEntity;

public class GuardGUIButton extends TexturedButtonWidget{
        private final Identifier texture;
        private final Identifier newTexture;
        private final boolean isFollowButton;
        private final GuardEntity guardEntity;

        public GuardGUIButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, Identifier resourceLocationIn, Identifier newTexture, boolean isFollowButton, GuardEntity guardEntity, PressAction pressAction) {
            super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, pressAction);
            this.texture = resourceLocationIn;
            this.newTexture = newTexture;
            this.isFollowButton = isFollowButton;
            this.guardEntity = guardEntity;
        }

        public boolean requirementsForTexture() {
            boolean following = guardEntity.isFollowing();
            boolean patrol = guardEntity.isPatrolling();
            return this.isFollowButton ? following : patrol;
        }

        @Override
        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Identifier icon = this.requirementsForTexture() ? texture : newTexture;
            RenderSystem.setShaderTexture(0, icon);
            int i = this.v;
            if (this.isHovered()) {
                i += this.hoveredVOffset;
            }

            RenderSystem.enableDepthTest();
            drawTexture(matrixStack, this.x, this.y, (float) v, (float) i, this.width, this.height, textureWidth, textureHeight);
        }
    }
