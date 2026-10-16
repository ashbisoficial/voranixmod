package com.voranix.ottoke.common.cooking;

import com.voranix.voranixmod.common.cooking.system.AbstractCookingBlockEntity;
import com.voranix.voranixmod.common.cooking.system.AbstractCookingManager;
import com.voranix.voranixmod.common.cooking.system.CookingContainer;
import com.voranix.voranixmod.common.cooking.system.CookingProcess;
import com.voranix.voranixmod.common.cooking.system.SimpleCookingProcess;
import com.voranix.voranixmod.common.cooking.system.SlotType;
import com.voranix.ottoke.common.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Map;

/**
 * The Freidora's BlockEntity. Everything that used to be hand-rolled here (the tick loop, the
 * cook/craft decision, progress bookkeeping, capability wiring) now comes from the cooking system;
 * this class is left with exactly the things that are genuinely specific to the Freidora: its
 * three-slot item storage, and which physical face maps to which {@link SlotType}.
 * <p>
 * {@code INPUT_SLOT}/{@code OUTPUT_SLOT} are still public static ints - {@link FreidoraMenu} reads
 * them - but their values come from {@link FreidoraInventory#SLOTS}, not a second hardcoded copy.
 * <p>
 * Per-face automation access is not written here at all: {@code DirectionalItemHandler} plus
 * {@code AbstractCookingBlockEntity#buildDirectionalHandlers} already derive the correct visibility
 * and insert/extract rules from {@link FreidoraInventory#SLOTS} - this class only has to build the
 * map once and pick from it.
 */
public class FreidoraBlockEntity extends AbstractCookingBlockEntity {
    public static final int INPUT_SLOT = FreidoraInventory.SLOTS.indexOf(SlotType.INPUT);
    public static final int OUTPUT_SLOT = FreidoraInventory.SLOTS.indexOf(SlotType.OUTPUT);
    public static final int FUEL_SLOT = FreidoraInventory.SLOTS.indexOf(SlotType.FUEL);

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final FreidoraInventory inventory = new FreidoraInventory(this.itemHandler);
    private final SimpleCookingProcess process = new SimpleCookingProcess();

    private final LazyOptional<IItemHandler> fullItemHandler = trackItemHandler(LazyOptional.of(() -> this.itemHandler));
    private final Map<Direction, LazyOptional<IItemHandler>> directionalItemHandlers =
            buildDirectionalHandlers(this.itemHandler, FreidoraInventory.SLOTS);

    public FreidoraBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FREIDORA.get(), pos, state);
    }

    /** {@code side == null} (this station's own menu) gets the full, unrestricted handler. */
    @Override
    protected LazyOptional<IItemHandler> getItemHandlerCapability(Direction side) {
        return side == null ? this.fullItemHandler : this.directionalItemHandlers.get(side);
    }

    /**
     * {@code AbstractCookingManager#tick} (called from {@code super.serverTick}) only knows about
     * {@link CookingContainer}/{@link CookingProcess}/{@link Level} - deliberately not
     * {@link BlockPos}/{@link BlockState}, so a future non-block-entity station isn't forced to have
     * a blockstate at all (see that class's javadoc). Reflecting "is this station actively cooking"
     * into a real blockstate property is therefore a BlockEntity-specific concern, done here by
     * comparing {@link CookingProcess#isActive()} before and after the tick and writing
     * {@link FreidoraBlock#COOKING} only on an actual transition - the same one-block-update-per-
     * state-change a vanilla furnace does, not a write every tick.
     */
    @Override
    public void serverTick(Level level) {
        boolean wasActive = this.process.isActive();
        super.serverTick(level);
        boolean isActive = this.process.isActive();
        if (wasActive != isActive) {
            BlockState state = getBlockState();
            level.setBlock(getBlockPos(), state.setValue(FreidoraBlock.COOKING, isActive), Block.UPDATE_ALL);
        }
    }

    @Override
    public CookingContainer getCookingContainer() {
        return this.inventory;
    }

    @Override
    public CookingProcess getCookingProcess() {
        return this.process;
    }

    @Override
    public AbstractCookingManager getCookingManager() {
        return FreidoraManager.INSTANCE;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", this.itemHandler.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.ottoke.freidora");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new FreidoraMenu(containerId, inventory, this, this.level);
    }

    public ItemStackHandler getInventory() {
        return this.itemHandler;
    }

    public ContainerData getData() {
        return this.process;
    }
}
