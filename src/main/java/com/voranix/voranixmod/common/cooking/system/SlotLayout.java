package com.voranix.voranixmod.common.cooking.system;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maps each {@link SlotType} a station uses to the numeric index(es) it actually occupies in that
 * station's underlying inventory. This is the piece that actually replaces magic numbers: code that
 * today writes {@code inventory.getStackInSlot(0)} instead writes
 * {@code inventory.getStackInSlot(layout.indexOf(SlotType.INPUT))} - the literal {@code 0} exists in
 * exactly one place (wherever that station builds its layout), not repeated in every method that
 * happens to touch that slot.
 * <p>
 * A station with no {@code FUEL} slot (a future station without one) simply never adds one to its
 * layout; {@link #indexOf}/{@link #indicesOf} then throw for that type rather than silently
 * returning a wrong slot - asking a station for a role it doesn't have is a bug to surface, not
 * paper over with a default. A station with several slots of the same role (e.g. a future Olla with
 * multiple ingredient inputs) registers {@code INPUT} more than once; {@link #indicesOf} returns all
 * of them.
 */
public final class SlotLayout {
    private final Map<SlotType, List<Integer>> indicesByType;
    private final Map<Integer, SlotType> typeByIndex;

    private SlotLayout(Map<SlotType, List<Integer>> indicesByType, Map<Integer, SlotType> typeByIndex) {
        this.indicesByType = indicesByType;
        this.typeByIndex = typeByIndex;
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Convenience for a role a station only has one slot of (e.g. OUTPUT). */
    public int indexOf(SlotType type) {
        return indicesOf(type).get(0);
    }

    public List<Integer> indicesOf(SlotType type) {
        List<Integer> indices = this.indicesByType.get(type);
        if (indices == null || indices.isEmpty()) {
            throw new IllegalArgumentException("This station has no slot of type " + type);
        }
        return indices;
    }

    /**
     * Like {@link #indicesOf}, but for a role a station might genuinely not have (only {@code FUEL}
     * is optional today - every {@link CookingContainer} implies at least one {@code INPUT} and
     * exactly one {@code OUTPUT}). Returns an empty list instead of throwing, for code generic enough
     * to not know in advance whether the role exists - see {@link DirectionalItemHandler}.
     */
    public List<Integer> indicesOfOrEmpty(SlotType type) {
        return this.indicesByType.getOrDefault(type, List.of());
    }

    /**
     * The role of whichever slot lives at {@code index}. Every index handed out by this layout
     * resolves. A plain map lookup, not a scan - {@link DirectionalItemHandler} calls this on every
     * automated insert/extract/isItemValid check, so it needs to stay O(1) regardless of how many
     * slots a station ends up with.
     */
    public SlotType typeOf(int index) {
        SlotType type = this.typeByIndex.get(index);
        if (type == null) {
            throw new IllegalArgumentException("No slot type registered for index " + index);
        }
        return type;
    }

    public static final class Builder {
        private final Map<SlotType, List<Integer>> indicesByType = new EnumMap<>(SlotType.class);

        public Builder slot(SlotType type, int index) {
            this.indicesByType.computeIfAbsent(type, key -> new ArrayList<>()).add(index);
            return this;
        }

        public SlotLayout build() {
            Map<SlotType, List<Integer>> indices = new EnumMap<>(SlotType.class);
            Map<Integer, SlotType> types = new HashMap<>();
            this.indicesByType.forEach((type, slots) -> {
                indices.put(type, List.copyOf(slots));
                slots.forEach(slot -> types.put(slot, type));
            });
            return new SlotLayout(Map.copyOf(indices), Map.copyOf(types));
        }
    }
}
