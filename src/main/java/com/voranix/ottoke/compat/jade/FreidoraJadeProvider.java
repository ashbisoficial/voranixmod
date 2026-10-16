package com.voranix.ottoke.compat.jade;

import com.voranix.voranixmod.VoranixMod;
import com.voranix.ottoke.common.cooking.FreidoraBlockEntity;
import com.voranix.voranixmod.common.cooking.system.CookingProcess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;

/**
 * Shows Freidora cooking progress in Jade's tooltip while just looking at the block, with no menu
 * open. This needs the server/client split {@link IServerDataProvider}/{@link IBlockComponentProvider}
 * give: {@link FreidoraBlockEntity} only syncs its {@link CookingProcess} to the client through
 * {@code ContainerData} while its menu is open (see {@code FreidoraMenu#addDataSlots}), so a client-side
 * probe query has no live progress to read on its own. {@link #appendServerData} runs on the
 * server, where the real {@link CookingProcess} is always current, and ships just the two ints the
 * tooltip needs; {@link #appendTooltip} runs on the client and only ever reads what was shipped.
 */
public final class FreidoraJadeProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    public static final FreidoraJadeProvider INSTANCE = new FreidoraJadeProvider();

    private static final ResourceLocation UID = new ResourceLocation(VoranixMod.MODID, "freidora");
    private static final String TAG_PROGRESS = "Progress";
    private static final String TAG_TOTAL_TIME = "TotalTime";

    private FreidoraJadeProvider() {
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof FreidoraBlockEntity freidora) {
            CookingProcess process = freidora.getCookingProcess();
            data.putInt(TAG_PROGRESS, process.getProgress());
            data.putInt(TAG_TOTAL_TIME, process.getTotalTime());
        }
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        int totalTime = data.getInt(TAG_TOTAL_TIME);
        if (totalTime <= 0) {
            return;
        }
        float progress = data.getInt(TAG_PROGRESS) / (float) totalTime;
        tooltip.add(IElementHelper.get().progress(progress,
                Component.translatable("jade.ottoke.freidora.cooking"),
                IElementHelper.get().progressStyle(),
                BoxStyle.DEFAULT,
                true));
    }
}
