package com.voranix.maipizza.common.villager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Mirrors {@code com.voranix.ottoke.common.villager.OttokeChefSpawnEggItem} exactly - see that class
 * for why both interaction paths (block right-click, dispenser) route through
 * {@link MaiPizzaChefSpawner#spawn} instead of a plain {@code EntityType#spawn}.
 */
public class MaiPizzaChefSpawnEggItem extends ForgeSpawnEggItem {
    public MaiPizzaChefSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Item.Properties properties) {
        super(type, backgroundColor, highlightColor, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockState blockState = level.getBlockState(clickedPos);
        BlockPos spawnPos = blockState.getCollisionShape(level, clickedPos).isEmpty() ? clickedPos : clickedPos.relative(direction);

        MaiPizzaChefSpawner.spawn(serverLevel, spawnPos);
        stack.shrink(1);
        level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, clickedPos);
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    protected DispenseItemBehavior createDispenseBehavior() {
        return (source, stack) -> {
            Direction face = source.getBlockState().getValue(DispenserBlock.FACING);
            MaiPizzaChefSpawner.spawn(source.getLevel(), source.getPos().relative(face));
            stack.shrink(1);
            source.getLevel().gameEvent(GameEvent.ENTITY_PLACE, source.getPos(), GameEvent.Context.of(source.getBlockState()));
            return stack;
        };
    }
}
