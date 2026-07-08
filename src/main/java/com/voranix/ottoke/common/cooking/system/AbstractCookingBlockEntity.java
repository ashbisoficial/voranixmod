package com.voranix.ottoke.common.cooking.system;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for any block that cooks. Wires the mechanical parts every {@link CookingStation}
 * needs from a BlockEntity - server ticking, NBT persistence, and Forge item-handler capability
 * exposure - onto the collaborators a concrete station supplies, so a future device only has to
 * provide those collaborators plus its own {@link MenuProvider} details and slot layout, instead of
 * re-deriving this boilerplate the way {@code FreidoraBlockEntity} used to.
 * <p>
 * {@link #serverTick(Level)} does not decide anything about cooking itself - it builds a
 * {@link CookingContext} (this station, its position, the level) and delegates to
 * {@link CookingSystem#tick}. A subclass still owns {@code getTicker()} on its {@code Block} and is
 * expected to call this method from it.
 * <p>
 * {@link #saveAdditional}/{@link #load} persist the {@link CookingProcess} generically (progress,
 * total time, under the {@code "CookingProcess"} tag). They know nothing about a station's item
 * storage - a subclass overriding these must call {@code super} first, then persist its own
 * inventory under its own tag.
 * <p>
 * Capability exposure is split the same way: {@link #getCapability} owns the generic dispatch
 * (which capability, null-side vs faced query) once, {@code final}, so every station answers
 * {@code ForgeCapabilities.ITEM_HANDLER} identically. {@link #getItemHandlerCapability(Direction)}
 * is the one thing that genuinely differs per station - which registered view answers for a given
 * face - and {@link #trackItemHandler}/{@link #buildDirectionalHandlers} let a subclass register or
 * build whatever {@link LazyOptional} views it needs so they are invalidated automatically; a
 * subclass never needs its own {@code invalidateCaps()}/{@code setRemoved()} override, and (as long
 * as it is backed by a {@link SlotLayout}) never needs to write its own per-direction
 * {@link DirectionalItemHandler} wiring either.
 */
public abstract class AbstractCookingBlockEntity extends BlockEntity implements CookingStation, MenuProvider {
    private static final String TAG_COOKING_PROCESS = "CookingProcess";

    private final List<LazyOptional<IItemHandler>> itemHandlerCapabilities = new ArrayList<>();

    protected AbstractCookingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void serverTick(Level level) {
        CookingSystem.tick(new CookingContext(level, getBlockPos(), this));
    }

    /**
     * Registers a capability view so it is invalidated automatically on block removal or chunk
     * unload. A subclass calls this once per {@link LazyOptional} it builds (see
     * {@code FreidoraBlockEntity}) instead of writing its own
     * {@code invalidateCaps()}/{@code setRemoved()} override.
     */
    protected final LazyOptional<IItemHandler> trackItemHandler(LazyOptional<IItemHandler> handler) {
        this.itemHandlerCapabilities.add(handler);
        return handler;
    }

    /**
     * Builds one tracked {@link DirectionalItemHandler} view per {@link Direction}, keyed by that
     * direction. Any station whose inventory is described by a {@link SlotLayout} gets a fully
     * correct automation surface from this one call instead of writing its own per-face wiring -
     * see {@code FreidoraBlockEntity} for the two-line override this reduces
     * {@link #getItemHandlerCapability} to.
     */
    protected final Map<Direction, LazyOptional<IItemHandler>> buildDirectionalHandlers(
            IItemHandlerModifiable inventory, SlotLayout layout) {
        Map<Direction, LazyOptional<IItemHandler>> handlers = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) {
            handlers.put(direction, trackItemHandler(
                    LazyOptional.of(() -> DirectionalItemHandler.forSide(inventory, layout, direction))));
        }
        return handlers;
    }

    /**
     * Which registered view answers a {@code ForgeCapabilities.ITEM_HANDLER} query for a given face;
     * {@code side == null} is the internal/unrestricted query (this station's own menu, for
     * instance). The only item-handler-capability decision left to a station: which face can reach
     * which slots.
     */
    protected abstract LazyOptional<IItemHandler> getItemHandlerCapability(Direction side);

    @Override
    public final <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return getItemHandlerCapability(side).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.invalidateItemHandlers();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.invalidateItemHandlers();
    }

    private void invalidateItemHandlers() {
        this.itemHandlerCapabilities.forEach(LazyOptional::invalidate);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(TAG_COOKING_PROCESS, getCookingProcess().save());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        getCookingProcess().load(tag.getCompound(TAG_COOKING_PROCESS));
    }
}
