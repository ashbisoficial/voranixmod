package com.voranix.voranixmod.common.item;

import net.minecraft.world.food.FoodProperties;

import java.util.List;

/**
 * Everything about how a food behaves when eaten: hunger restored, saturation restored, and the
 * status effects it applies. This is the one place FoodProperties gets built - every food item goes
 * through {@link #toFoodProperties()} instead of constructing its own FoodProperties.Builder.
 * <p>
 * Rarity is deliberately not here: it is a construction-time attribute of the Item itself
 * (Item.Properties#rarity), not an eating-time behavior, so it is set the same way vanilla sets it
 * on any item - via Item.Properties when the food's RegistryObject is registered. Duplicating it
 * into this record would just be a second, redundant home for the same value.
 */
public record FoodDefinition(int nutrition, float saturation, List<FoodEffect> effects, boolean alwaysEdible) {

    public FoodDefinition(int nutrition, float saturation) {
        this(nutrition, saturation, List.of(), false);
    }

    public FoodDefinition(int nutrition, float saturation, List<FoodEffect> effects) {
        this(nutrition, saturation, effects, false);
    }

    public FoodProperties toFoodProperties() {
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(this.nutrition)
                .saturationMod(this.saturation);

        for (FoodEffect foodEffect : this.effects) {
            builder.effect(foodEffect.effect(), foodEffect.probability());
        }

        if (this.alwaysEdible) {
            builder.alwaysEat();
        }

        return builder.build();
    }
}
