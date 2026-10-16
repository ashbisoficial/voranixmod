package com.voranix.maipizza.common.cooking;

import com.voranix.maipizza.common.registration.ModBlockEntities;
import com.voranix.voranixmod.common.cooking.system.AbstractCookingBlockEntity;
import com.voranix.voranixmod.common.cooking.system.AbstractCookingManager;
import com.voranix.voranixmod.common.cooking.system.CookingContainer;
import com.voranix.voranixmod.common.cooking.system.CookingProcess;
import com.voranix.voranixmod.common.cooking.system.SimpleCookingProcess;
import com.voranix.voranixmod.common.cooking.system.SlotType;
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
 * Horno de Barro's BlockEntity - a direct port of {@code FreidoraBlockEntity}'s shape onto the same
 * shared cooking system. See that class's javadoc for why so little is left to write here.
 */
public class HornoDeBarroBlockEntity extends AbstractCookingBlockEntity {
    public static final int INPUT_SLOT = HornoDeBarroInventory.SLOTS.indexOf(SlotType.INPUT);
    public static final int OUTPUT_SLOT = HornoDeBarroInventory.SLOTS.indexOf(SlotType.OUTPUT);
    public static final int FUEL_SLOT = HornoDeBarroInventory.SLOTS.indexOf(SlotType.FUEL);

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final HornoDeBarroInventory inventory = new HornoDeBarroInventory(this.itemHandler);
    private final SimpleCookingProcess process = new SimpleCookingProcess();

    private final LazyOptional<IItemHandler> fullItemHandler = trackItemHandler(LazyOptional.of(() -> this.itemHandler));
    private final Map<Direction, LazyOptional<IItemHandler>> directionalItemHandlers =
            buildDirectionalHandlers(this.itemHandler, HornoDeBarroInventory.SLOTS);

    public HornoDeBarroBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HORNO_DE_BARRO.get(), pos, state);
    }

    @Override
    protected LazyOptional<IItemHandler> getItemHandlerCapability(Direction side) {
        return side == null ? this.fullItemHandler : this.directionalItemHandlers.get(side);
    }

    @Override
    public void serverTick(Level level) {
        boolean wasActive = this.process.isActive();
        super.serverTick(level);
        boolean isActive = this.process.isActive();
        if (wasActive != isActive) {
            BlockState state = getBlockState();
            level.setBlock(getBlockPos(), state.setValue(HornoDeBarroBlock.COOKING, isActive), Block.UPDATE_ALL);
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
        return HornoDeBarroManager.INSTANCE;
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
        return Component.translatable("container.maipizza.horno_de_barro");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new HornoDeBarroMenu(containerId, inventory, this, this.level);
    }

    public ItemStackHandler getInventory() {
        return this.itemHandler;
    }

    public ContainerData getData() {
        return this.process;
    }
}
