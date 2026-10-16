package com.voranix.maipizza.common.item;

import com.voranix.voranixmod.common.item.CategorizedFood;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.List;
import net.minecraft.world.item.Item;

/**
 * Mirrors {@code com.voranix.ottoke.common.item.FoodCategory} - only one constant today (Mai Pizza
 * sells pizza, not also drinks the way Ottoke does), kept as an enum rather than a single constant
 * so a future category (drinks, desserts) is "add one more constant", not a restructure.
 */
public enum MaiPizzaCategory {
    PIZZA("category.maipizza.pizzas");

    private final String translationKey;

    MaiPizzaCategory(String translationKey) {
        this.translationKey = translationKey;
    }

    public Component getDisplayName() {
        return Component.translatable(this.translationKey);
    }

    public List<Item> getItems() {
        return ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> categoryOf(item) == this)
                .sorted(Comparator.comparing(item -> ForgeRegistries.ITEMS.getKey(item).toString()))
                .toList();
    }

    /**
     * Looks past {@code Item} vs {@code Block} for a {@link CategorizedFood}: a raw pizza-cruda
     * {@code Item} implements it directly, while a baked, placeable pizza only exposes it on the
     * {@code Block} its {@code BlockItem} wraps - see {@link CategorizedFood} for why this
     * indirection exists.
     */
    private static MaiPizzaCategory categoryOf(Item item) {
        if (item instanceof CategorizedFood<?> categorized) {
            return asMaiPizzaCategory(categorized);
        }
        if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof CategorizedFood<?> categorized) {
            return asMaiPizzaCategory(categorized);
        }
        return null;
    }

    private static MaiPizzaCategory asMaiPizzaCategory(CategorizedFood<?> categorized) {
        return categorized.getCategory() instanceof MaiPizzaCategory category ? category : null;
    }
}
