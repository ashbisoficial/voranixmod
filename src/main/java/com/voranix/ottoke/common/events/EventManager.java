package com.voranix.ottoke.common.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongConsumer;

/**
 * {@link LongConsumer}, not {@code Consumer<Long>}: {@link #dispatch} runs every server tick, so a
 * boxed {@code Long} would be allocated per listener per tick for no reason - {@link LongConsumer}
 * takes the primitive directly.
 */
public final class EventManager {
    private static final List<LongConsumer> listeners = new ArrayList<>();

    private EventManager() {
    }

    public static void registerListener(LongConsumer listener) {
        listeners.add(listener);
    }

    public static void unregisterListener(LongConsumer listener) {
        listeners.remove(listener);
    }

    public static void dispatch(long tick) {
        for (LongConsumer listener : listeners) {
            listener.accept(tick);
        }
    }
}
