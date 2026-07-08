package com.voranix.ottoke.common.cooking.system;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Direction}-aware, role-aware view over a station's inventory, built entirely from its
 * {@link SlotLayout} - never from a hardcoded slot index, and never from a specific station's class.
 * Any station backed by a {@link SlotLayout} gets a fully correct, safe automation surface just by
 * calling {@link #forSide}; a Parrilla, an Olla, or a drink machine reuses this exactly as-is.
 * <p>
 * Two independent rules combine to decide what a caller on a given face can do, and neither one
 * alone is enough:
 * <ol>
 *     <li><b>Which slots are visible</b> depends on the face: {@code UP} sees only {@code INPUT}
 *     slots, {@code DOWN} sees only the {@code OUTPUT} slot, every other face sees {@code INPUT} and
 *     {@code FUEL} slots. A station with no {@code FUEL} role simply exposes fewer slots on its
 *     lateral faces - see {@link SlotLayout#indicesOfOrEmpty}.</li>
 *     <li><b>Which operations a visible slot allows</b> depends only on its {@link SlotType}, never
 *     on the face: {@link SlotType#allowsExternalInsert()}/{@link SlotType#allowsExternalExtract()}
 *     are absolute. This is what visibility alone cannot guarantee - {@code DOWN} making
 *     {@code OUTPUT} visible would let something insert into it too, if operations weren't checked
 *     independently of which slots are merely reachable.</li>
 * </ol>
 * Both checks happen on every {@link #insertItem}/{@link #extractItem}/{@link #isItemValid} call, so
 * a raw ingredient or fuel item can never be extracted through any face, and the result can never be
 * inserted through any face, regardless of which slots happen to be visible there.
 */
public class DirectionalItemHandler implements IItemHandler {
    private final IItemHandlerModifiable inventory;
    private final SlotLayout layout;
    private final List<Integer> visibleSlots;

    private DirectionalItemHandler(IItemHandlerModifiable inventory, SlotLayout layout, List<Integer> visibleSlots) {
        this.inventory = inventory;
        this.layout = layout;
        this.visibleSlots = visibleSlots;
    }

    /**
     * Builds the view for a given face. {@code UP} is ingredients only, {@code DOWN} is the result
     * only, every other face (the four horizontal directions) is ingredients and fuel. There is no
     * {@code side == null} case here - an internal/unrestricted query is a different concern (see
     * {@code AbstractCookingBlockEntity}) and does not need role-based restriction at all.
     */
    public static DirectionalItemHandler forSide(IItemHandlerModifiable inventory, SlotLayout layout, Direction side) {
        List<Integer> visibleSlots = switch (side) {
            case UP -> layout.indicesOf(SlotType.INPUT);
            case DOWN -> layout.indicesOf(SlotType.OUTPUT);
            default -> combine(layout.indicesOf(SlotType.INPUT), layout.indicesOfOrEmpty(SlotType.FUEL));
        };
        return new DirectionalItemHandler(inventory, layout, visibleSlots);
    }

    private static List<Integer> combine(List<Integer> first, List<Integer> second) {
        List<Integer> combined = new ArrayList<>(first);
        combined.addAll(second);
        return combined;
    }

    @Override
    public int getSlots() {
        return this.visibleSlots.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inventory.getStackInSlot(toGlobalSlot(slot));
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        int globalSlot = toGlobalSlot(slot);
        if (!this.layout.typeOf(globalSlot).allowsExternalInsert()) {
            return stack;
        }
        return this.inventory.insertItem(globalSlot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        int globalSlot = toGlobalSlot(slot);
        if (!this.layout.typeOf(globalSlot).allowsExternalExtract()) {
            return ItemStack.EMPTY;
        }
        return this.inventory.extractItem(globalSlot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.inventory.getSlotLimit(toGlobalSlot(slot));
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        int globalSlot = toGlobalSlot(slot);
        return this.layout.typeOf(globalSlot).allowsExternalInsert() && this.inventory.isItemValid(globalSlot, stack);
    }

    private int toGlobalSlot(int localSlot) {
        return this.visibleSlots.get(localSlot);
    }
}
