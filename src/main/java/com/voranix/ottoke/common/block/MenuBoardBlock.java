package com.voranix.ottoke.common.block;

import com.voranix.ottoke.client.screen.MenuInfoScreenOpener;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

/**
 * The "Menu" block: a purely informational board, not a trading or storage block. Deliberately a
 * plain {@code Block}, not {@code BaseEntityBlock} - there is nothing to store. The screen it opens
 * lists whatever {@code FoodCategory.BANDERILLA}/{@code BEBIDA} resolve to from the item registry at
 * the moment it's opened, which is identical for every placed instance of this block and never
 * varies per-block, per-world, or per-player. No inventory, no trade, no server state at all, so the
 * whole interaction stays client-side; nothing is synced because nothing needs to be.
 * <p>
 * The actual {@code Minecraft}/{@code Screen} reference lives in {@link MenuInfoScreenOpener}, a
 * separate class, not inlined here - see that class's doc for why a {@code Block} subclass (loaded
 * on both dist) must never itself contain a direct {@code new SomeScreen()} call, even one wrapped
 * in {@link DistExecutor#unsafeRunWhenOn}: {@code DistExecutor} gates whether the call runs, not
 * whether the class containing it gets loaded, and loading is what a dedicated server can't survive.
 */
public class MenuBoardBlock extends Block {
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
