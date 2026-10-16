package com.voranix.maipizza.client.screen;

import com.voranix.maipizza.common.cooking.HornoDeBarroMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Mirrors {@code com.voranix.ottoke.client.screen.FreidoraScreen} exactly - same reused vanilla
 * {@code furnace.png}, same reasoning (no new art needed, on/off flame instead of vanilla's gradual
 * burn-down since fuel is charged whole on completion). Swap {@link #TEXTURE} for a real Horno de
 * Barro background once one exists; the slot coordinates below already match
 * {@link HornoDeBarroMenu}'s baked-in 56,17 / 56,53 / 116,35 layout either way.
 */
public class HornoDeBarroScreen extends AbstractContainerScreen<HornoDeBarroMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");

    private static final int ARROW_X = 79;
    private static final int ARROW_Y = 34;
    private static final int ARROW_U = 176;
    private static final int ARROW_V = 14;
    private static final int ARROW_WIDTH = 24;
    private static final int ARROW_HEIGHT = 16;

    private static final int FLAME_X = 56;
    private static final int FLAME_Y = 36;
    private static final int FLAME_U = 176;
    private static final int FLAME_V = 0;
    private static final int FLAME_WIDTH = 14;
    private static final int FLAME_HEIGHT = 14;

    public HornoDeBarroScreen(HornoDeBarroMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.hasFuel()) {
            guiGraphics.blit(TEXTURE, x + FLAME_X, y + FLAME_Y, FLAME_U, FLAME_V, FLAME_WIDTH, FLAME_HEIGHT);
        }

        renderProgressArrow(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        int totalTime = this.menu.getCookingTotalTime();
        if (totalTime <= 0) {
            return;
        }

        int filledWidth = this.menu.getCookingProgress() * ARROW_WIDTH / totalTime;
        if (filledWidth <= 0) {
            return;
        }

        guiGraphics.blit(TEXTURE, x + ARROW_X, y + ARROW_Y, ARROW_U, ARROW_V, filledWidth + 1, ARROW_HEIGHT);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
