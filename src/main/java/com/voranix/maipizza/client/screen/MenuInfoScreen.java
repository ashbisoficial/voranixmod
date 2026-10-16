package com.voranix.maipizza.client.screen;

import com.voranix.maipizza.common.item.MaiPizzaCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Mirrors {@code com.voranix.ottoke.client.screen.MenuInfoScreen} exactly - iterates
 * {@link MaiPizzaCategory#values()} instead of naming a single item, so a new pizza (or a future
 * second category) needs no change here.
 */
public class MenuInfoScreen extends Screen {
    private static final int LINE_HEIGHT = 12;
    private static final int CATEGORY_GAP = 10;
    private static final int TITLE_Y = 20;
    private static final int CONTENT_START_Y = 40;
    private static final int TITLE_COLOR = 0xFFFFFF;
    private static final int CATEGORY_COLOR = 0xFFD700;
    private static final int ITEM_COLOR = 0xCCCCCC;

    private final Map<MaiPizzaCategory, Component> categoryTitles = new EnumMap<>(MaiPizzaCategory.class);
    private final Map<MaiPizzaCategory, List<Component>> itemNamesByCategory = new EnumMap<>(MaiPizzaCategory.class);

    public MenuInfoScreen() {
        super(Component.translatable("screen.maipizza.menu.title"));
    }

    @Override
    protected void init() {
        super.init();
        for (MaiPizzaCategory category : MaiPizzaCategory.values()) {
            this.categoryTitles.put(category, category.getDisplayName());

            List<Component> itemNames = new ArrayList<>();
            category.getItems().forEach(item -> itemNames.add(item.getDescription()));
            this.itemNamesByCategory.put(category, itemNames);
        }

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
                .bounds(this.width / 2 - 50, this.height - 28, 100, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, TITLE_Y, TITLE_COLOR);

        int y = CONTENT_START_Y;
        for (MaiPizzaCategory category : MaiPizzaCategory.values()) {
            guiGraphics.drawCenteredString(this.font, this.categoryTitles.get(category), this.width / 2, y, CATEGORY_COLOR);
            y += LINE_HEIGHT;
            for (Component itemName : this.itemNamesByCategory.get(category)) {
                guiGraphics.drawCenteredString(this.font, itemName, this.width / 2, y, ITEM_COLOR);
                y += LINE_HEIGHT;
            }
            y += CATEGORY_GAP;
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
