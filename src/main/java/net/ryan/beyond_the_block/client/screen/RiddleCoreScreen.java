package net.ryan.beyond_the_block.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.riddles.Riddle;
import net.ryan.beyond_the_block.content.riddles.RiddleClientCache;
import net.ryan.beyond_the_block.content.riddles.RiddleDataManager;
import net.ryan.beyond_the_block.screen.handler.RiddleCoreScreenHandler;
import net.ryan.beyond_the_block.utils.visual.ScrollBarWidget;

import java.util.*;


public class RiddleCoreScreen extends HandledScreen<RiddleCoreScreenHandler> {


    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/demo_background.png");

    private ScrollBarWidget activeRiddlesScroll;
    private ScrollBarWidget completedRiddlesScroll;
    int entriesPerPage;
    int btnWidth;
    int btnHeight;


    private enum Tab {
        INFO, ACTIVE, COMPLETED
    }

    private Tab currentTab = Tab.INFO;

    public RiddleCoreScreen(RiddleCoreScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 256;
        this.backgroundHeight = 200;
    }

    @Override
    protected void init() {
        super.init();

        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;

        // Calculate the position for buttons relative to the background
        int buttonXOffset = 5; // Offset for spacing between buttons (adjust as needed)
        btnWidth = 60;
        btnHeight = 20;
        int totalButtonWidth = (btnWidth * 3) + (buttonXOffset * 2); // Total width of both buttons and the space between them
        int btn1 = x + (backgroundWidth - totalButtonWidth) / 2; // Center the buttons
        int btn2 = btn1 + btnWidth + buttonXOffset;
        int btn3 = btn2 + btnWidth + buttonXOffset;

        List<ButtonWidget> tabButtons = new ArrayList<>();
        tabButtons.add(this.addDrawableChild(new ButtonWidget(btn1, y + 20, btnWidth, btnHeight, Text.literal("Info"), button -> switchTab(Tab.INFO))));
        tabButtons.add(this.addDrawableChild(new ButtonWidget(btn2, y + 20, btnWidth, btnHeight, Text.literal("Active"), button -> switchTab(Tab.ACTIVE))));
        tabButtons.add(this.addDrawableChild(new ButtonWidget(btn3, y + 20, btnWidth, btnHeight, Text.literal("Completed"), button -> switchTab(Tab.COMPLETED))));

        int scrollX = x + backgroundWidth - 28;
        int scrollY = y + 45;
        int scrollHeight = backgroundHeight - 85;
        entriesPerPage = 2;

        activeRiddlesScroll = new ScrollBarWidget(scrollX, scrollY, scrollHeight, RiddleClientCache.activeRiddles.size(), entriesPerPage);
        completedRiddlesScroll = new ScrollBarWidget(scrollX, scrollY, scrollHeight, RiddleClientCache.completedRiddles.size(), entriesPerPage);
    }

    private void switchTab(Tab tab) {
        if (this.currentTab == tab) return;
        this.currentTab = tab;
        this.clearChildren();
        this.init();
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        textRenderer.draw(matrices, title, (float) this.titleX, (float) this.titleY, 4210752);  // Keeps the main title

        switch(currentTab){
            case INFO -> renderInfoTab(matrices);
            case ACTIVE -> renderActiveTab(matrices);
            case COMPLETED -> renderCompletedTab(matrices);
        }

    }

    private void renderInfoTab(MatrixStack matrices) {
        long seconds = RiddleDataManager.getSecondsUntilNextDay();
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        String timeString = "Generating Riddle in: " + minutes + "m " + remainingSeconds + "s";

        int startY = this.y + 30;
        int startX = this.titleX + 15;

        textRenderer.draw(matrices, timeString, startX, startY, 4210752);
        textRenderer.draw(matrices, "Active Riddles: " + RiddleClientCache.activeRiddles.size(), startX, startY + 10, 4210752);
        textRenderer.draw(matrices, "Completed Riddles: " + RiddleClientCache.completedRiddles.size(), startX, startY + 20, 4210752);
    }

