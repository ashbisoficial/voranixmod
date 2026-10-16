package com.voranix.ottoke.common.cooking;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

public class FreidoraBlock extends BaseEntityBlock {
    /**
     * Mirrors {@code CartelLuminosoBlock}'s {@code LIT} pattern, but with no toggle - only
     * {@link FreidoraBlockEntity#serverTick} ever changes it, flipping it to match
     * {@code CookingProcess#isActive()} exactly when a cook starts or stops. Light emission is wired
     * to it below via {@code lightLevel}, the same way Cartel Luminoso wires its own light to
     * {@code LIT} in {@code ModBlocks}.
     */
    public static final BooleanProperty COOKING = BooleanProperty.create("cooking");

    /**
     * Same {@code HORIZONTAL_FACING} rotate-on-placement behavior as {@code HorizontalPanelBlock} -
     * duplicated here rather than shared through it, because Java has no multiple inheritance and
     * this block already extends {@code BaseEntityBlock} for its {@link FreidoraBlockEntity}, which
     * {@code HorizontalPanelBlock} (a plain {@code Block}) can't also be.
     */
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public FreidoraBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.0f, 6.0f).noOcclusion()
                .lightLevel(state -> state.getValue(COOKING) ? 13 : 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(COOKING, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COOKING).add(FACING);
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

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FreidoraBlockEntity(pos, state);
    }

    /**
     * {@code BaseEntityBlock}'s own default is {@code RenderShape.INVISIBLE} - it assumes a block
     * entity normally means the block is rendered entirely by a {@code BlockEntityRenderer}, the way
     * a chest or banner is. The Freidora has no BER; its entire visual is the JSON model wired in
     * {@code ModBlockStateProvider}. Leaving the inherited default meant the placed block never drew
     * anything (its item form still rendered fine, since item rendering goes through the model
     * system directly and never calls this method at all) - this override is what makes the model
     * actually appear once placed.
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, net.minecraft.world.phys.BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof FreidoraBlockEntity blockEntity) {
            MenuProvider provider = blockEntity;
            NetworkHooks.openScreen((ServerPlayer) player, provider, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : (level1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof FreidoraBlockEntity freidora) {
                freidora.serverTick(level1);
            }
        };
    }

    /**
     * Without this, breaking the block silently deletes whatever ingredient, fuel, or finished
     * result was sitting in it - BaseEntityBlock does not drop a block entity's inventory on its
     * own, unlike the block itself (already handled by the loot table). Only fires when the block
     * actually changes (not on a same-block state change) so mid-cook contents survive things like
     * a neighbor update.
     */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof FreidoraBlockEntity freidora) {
                ItemStackHandler inventory = freidora.getInventory();
                for (int slot = 0; slot < inventory.getSlots(); slot++) {
                    ItemStack stack = inventory.getStackInSlot(slot);
                    if (!stack.isEmpty()) {
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                    }
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
