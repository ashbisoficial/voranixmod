package com.voranix.voranixmod.common.item;

/**
 * Implemented by anything a menu-listing enum (Ottoke's {@code FoodCategory}, Mai Pizza's
 * {@code MaiPizzaCategory}) needs to group by category - both plain {@code Item} foods
 * ({@code OttokeFoodItem}/{@code MaiPizzaFoodItem}) and block-placed foods
 * ({@code SliceableFoodBlock}/{@code PizzaBlock}), since a category listing has to look past
 * {@code Item} vs {@code Block} to find the food underneath either an {@code ItemStack}'s
 * {@code Item} directly, or - for a block - its {@code BlockItem}'s {@code getBlock()}.
 */
public interface CategorizedFood<C> {
    C getCategory();
}
