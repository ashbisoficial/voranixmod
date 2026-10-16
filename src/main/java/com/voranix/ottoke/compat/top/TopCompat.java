package com.voranix.ottoke.compat.top;

import com.voranix.voranixmod.VoranixMod;
import com.voranix.ottoke.common.cooking.FreidoraBlockEntity;
import com.voranix.voranixmod.common.cooking.system.CookingProcess;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.InterModComms;

import java.util.function.Function;

/**
 * The One Probe side of the compat layer - see {@link com.voranix.ottoke.compat.ModCompatEvents}
 * for why this class only ever loads when TOP is actually present.
 * <p>
 * {@link IProbeInfoProvider#addProbeInfo} runs on the logical server (TOP ships the result to the
 * looking player itself), so unlike the Jade integration there is no separate server/client data
 * split to write - the live {@link CookingProcess} is read directly.
 */
public final class TopCompat {
    private TopCompat() {
    }

    public static void sendCapabilities() {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", ProbeCallback::new);
    }

    private static final class ProbeCallback implements Function<ITheOneProbe, Void> {
        @Override
        public Void apply(ITheOneProbe probe) {
            probe.registerProvider(new FreidoraProbeInfoProvider());
            return null;
        }
    }

    private static final class FreidoraProbeInfoProvider implements IProbeInfoProvider {
        private static final ResourceLocation ID = new ResourceLocation(VoranixMod.MODID, "freidora");

        @Override
        public ResourceLocation getID() {
            return ID;
        }

        @Override
        public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level level,
                                  BlockState blockState, IProbeHitData data) {
            BlockEntity blockEntity = level.getBlockEntity(data.getPos());
            if (!(blockEntity instanceof FreidoraBlockEntity freidora)) {
                return;
            }
            CookingProcess process = freidora.getCookingProcess();
            if (process.isActive()) {
                info.progress(process.getProgress(), process.getTotalTime());
            }
        }
    }
}
