package com.voranix.voranixmod.common.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

/**
 * Base for decorative, non-cubic panel blocks that hang against a wall and should face whichever
 * side the player placed them on - Banner Ottoke, Cartel, Cartel Luminoso, and any future sign/menu
 * panel. Rotates on placement the same way vanilla's {@code WallSignBlock} does: four horizontal
 * facings via a plain blockstate property, generated as ordinary 90-degree model rotations in the
 * blockstate JSON (see {@code ModBlockStateProvider#horizontalBlock}).
 * <p>
 * Not vanilla's finer 16-step standing-sign rotation: that mechanic renders at an arbitrary angle via
 * a {@code BlockEntityRenderer}, which needs a BlockEntity to render from. None of these panels have
 * one, so four-way rotation - achievable with plain blockstate variants - is the ceiling, not sixteen.
 */
public class HorizontalPanelBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public HorizontalPanelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
