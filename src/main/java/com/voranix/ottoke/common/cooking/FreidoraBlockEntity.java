package com.voranix.ottoke.common.cooking;

import com.voranix.ottoke.common.registration.ModBlockEntities;
import com.voranix.ottoke.common.registration.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

public class FreidoraBlockEntity extends BlockEntity implements MenuProvider {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final LazyOptional<IItemHandler> inventoryOptional = LazyOptional.of(() -> this.inventory);
    private final ContainerData data = new SimpleContainerData(2);

    private int cookingProgress;
    private int cookingTotalTime;

    public FreidoraBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FREIDORA.get(), pos, state);
    }

    public void tick() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }

        if (this.hasRecipe()) {
            this.cookingTotalTime = this.getCurrentRecipe().get().getCookingTime();
            this.cookingProgress++;
            this.data.set(0, this.cookingProgress);
            this.data.set(1, this.cookingTotalTime);
            if (this.cookingProgress >= this.cookingTotalTime) {
                this.craftItem();
            }
            setChanged();
        } else {
            this.cookingProgress = 0;
            this.data.set(0, this.cookingProgress);
            this.data.set(1, this.cookingTotalTime);
        }
    }

    private boolean hasRecipe() {
        return this.getCurrentRecipe().isPresent() && this.canCraftOutput();
    }

    private Optional<FreidoraRecipe> getCurrentRecipe() {
        if (this.level == null) {
            return Optional.empty();
        }

        SimpleContainer container = new SimpleContainer(1);
        container.setItem(0, this.inventory.getStackInSlot(INPUT_SLOT));
        RecipeManager recipeManager = this.level.getRecipeManager();
        return recipeManager.getRecipeFor(ModRecipes.FREIDORA_RECIPE_TYPE.get(), container, this.level);
    }

    private boolean canCraftOutput() {
        Optional<FreidoraRecipe> recipe = this.getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack result = recipe.get().getResultItem(this.level != null ? this.level.registryAccess() : null);
        ItemStack outputStack = this.inventory.getStackInSlot(OUTPUT_SLOT);
        if (outputStack.isEmpty()) {
            return true;
        }

        return outputStack.getItem() == result.getItem() && outputStack.getCount() + result.getCount() <= outputStack.getMaxStackSize();
    }

    private void craftItem() {
        Optional<FreidoraRecipe> recipe = this.getCurrentRecipe();
        if (recipe.isEmpty() || !this.canCraftOutput()) {
            return;
        }

        ItemStack result = recipe.get().getResultItem(this.level != null ? this.level.registryAccess() : null).copy();
        ItemStack outputStack = this.inventory.getStackInSlot(OUTPUT_SLOT);
        if (outputStack.isEmpty()) {
            this.inventory.setStackInSlot(OUTPUT_SLOT, result);
        } else if (outputStack.getItem() == result.getItem()) {
            outputStack.grow(result.getCount());
        }

        this.inventory.extractItem(INPUT_SLOT, 1, false);
        this.cookingProgress = 0;
        this.cookingTotalTime = 0;
        this.data.set(0, this.cookingProgress);
        this.data.set(1, this.cookingTotalTime);
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.ottoke.freidora");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new FreidoraMenu(containerId, inventory, this, this.level);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", this.inventory.serializeNBT());
        tag.putInt("CookingProgress", this.cookingProgress);
        tag.putInt("CookingTotalTime", this.cookingTotalTime);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.inventory.deserializeNBT(tag.getCompound("Inventory"));
        this.cookingProgress = tag.getInt("CookingProgress");
        this.cookingTotalTime = tag.getInt("CookingTotalTime");
        this.data.set(0, this.cookingProgress);
        this.data.set(1, this.cookingTotalTime);
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public ContainerData getData() {
        return this.data;
    }

    public int getCookingProgress() {
        return this.cookingProgress;
    }

    public int getCookingTotalTime() {
        return this.cookingTotalTime;
    }
}
