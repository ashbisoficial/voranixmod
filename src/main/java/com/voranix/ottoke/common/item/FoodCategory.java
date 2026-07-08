package com.voranix.ottoke.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.List;

/**
 * What kind of finished, consumable good an {@link OttokeFoodItem} is - not to be confused with
 * {@link IngredientCategory}, which classifies crafting components, not things a player eats.
 * <p>
 * {@link #getItems()} scans the item registry instead of reading a manually-maintained list, so a
 * menu board (see {@code MenuInfoScreen}) never needs a code change when a new Banderilla or Bebida
 * is registered elsewhere - passing the right category to an {@link OttokeFoodItem}'s constructor
 * at registration time is the only step required for it to show up.
 */
public enum FoodCategory {
    BANDERILLA("category.ottoke.banderillas"),
    BEBIDA("category.ottoke.bebidas");

    private final String translationKey;

    FoodCategory(String translationKey) {
        this.translationKey = translationKey;
    }

    public Component getDisplayName() {
        return Component.translatable(this.translationKey);
    }

    /** Every registered item tagged with this category, in a stable (registry-name) order. */
    public List<Item> getItems() {
        return ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item instanceof OttokeFoodItem food && food.getCategory() == this)
                .sorted(Comparator.comparing(item -> ForgeRegistries.ITEMS.getKey(item).toString()))
                .toList();
    }
}
