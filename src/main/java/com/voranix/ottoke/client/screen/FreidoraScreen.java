package com.voranix.ottoke.client.screen;

import com.voranix.ottoke.common.cooking.FreidoraMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * The Freidora's own GUI. Reuses vanilla's {@code textures/gui/container/furnace.png} directly at
 * runtime rather than shipping a copy or hand-drawing an equivalent - Forge guarantees the vanilla
 * resource pack is always present, so referencing it by its vanilla {@link ResourceLocation} is a
 * normal, standard way to reuse a base-game asset without redistributing it in this mod's jar. It
 * also means zero new art had to be authored to get a real framed/beveled look, which a hand-drawn
 * recreation would not have matched pixel-for-pixel anyway.
 * <p>
 * {@link FreidoraMenu}'s three station slots are positioned at furnace.png's own baked-in slot
 * coordinates (56,17 / 56,53 / 116,35) specifically so the texture's recessed-slot art lines up
 * exactly with the real interactive slots - this screen does not offset anything relative to the
 * menu, it trusts the menu's coordinates completely, the same relationship
 * {@code AbstractFurnaceScreen} has with {@code AbstractFurnaceMenu}.
 * <p>
 * The progress arrow reuses furnace.png's own arrow sprite (the same source pixels vanilla's furnace
 * screen blits) rather than the flat-color vertical bar this screen used before switching to a real
 * background - a custom color fill would have clashed with the framed/wood-grain look. The flame
 * icon is also furnace.png's own sprite, but drawn as a simple on/off state (full or absent) instead
 * of vanilla's gradual burn-down animation: the Freidora charges fuel whole on recipe completion, not
 * tick-by-tick like a furnace (see {@link FreidoraMenu#hasFuel()}), so there is no "remaining burn
 * time" fraction to animate honestly - only "is there usable fuel right now".
 */
public class FreidoraScreen extends AbstractContainerScreen<FreidoraMenu> {
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

    public FreidoraScreen(FreidoraMenu menu, Inventory inventory, Component title) {
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

    /**
     * Reads progress straight from the menu's {@code ContainerData} - the same data slots
     * {@code FreidoraMenu} wires from {@code FreidoraBlockEntity.getData()}, which is the process's
     * own {@code SimpleCookingProcess} instance. No separate network message exists or is needed for
     * this bar; it is exactly what {@code CookingProcess extends ContainerData} was built for.
     */
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