    private void renderActiveTab(MatrixStack matrices) {
        Map<UUID, Riddle> activeRiddles = RiddleClientCache.activeRiddles;
        int offset = activeRiddlesScroll.getScrollOffset();
        int startY = this.y + 30;
        int startX = this.titleX + 15;

        if (activeRiddles.isEmpty()) {
            textRenderer.draw(matrices, "No active riddles.", startX, startY, 4210752);
            return;
        }
        List<Map.Entry<UUID, Riddle>> scrollableActiveRiddles = new ArrayList<>(activeRiddles.entrySet());
        for(int i = offset; i < Math.min(offset + entriesPerPage, scrollableActiveRiddles.size()); i++){
            Map.Entry<UUID, Riddle> entry = scrollableActiveRiddles.get(i);
            UUID playerId = entry.getKey();
            Riddle riddle = entry.getValue();
            drawPlayerHead(matrices, startX, startY, playerId);
            textRenderer.draw(matrices, handler.getPlayerNameFromUUID(playerId) + " - ", startX + 15, startY + 5, 4210752);
            String shortRiddleID = riddle.id().toString().substring(0, 8);
            textRenderer.draw(matrices, shortRiddleID.toUpperCase(), startX + textRenderer.getWidth(handler.getPlayerNameFromUUID(playerId) + " - ") + 20, startY + 5, 4210752);
            startY += 15;
        }
    }

    private void renderCompletedTab(MatrixStack matrices) {
        Map<UUID, Set<UUID>> completedRiddles = RiddleClientCache.completedRiddles;
        int offset = completedRiddlesScroll.getScrollOffset();
        int startY = this.y + 30;
        int startX = this.titleX + 15;
        if (completedRiddles.isEmpty()) {
            textRenderer.draw(matrices, "No completed riddles.", startX, startY, 4210752);
            return;
        }
        List<Map.Entry<UUID, Set<UUID>>> scrollableCompletedRiddles = new ArrayList<>(completedRiddles.entrySet());
        for(int i = offset; i < Math.min(offset + entriesPerPage, scrollableCompletedRiddles.size()); i++){
            Map.Entry<UUID, Set<UUID>> entry = scrollableCompletedRiddles.get(i);
            UUID playerId = entry.getKey();
            drawPlayerHead(matrices, startX, startY, playerId);
            textRenderer.draw(matrices, handler.getPlayerNameFromUUID(playerId) + " " + entry.getValue().size(), startX + 20, startY + 5, 4210752);
            startY += 15;
        }
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
        if (currentTab == Tab.ACTIVE
                && RiddleClientCache.activeRiddles.size() > entriesPerPage) {
            if (client != null) {
                activeRiddlesScroll.render(matrices, client);
            }
        } else if (currentTab == Tab.COMPLETED
                && RiddleClientCache.completedRiddles.size() > entriesPerPage) {
            if (client != null) {
                completedRiddlesScroll.render(matrices, client);
            }
        }
    }


    private void drawPlayerHead(MatrixStack matrices, int x, int y, UUID playerUUID) {
        ItemStack playerHead = new ItemStack(Items.PLAYER_HEAD);
        NbtCompound tag = new NbtCompound();
        tag.putString("SkullOwner", handler.getPlayerNameFromUUID(playerUUID));
        playerHead.setNbt(tag);
        this.itemRenderer.renderInGui(playerHead, x, y);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (currentTab == Tab.ACTIVE) {
            activeRiddlesScroll.onMouseScroll(amount);
        } else if (currentTab == Tab.COMPLETED) {
            completedRiddlesScroll.onMouseScroll(amount);
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentTab == Tab.ACTIVE && activeRiddlesScroll.onMouseClicked(mouseX, mouseY)) return true;
        if (currentTab == Tab.COMPLETED && completedRiddlesScroll.onMouseClicked(mouseX, mouseY)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (currentTab == Tab.ACTIVE && activeRiddlesScroll.onMouseReleased(mouseX, mouseY)) return true;
        if (currentTab == Tab.COMPLETED && completedRiddlesScroll.onMouseReleased(mouseX, mouseY)) return true;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (currentTab == Tab.ACTIVE && activeRiddlesScroll.onMouseDragged(mouseX, mouseY)) return true;
        if (currentTab == Tab.COMPLETED && completedRiddlesScroll.onMouseDragged(mouseX, mouseY)) return true;
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }
}