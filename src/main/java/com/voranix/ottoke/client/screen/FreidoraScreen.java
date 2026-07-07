package com.voranix.ottoke.client.screen;

import com.voranix.ottoke.common.cooking.FreidoraMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FreidoraScreen extends AbstractContainerScreen<FreidoraMenu> {
    public FreidoraScreen(FreidoraMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xFF1A1A1A);
        guiGraphics.fill(x + 4, y + 4, x + this.imageWidth - 4, y + this.imageHeight - 4, 0xFF2A2A2A);
        guiGraphics.fill(x + 48, y + 28, x + 132, y + 48, 0xFF3A3A3A);

        int progress = this.menu.getCookingTotalTime() > 0 ? this.menu.getCookingProgress() * 80 / this.menu.getCookingTotalTime() : 0;
        guiGraphics.fill(x + 49, y + 29, x + 49 + progress, y + 47, 0xFF00CC66);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
