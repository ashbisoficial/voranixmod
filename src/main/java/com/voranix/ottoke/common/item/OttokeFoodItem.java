package com.voranix.ottoke.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

/**
 * The one Item subclass every edible item in the mod uses. Takes its {@link FoodDefinition} lazily
 * (a Supplier, not a value) because registration runs before Forge guarantees config is loaded -
 * a food whose hunger/saturation come from OttokeConfig can't resolve those values yet at the point
 * where its RegistryObject supplier runs. Evaluating lazily, inside getFoodProperties(), defers that
 * read until the item is actually used in a loaded game, when config is guaranteed available.
 * <p>
 * A food with fixed, non-config values can use the {@link #OttokeFoodItem(FoodCategory, FoodDefinition, Item.Properties)}
 * convenience constructor below instead of writing its own trivial supplier.
 * <p>
 * {@link #category} exists so generic UI (see {@link FoodCategory#getItems()}, used by the Menu
 * board screen) can list "every Banderilla" or "every Bebida" without maintaining a separate index -
 * it is intentionally a required constructor argument, not optional, so no food can be registered
 * without being classified.
 */
public class OttokeFoodItem extends Item {
    private final FoodCategory category;
    private final Supplier<FoodDefinition> definition;

    public OttokeFoodItem(FoodCategory category, Supplier<FoodDefinition> definition, Item.Properties properties) {
        super(properties);
        this.category = category;
        this.definition = definition;
    }

    public OttokeFoodItem(FoodCategory category, FoodDefinition definition, Item.Properties properties) {
        this(category, () -> definition, properties);
    }

    public FoodCategory getCategory() {
        return this.category;
    }

    @Override
    public boolean isEdible() {
        return true;
    }

    @Override
    public FoodProperties getFoodProperties(ItemStack stack, LivingEntity entity) {
        return this.definition.get().toFoodProperties();
    }
}
