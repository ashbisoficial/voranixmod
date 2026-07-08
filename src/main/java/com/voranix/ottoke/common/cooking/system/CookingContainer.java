package com.voranix.ottoke.common.cooking.system;

import net.minecraft.world.item.ItemStack;

/**
 * The input/output slots a {@link CookingRecipe} reads from and writes to, independent of whatever
 * inventory representation a station actually uses (a Forge ItemStackHandler, a vanilla Container,
 * anything else). {@link CookingRecipe} and {@link AbstractCookingManager} depend only on this -
 * never on a station's concrete inventory type - so a future station can back it with any storage
 * as long as it can answer these four questions.
 * <p>
 * {@code FreidoraInventory} is the first implementation, backed by its own three-slot
 * {@code ItemStackHandler} (input, output, fuel) addressed through a {@link SlotLayout}.
 */
public interface CookingContainer {
    int getInputSlotCount();

    ItemStack getInputItem(int slot);

    ItemStack getOutputItem();

    void setOutputItem(ItemStack stack);
}
