package com.voranix.ottoke.common.cooking.system;

/**
 * The role a physical inventory slot plays in a cooking station, independent of which numeric index
 * that slot happens to live at in any particular station's inventory. A raw {@code int} like
 * {@code 0} or {@code 1} carries no meaning on its own and means something different in every
 * station; {@code SlotType.INPUT} means the same thing everywhere.
 * <p>
 * Add a constant here when a station introduces a genuinely new role - do not add a station-specific
 * variant of an existing one (e.g. no {@code SECONDARY_INPUT}); a station with more than one input
 * slot just occupies multiple indices under the same {@code INPUT} type - see {@link SlotLayout}.
 * <p>
 * {@link #allowsExternalInsert()}/{@link #allowsExternalExtract()} are the universal, direction-
 * independent half of the mod's automation rules (see {@link DirectionalItemHandler} for the other
 * half, which slots are visible per face): a raw ingredient or fuel item can never be pulled back
 * out by a hopper/pipe once inserted, and the finished result can never be pushed back in. These are
 * properties of the role itself, not of any one station, so a new constant must decide both before
 * it can be used safely.
 */
public enum SlotType {
    INPUT,
    OUTPUT,
    FUEL;

    public boolean allowsExternalInsert() {
        return this != OUTPUT;
    }

    public boolean allowsExternalExtract() {
        return this == OUTPUT;
    }
}
