package com.voranix.ottoke.compat;

import net.minecraftforge.fml.ModList;

/**
 * Single source of truth for "is mod X installed right now". Every optional integration in
 * {@code compat.*} reads its target mod's presence from here instead of calling
 * {@link ModList#isLoaded} directly - one place to see the mod's whole compatibility surface, and
 * one place to fix if a mod id ever needs to change.
 * <p>
 * The booleans are computed once, at class-init time, not re-queried per call: {@link ModList} is
 * fixed for the lifetime of the JVM (mods don't load or unload mid-session), so this is exactly
 * the kind of value that belongs cached, not recomputed - see the mod's own performance audit for
 * why that distinction matters.
 * <p>
 * None of these are read to decide whether {@code compat.jei}/{@code compat.emi}/{@code compat.jade}
 * ever get <em>loaded</em> - JEI, EMI, and Jade each discover their own plugin class through their
 * own framework (annotation scanning or {@code ServiceLoader}), which only ever runs when that mod
 * itself is present. {@link #THE_ONE_PROBE} is the one flag actually consulted before doing
 * anything: The One Probe has no such self-discovery, so <em>we</em> have to decide whether to send
 * it an IMC message - see {@link ModCompatEvents}. The rest exist for the data-driven integrations
 * (conditional recipes, tag contributions) and for any future code that needs the same check.
 */
public final class ModCompat {
    public static final String FARMERS_DELIGHT_ID = "farmersdelight";
    public static final String CREATE_ID = "create";
    public static final String JEI_ID = "jei";
    public static final String EMI_ID = "emi";
    public static final String JADE_ID = "jade";
    public static final String THE_ONE_PROBE_ID = "theoneprobe";

    public static final boolean FARMERS_DELIGHT = ModList.get().isLoaded(FARMERS_DELIGHT_ID);
    public static final boolean CREATE = ModList.get().isLoaded(CREATE_ID);
    public static final boolean JEI = ModList.get().isLoaded(JEI_ID);
    public static final boolean EMI = ModList.get().isLoaded(EMI_ID);
    public static final boolean JADE = ModList.get().isLoaded(JADE_ID);
    public static final boolean THE_ONE_PROBE = ModList.get().isLoaded(THE_ONE_PROBE_ID);

    private ModCompat() {
    }
}
