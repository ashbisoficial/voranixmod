package com.voranix.voranixmod.common.block;

import com.voranix.voranixmod.common.item.FoodDefinition;
import com.voranix.voranixmod.common.item.FoodEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

/**
 * Base for foods that place as a block and get eaten one slice at a time, the same way vanilla
 * {@code CakeBlock} works - unlike {@code OttokeFoodItem}/{@code MaiPizzaFoodItem}, which are
 * consumed whole in a single right-click. {@link #TOTAL_SLICES} is fixed rather than per-instance,
 * so every subclass shares one {@link #SLICES_EATEN} blockstate property instead of each needing
 * its own with a different bound.
 * <p>
 * Reuses {@link FoodDefinition}/{@link FoodEffect} exactly as {@code OttokeFoodItem} does for
 * whole-item foods: {@link #getFoodDefinition()} supplies the pizza's total nutrition/saturation/
 * effects, and each slice eaten here restores {@code total/TOTAL_SLICES} hunger and saturation and
 * re-rolls every {@link FoodEffect} in the list (per-slice reapplication, not a one-time bonus on
 * the last slice - the user asked for eating a slice to feel like eating a mini banderilla each
 * time).
 */
public abstract class SliceableFoodBlock extends Block {
    /**
     * 4 blockstate values (0-3) => the 4 Blockbench models the user actually delivered for Pizza
     * Margarita: {@code completa} (0, whole), {@code 34} (1, three-quarters), {@code 12} (2, half),
     * {@code 14} (3, one quarter). The 4th right-click, eaten from state 3, removes the block
     * entirely - no "empty" model needed, vanishing has no visual state.
     */
    public static final int TOTAL_SLICES = 4;
    public static final IntegerProperty SLICES_EATEN = IntegerProperty.create("slices_eaten", 0, TOTAL_SLICES - 1);

    private static final VoxelShape[] SHAPE_BY_SLICES_EATEN = buildShapes();

    protected SliceableFoodBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SLICES_EATEN, 0));
    }

    /** The whole pizza's nutrition/saturation/effects - divided/re-rolled per slice in {@link #eatSlice}. */
    protected abstract Supplier<FoodDefinition> getFoodDefinition();

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SLICES_EATEN);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return eatSlice(level, pos, state, player).consumesAction() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }
        return eatSlice(level, pos, state, player);
    }

    private InteractionResult eatSlice(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
        FoodDefinition definition = this.getFoodDefinition().get();
        boolean canEat = definition.alwaysEdible() || player.getFoodData().needsFood();
        if (!canEat) {
            return InteractionResult.PASS;
        }

        player.getFoodData().eat(definition.nutrition() / TOTAL_SLICES, definition.saturation() / TOTAL_SLICES);
        applyEffects(definition, player);
        level.gameEvent(player, GameEvent.EAT, pos);

        int slicesEaten = state.getValue(SLICES_EATEN);
        if (slicesEaten < TOTAL_SLICES - 1) {
            level.setBlock(pos, state.setValue(SLICES_EATEN, slicesEaten + 1), 3);
        } else {
            level.removeBlock(pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }
        return InteractionResult.SUCCESS;
    }

    private static void applyEffects(FoodDefinition definition, Player player) {
        RandomSource random = player.getRandom();
        for (FoodEffect foodEffect : definition.effects()) {
            if (random.nextFloat() < foodEffect.probability()) {
                // The copy constructor - not the (effect, duration, amplifier) one - because that
                // one silently drops ambient/visible/showIcon back to their (particles-on,
                // icon-on) defaults, undoing whatever the FoodEffect's own MobEffectInstance chose
                // (e.g. Grow/Shrink authored with particles and the HUD icon off - see ModBlocks).
                player.addEffect(new MobEffectInstance(foodEffect.effect().get()));
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_SLICES_EATEN[state.getValue(SLICES_EATEN)];
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    /** Shrinks the box by one slice's width (14/{@link #TOTAL_SLICES} px) per bite, same taper as vanilla cake's fixed 7-shape array. */
    private static VoxelShape[] buildShapes() {
        VoxelShape[] shapes = new VoxelShape[TOTAL_SLICES];
        double sliceWidth = 14.0 / TOTAL_SLICES;
        for (int slicesEaten = 0; slicesEaten < TOTAL_SLICES; slicesEaten++) {
            shapes[slicesEaten] = Block.box(1.0 + slicesEaten * sliceWidth, 0.0, 1.0, 15.0, 8.0, 15.0);
        }
        return shapes;
    }
}
