package com.voranix.voranixmod.common.item;

/**
 * Classifies a {@link IngredientItem} by its role in the crafting chain, independent of its name.
 * Future code that needs to group or filter ingredients (creative tab sections, a JEI-style
 * category view, recipe validation) reads this instead of pattern-matching on registry names.
 * <p>
 * Add a new constant when an ingredient doesn't fit an existing role - do not stretch an existing
 * category to cover an unrelated ingredient just to avoid adding one.
 */
public enum IngredientCategory {
    /** Raw pantry staples used directly in recipes (Harina, Sal, Azucar). */
    BASE,
    /** Dairy products (Leche). */
    DAIRY,
    /** Fats and oils used for frying or binding (Aceite). */
    FAT,
    /** Raw protein fillings (Salchicha). */
    PROTEIN,
    /** Non-food structural components (Palo). */
    STRUCTURAL,
    /** Goods crafted from other ingredients rather than gathered directly (Masa, Empanizado). */
    PROCESSED
}
