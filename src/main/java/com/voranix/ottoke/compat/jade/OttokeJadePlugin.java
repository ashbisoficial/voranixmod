package com.voranix.ottoke.compat.jade;

import com.voranix.ottoke.common.cooking.FreidoraBlock;
import com.voranix.ottoke.common.cooking.FreidoraBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

/**
 * Discovered by Jade itself through {@link WailaPlugin} annotation scanning - nothing else in the
 * mod imports this class or {@code compat.jade} at all, so this file (and therefore
 * {@code snownee.jade.*}) only ever needs to resolve when Jade's own plugin loader finds and loads
 * it, which only happens when Jade is actually installed. See {@code ModCompat}'s class doc for how
 * this differs from The One Probe's integration, which has no such self-discovery.
 */
@WailaPlugin("ottoke")
public final class OttokeJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(FreidoraJadeProvider.INSTANCE, FreidoraBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(FreidoraJadeProvider.INSTANCE, FreidoraBlock.class);
    }
}
