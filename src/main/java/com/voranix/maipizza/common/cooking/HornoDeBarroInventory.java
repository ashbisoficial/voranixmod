package com.voranix.maipizza.common.cooking;

import com.voranix.voranixmod.common.cooking.system.CookingContainer;
import com.voranix.voranixmod.common.cooking.system.SlotLayout;
import com.voranix.voranixmod.common.cooking.system.SlotType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * Horno de Barro's concrete inventory - input, output, fuel, exactly like {@code FreidoraInventory}
 * (see that class for why fuel is exposed here rather than through {@link CookingContainer}'s
 * generic contract).
 */
public class HornoDeBarroInventory implements CookingContainer {
    public static final SlotLayout SLOTS = SlotLayout.builder()
            .slot(SlotType.INPUT, 0)
            .slot(SlotType.OUTPUT, 1)
            .slot(SlotType.FUEL, 2)
            .build();

    private final IItemHandlerModifiable inventory;

    public HornoDeBarroInventory(IItemHandlerModifiable inventory) {
        this.inventory = inventory;
    }

    @Override
    public int getInputSlotCount() {
        return 1;
    }

    @Override
    public ItemStack getInputItem(int slot) {
        return this.inventory.getStackInSlot(SLOTS.indexOf(SlotType.INPUT));
    }

    @Override
    public ItemStack getOutputItem() {
        return this.inventory.getStackInSlot(SLOTS.indexOf(SlotType.OUTPUT));
    }

    @Override
    public void setOutputItem(ItemStack stack) {
        this.inventory.setStackInSlot(SLOTS.indexOf(SlotType.OUTPUT), stack);
    }

    public ItemStack getFuelItem() {
        return this.inventory.getStackInSlot(SLOTS.indexOf(SlotType.FUEL));
    }

    public void extractInput(int amount) {
        this.inventory.extractItem(SLOTS.indexOf(SlotType.INPUT), amount, false);
    }

    public void extractFuel(int amount) {
        this.inventory.extractItem(SLOTS.indexOf(SlotType.FUEL), amount, false);
    }
}
