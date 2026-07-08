package com.voranix.ottoke.client.screen;

import com.voranix.ottoke.common.cooking.FreidoraMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * The Freidora's own GUI - not FurnaceScreen with different numbers. No art texture exists yet (the
 * mod has no image assets at all), so slots are drawn as plain outlined squares and the background
 * is flat fills; swapping in a real texture later only touches {@link #renderBg}, since
 * {@link FreidoraMenu} already owns the actual slot coordinates this class mirrors for rendering.
 * <p>
 * The progress indicator is a vertical bar that fills bottom-to-top between the ingredient and fuel
 * slots, not vanilla's left-to-right arrow - a deliberate difference from the furnace that also
 * reads naturally as "heat rising" for a fryer.
 */
public class FreidoraScreen extends AbstractContainerScreen<FreidoraMenu> {
    private static final int SLOT_SIZE = 18;
    private static final int INGREDIENT_SLOT_X = 47;
    private static final int INGREDIENT_SLOT_Y = 24;
    private static final int FUEL_SLOT_X = 47;
    private static final int FUEL_SLOT_Y = 52;
    private static final int RESULT_SLOT_X = 116;
    private static final int RESULT_SLOT_Y = 38;

    private static final int PROGRESS_X = 82;
    private static final int PROGRESS_Y = 22;
    private static final int PROGRESS_WIDTH = 14;
    private static final int PROGRESS_HEIGHT = 34;

    public FreidoraScreen(FreidoraMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xFF1A1A1A);
        guiGraphics.fill(x + 4, y + 4, x + this.imageWidth - 4, y + this.imageHeight - 4, 0xFF2A2A2A);

        renderSlotOutline(guiGraphics, x, y, INGREDIENT_SLOT_X, INGREDIENT_SLOT_Y);
        renderSlotOutline(guiGraphics, x, y, FUEL_SLOT_X, FUEL_SLOT_Y);
        renderSlotOutline(guiGraphics, x, y, RESULT_SLOT_X, RESULT_SLOT_Y);

        renderProgressBar(guiGraphics, x, y);
    }

    private void renderSlotOutline(GuiGraphics guiGraphics, int x, int y, int slotX, int slotY) {
        guiGraphics.fill(x + slotX - 1, y + slotY - 1, x + slotX + SLOT_SIZE + 1, y + slotY + SLOT_SIZE + 1, 0xFF000000);
        guiGraphics.fill(x + slotX, y + slotY, x + slotX + SLOT_SIZE, y + slotY + SLOT_SIZE, 0xFF8B8B8B);
    }

    /**
     * Reads progress straight from the menu's {@code ContainerData} - the same data slots
     * {@code FreidoraMenu} wires from {@code FreidoraBlockEntity.getData()}, which is the process's
     * own {@code SimpleCookingProcess} instance. No separate network message exists or is needed for
     * this bar; it is exactly what {@code CookingProcess extends ContainerData} was built for.
     */
    private void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x + PROGRESS_X, y + PROGRESS_Y, x + PROGRESS_X + PROGRESS_WIDTH, y + PROGRESS_Y + PROGRESS_HEIGHT, 0xFF0F0F0F);

        int totalTime = this.menu.getCookingTotalTime();
        if (totalTime <= 0) {
            return;
        }

        int filledHeight = this.menu.getCookingProgress() * PROGRESS_HEIGHT / totalTime;
        int filledTop = y + PROGRESS_Y + (PROGRESS_HEIGHT - filledHeight);
        guiGraphics.fill(x + PROGRESS_X, filledTop, x + PROGRESS_X + PROGRESS_WIDTH, y + PROGRESS_Y + PROGRESS_HEIGHT, 0xFFFF6600);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
