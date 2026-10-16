package com.voranix.ottoke.common.block;

import com.voranix.voranixmod.common.block.HorizontalPanelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A light source toggled by right-click, on top of {@link HorizontalPanelBlock}'s facing - no
 * BlockEntity, because "on or off" is exactly what a {@link BooleanProperty} on the blockstate
 * already is: persisted with the chunk automatically, with zero NBT of its own. Nothing about this
 * block varies per-instance beyond that flag and its facing, so there is nothing a BlockEntity would
 * add.
 * <p>
 * Light level is wired directly to {@link #LIT} via {@code BlockBehaviour.Properties#lightLevel} at
 * registration (see {@code ModBlocks}), not computed here - the block only owns the state, not the
 * property's registration-time configuration.
 */
public class CartelLuminosoBlock extends HorizontalPanelBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public CartelLuminosoBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            boolean lit = !state.getValue(LIT);
            level.setBlock(pos, state.setValue(LIT, lit), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, lit ? 0.6F : 0.5F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
