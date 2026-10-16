package com.voranix.ottoke.common.cooking;

import com.voranix.voranixmod.common.cooking.system.SlotType;
import com.voranix.ottoke.common.registration.ModBlocks;
import com.voranix.ottoke.common.registration.ModMenuTypes;
import com.voranix.ottoke.common.registration.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Slots are added in visual order (ingredient, fuel, result), which is why this class keeps its own
 * {@code *_MENU_SLOT} constants instead of reusing {@link FreidoraBlockEntity#INPUT_SLOT}/
 * {@code FUEL_SLOT}/{@code OUTPUT_SLOT}: those describe positions in the underlying
 * {@code ItemStackHandler}, not positions in this menu's slot list - the two only coincided by
 * accident back when there were just two slots in the same order as the handler.
 */
public class FreidoraMenu extends AbstractContainerMenu {
    private static final int INGREDIENT_MENU_SLOT = 0;
    private static final int FUEL_MENU_SLOT = 1;
    private static final int CONTAINER_SLOT_COUNT = 3;

    private final BlockPos pos;
    private final Level level;
    private final ContainerData data;

    public FreidoraMenu(int containerId, Inventory playerInventory, BlockPos pos, Level level) {
        this(containerId, playerInventory, (FreidoraBlockEntity) level.getBlockEntity(pos), level);
    }

    public FreidoraMenu(int containerId, Inventory playerInventory, FreidoraBlockEntity blockEntity, Level level) {
        super(ModMenuTypes.FREIDORA.get(), containerId);
        this.pos = blockEntity.getBlockPos();
        this.level = level;
        this.data = blockEntity.getData();

        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), FreidoraBlockEntity.INPUT_SLOT, 56, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return SlotType.INPUT.allowsExternalInsert();
            }
        });
        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), FreidoraBlockEntity.FUEL_SLOT, 56, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return SlotType.FUEL.allowsExternalInsert() && ForgeHooks.getBurnTime(stack, null) > 0;
            }
        });
        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), FreidoraBlockEntity.OUTPUT_SLOT, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return SlotType.OUTPUT.allowsExternalInsert();
            }
        });

        this.addPlayerInventorySlots(playerInventory, 8, 84);
        this.addDataSlots(this.data);
    }

    private void addPlayerInventorySlots(Inventory inventory, int x, int y) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                int slotX = x + column * 18;
                int slotY = y + row * 18;
                this.addSlot(new Slot(inventory, column + row * 9 + 9, slotX, slotY));
            }
        }

        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(inventory, column, x + column * 18, y + 58));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null) {
            return true;
        }
        return stillValid(ContainerLevelAccess.create(this.level, this.pos), player, ModBlocks.FREIDORA.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if (index < CONTAINER_SLOT_COUNT) {
                if (!this.moveItemStackTo(slotStack, CONTAINER_SLOT_COUNT, CONTAINER_SLOT_COUNT + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, INGREDIENT_MENU_SLOT, FUEL_MENU_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemStack;
    }

    public int getCookingProgress() {
        return this.data.get(0);
    }

    public int getCookingTotalTime() {
        return this.data.get(1);
    }

    /**
     * Whether the fuel slot currently holds enough of a genuinely burnable item to pay for whatever
     * recipe the ingredient slot currently matches. Unlike a vanilla furnace, fuel here is charged
     * whole on recipe completion rather than ticked down over time (see
     * {@code FreidoraManager#consumeFuel}) - there is no "burn time remaining" fraction to animate,
     * so the screen's flame icon is an honest on/off read of this, not a fake gradual meter.
     * <p>
     * Checks the matched recipe's own {@code fuelCost}, not just "is this item flammable" - a recipe
     * charging more than 1 (Banderilla Premium does) would otherwise show a lit flame with only one
     * fuel item present, while {@code FreidoraManager} silently refuses to start the cook underneath
     * it: the flame looks ready, nothing happens, and there is no other feedback that more fuel is
     * needed. Reads the fuel slot's item directly rather than through {@link #data}: slot contents
     * already replicate to the client via vanilla's standard container sync, so no extra synced
     * field is needed just for this check.
     */
    public boolean hasFuel() {
        int fuelCost = currentFuelCost();
        if (fuelCost <= 0) {
            return true;
        }
        ItemStack fuelStack = this.slots.get(FUEL_MENU_SLOT).getItem();
        if (ForgeHooks.getBurnTime(fuelStack, null) <= 0) {
            return false;
        }
        return fuelStack.getCount() >= fuelCost;
    }

    /**
     * The fuel cost of whichever {@code voranixmod:freidora} recipe currently matches the ingredient
     * slot, or {@code 1} if none does (no recipe, no ingredient) or the level isn't available yet -
     * the same "at least one genuinely burnable item" threshold {@link #hasFuel()} used before it
     * became recipe-aware.
     */
    private int currentFuelCost() {
        if (this.level == null) {
            return 1;
        }
        ItemStack ingredientStack = this.slots.get(INGREDIENT_MENU_SLOT).getItem();
        for (FreidoraRecipe recipe : this.level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.FREIDORA.get())) {
            if (recipe.getIngredient().test(ingredientStack)) {
                return recipe.getCookingResult().fuelCost();
            }
        }
        return 1;
    }
}
