package com.voranix.ottoke.common.item;

import net.minecraft.world.item.Item;

/**
 * An Item that carries its {@link IngredientCategory} as data instead of the category being
 * inferred elsewhere from the item's registry name. Keeps ModIngredients a flat, uniform list -
 * every entry is "name + category", so adding an ingredient for a future recipe never requires a
 * new Item subclass.
 */
public class IngredientItem extends Item {
    private final IngredientCategory category;

    public IngredientItem(IngredientCategory category, Properties properties) {
        super(properties);
        this.category = category;
    }

    public IngredientCategory getCategory() {
        return this.category;
    }
}
