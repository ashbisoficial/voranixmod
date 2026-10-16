package com.voranix.maipizza.client.screen;

import net.minecraft.client.Minecraft;

/**
 * Mirrors {@code com.voranix.ottoke.client.screen.MenuInfoScreenOpener} - see that class for why
 * this split (not inlined into the {@code Block} subclass) is load-bearing for dedicated servers,
 * not stylistic.
 */
public final class MenuInfoScreenOpener {
    private MenuInfoScreenOpener() {
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new MenuInfoScreen());
    }
}
