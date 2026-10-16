package com.voranix.maipizza.common.block;

import com.voranix.maipizza.client.screen.MenuInfoScreenOpener;
import com.voranix.voranixmod.common.block.HorizontalPanelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

/**
 * Mirrors {@code com.voranix.ottoke.common.block.MenuBoardBlock} - see that class for why the
 * screen-opening call is split into {@link MenuInfoScreenOpener} rather than inlined here.
 */
public class MenuBoardBlock extends HorizontalPanelBlock {
    public MenuBoardBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> MenuInfoScreenOpener::open);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
