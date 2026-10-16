package com.voranix.voranixmod.common.cooking.system;

/**
 * A device that cooks: owns a {@link CookingContainer} to read/write its item slots, a
 * {@link CookingProcess} to track progress, and an {@link AbstractCookingManager} to decide what
 * happens each tick. FreidoraBlockEntity is the first implementer; any future device (a Parrilla, an
 * Olla, a drink machine) implements this same three-method contract instead of hand-rolling its own
 * tick/progress/recipe-lookup logic the way FreidoraBlockEntity used to.
 */
public interface CookingStation {
    CookingContainer getCookingContainer();

    CookingProcess getCookingProcess();

    AbstractCookingManager getCookingManager();
}
