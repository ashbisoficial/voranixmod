package com.voranix.maipizza.common.cooking;

import com.voranix.maipizza.common.registration.ModBlocks;
import com.voranix.maipizza.common.registration.ModMenuTypes;
import com.voranix.maipizza.common.registration.ModRecipeTypes;
import com.voranix.voranixmod.common.cooking.system.SlotType;
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
 * Horno de Barro's menu - a direct port of {@code FreidoraMenu}'s shape (slots, quick-move, fuel
 * readout).
 */
public class HornoDeBarroMenu extends AbstractContainerMenu {
    private static final int INGREDIENT_MENU_SLOT = 0;
    private static final int FUEL_MENU_SLOT = 1;
    private static final int CONTAINER_SLOT_COUNT = 3;

    private final BlockPos pos;
    private final Level level;
    private final ContainerData data;

    public HornoDeBarroMenu(int containerId, Inventory playerInventory, BlockPos pos, Level level) {
        this(containerId, playerInventory, (HornoDeBarroBlockEntity) level.getBlockEntity(pos), level);
    }

    public HornoDeBarroMenu(int containerId, Inventory playerInventory, HornoDeBarroBlockEntity blockEntity, Level level) {
        super(ModMenuTypes.HORNO_DE_BARRO.get(), containerId);
        this.pos = blockEntity.getBlockPos();
        this.level = level;
        this.data = blockEntity.getData();

        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), HornoDeBarroBlockEntity.INPUT_SLOT, 56, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return SlotType.INPUT.allowsExternalInsert();
            }
        });
        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), HornoDeBarroBlockEntity.FUEL_SLOT, 56, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return SlotType.FUEL.allowsExternalInsert() && ForgeHooks.getBurnTime(stack, null) > 0;
            }
        });
        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), HornoDeBarroBlockEntity.OUTPUT_SLOT, 116, 35) {
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
        return stillValid(ContainerLevelAccess.create(this.level, this.pos), player, ModBlocks.HORNO_DE_BARRO.get());
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

    private int currentFuelCost() {
        if (this.level == null) {
            return 1;
        }
        ItemStack ingredientStack = this.slots.get(INGREDIENT_MENU_SLOT).getItem();
        for (HornoDeBarroRecipe recipe : this.level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.HORNO_DE_BARRO.get())) {
            if (recipe.getIngredient().test(ingredientStack)) {
                return recipe.getCookingResult().fuelCost();
            }
        }
        return 1;
    }
}
