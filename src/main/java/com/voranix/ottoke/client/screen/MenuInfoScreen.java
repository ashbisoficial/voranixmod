package com.voranix.ottoke.client.screen;

import com.voranix.ottoke.common.item.FoodCategory;
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
 * Purely informational - no slots, no {@code AbstractContainerMenu}, no server round-trip. Opened
 * directly by {@code MenuBoardBlock} on the client, the way vanilla's own non-container screens work.
 * <p>
 * Never names a single item or category in code: it iterates {@link FoodCategory#values()} and asks
 * each one for its {@link FoodCategory#getItems()} and {@link FoodCategory#getDisplayName()}, both
 * translatable. Adding a new Banderilla/Bebida - or a whole new {@link FoodCategory} constant later -
 * requires zero changes to this class.
 * <p>
 * Everything {@link #render} draws text from is resolved once in {@link #init()}, never freshly
 * built inside {@link #render} itself: {@link FoodCategory#getItems()} scans the whole item registry,
 * and both {@link FoodCategory#getDisplayName()} and {@code Item.getDescription()} allocate a new
 * translatable {@link Component} on every call. Render runs every frame, so doing any of that ~60+
 * times a second while the screen just sits open would be pointless repeated work for text that
 * cannot change while the screen is up.
 */
public class MenuInfoScreen extends Screen {
    private static final int LINE_HEIGHT = 12;
    private static final int CATEGORY_GAP = 10;
    private static final int TITLE_Y = 20;
    private static final int CONTENT_START_Y = 40;
    private static final int TITLE_COLOR = 0xFFFFFF;
    private static final int CATEGORY_COLOR = 0xFFD700;
    private static final int ITEM_COLOR = 0xCCCCCC;

    private final Map<FoodCategory, Component> categoryTitles = new EnumMap<>(FoodCategory.class);
    private final Map<FoodCategory, List<Component>> itemNamesByCategory = new EnumMap<>(FoodCategory.class);

    public MenuInfoScreen() {
        super(Component.translatable("screen.ottoke.menu.title"));
    }

    @Override
    protected void init() {
        super.init();
        for (FoodCategory category : FoodCategory.values()) {
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
        for (FoodCategory category : FoodCategory.values()) {
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
