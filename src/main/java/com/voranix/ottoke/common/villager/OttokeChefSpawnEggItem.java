package com.voranix.ottoke.common.villager;

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
 * A plain spawn egg only knows how to run {@code EntityType#spawn} - it has no way to run the
 * profession assignment {@link OttokeChefSpawner#spawn} does, which is required because Chef Ottoke
 * never acquires its profession through the vanilla POI job search (see
 * {@link OttokeChefProfession#PROFESSION}). Both interaction paths a spawn egg can trigger -
 * right-clicking a block, and being fired from a dispenser - are overridden here to go through
 * {@link OttokeChefSpawner#spawn} instead, so however this egg is used, the result is always a
 * Chef Ottoke, never a professionless villager. The water-right-click path
 * ({@code SpawnEggItem#use}) is intentionally left at its vanilla default (spawns a plain villager)
 * - it's an edge case vanilla only supports for convenience on swimming mobs, not a path players are
 * expected to use to create a Chef Ottoke.
 * <p>
 * This item renders as a flat custom icon ({@code textures/item/huevo_ottoke.png}, wired in
 * {@code ModItemModelProvider}), not the vanilla tinted egg template - so {@code backgroundColor}/
 * {@code highlightColor} below are never actually drawn; they're kept plausible (sampled from the
 * icon's own palette) only because {@link ForgeSpawnEggItem}'s constructor requires them and other
 * systems (e.g. {@code getColor}) may still query them.
 */
public class OttokeChefSpawnEggItem extends ForgeSpawnEggItem {
    public OttokeChefSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Item.Properties properties) {
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

        OttokeChefSpawner.spawn(serverLevel, spawnPos);
        stack.shrink(1);
        level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, clickedPos);
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    protected DispenseItemBehavior createDispenseBehavior() {
        return (source, stack) -> {
            Direction face = source.getBlockState().getValue(DispenserBlock.FACING);
            OttokeChefSpawner.spawn(source.getLevel(), source.getPos().relative(face));
            stack.shrink(1);
            source.getLevel().gameEvent(GameEvent.ENTITY_PLACE, source.getPos(), GameEvent.Context.of(source.getBlockState()));
            return stack;
        };
    }
}
