package com.voranix.ottoke.client.screen;

import net.minecraft.client.Minecraft;

/**
 * Split out of {@code MenuBoardBlock} on purpose - and this split is load-bearing, not stylistic.
 * {@code MenuBoardBlock} is a {@code Block} subclass, so it loads on both dist; a dedicated server
 * crashes the instant such a class is loaded if <em>any</em> of its own methods directly construct a
 * {@code Screen} subclass (here, {@code new MenuInfoScreen()}), because verifying that method's
 * bytecode resolves every type in its stack map frames, including {@code MenuInfoScreen}'s
 * supertype {@code Screen} - which is {@code @OnlyIn(Dist.CLIENT)} - regardless of whether the call
 * is wrapped in {@code DistExecutor}. {@code DistExecutor} only stops the call from
 * <em>running</em>; it does nothing to stop the containing class from being <em>loaded</em>, and
 * loading is what fails.
 * <p>
 * Referencing a same-named <em>method</em> on a separate class (as {@code MenuBoardBlock} now does
 * with {@code MenuInfoScreenOpener::open}) does not have this problem: that only resolves this
 * method's signature, not what is inside its body. The body - and therefore the actual
 * {@code Minecraft}/{@code Screen} reference - only gets resolved when {@link #open()} is loaded,
 * which {@code DistExecutor} correctly gates to the client.
 */
public final class MenuInfoScreenOpener {
    private MenuInfoScreenOpener() {
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new MenuInfoScreen());
    }
}
