package com.voranix.ottoke.common.cooking;

import com.voranix.voranixmod.common.cooking.system.CookingContainer;
import com.voranix.voranixmod.common.cooking.system.SlotLayout;
import com.voranix.voranixmod.common.cooking.system.SlotType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * The Freidora's concrete inventory: input, output, and fuel, addressed by {@link SlotType} through
 * a {@link SlotLayout} instead of raw indices scattered across the class.
 * <p>
 * Implements {@link CookingContainer} so {@code FreidoraManager} (via the inherited
 * {@link com.voranix.voranixmod.common.cooking.system.AbstractCookingManager}) can drive it generically,
 * but also exposes the fuel slot directly as its own methods - fuel has no place in
 * {@code CookingContainer}'s generic contract (see {@code AbstractCookingManager#hasFuel}), so the
 * one manager that actually needs it reaches for this concrete type instead.
 * <p>
 * {@link #SLOTS} is public so {@code FreidoraBlockEntity} can build its per-face capability views
 * (see {@code AbstractCookingBlockEntity#getItemHandlerCapability}) from the exact same layout this
 * class uses internally - one source of truth for "which index is which slot", instead of a second
 * copy of the same three numbers living on the block entity.
 */
public class FreidoraInventory implements CookingContainer {
    public static final SlotLayout SLOTS = SlotLayout.builder()
            .slot(SlotType.INPUT, 0)
            .slot(SlotType.OUTPUT, 1)
            .slot(SlotType.FUEL, 2)
            .build();

    private final IItemHandlerModifiable inventory;

    public FreidoraInventory(IItemHandlerModifiable inventory) {
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
