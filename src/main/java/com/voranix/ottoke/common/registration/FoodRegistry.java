package com.voranix.ottoke.common.registration;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public final class FoodRegistry {
    private FoodRegistry() {
    }

    public static Item.Properties nonEdible() {
        return new Item.Properties();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final FoodProperties.Builder foodBuilder = new FoodProperties.Builder();

        public Builder nutrition(int nutrition) {
            foodBuilder.nutrition(nutrition);
            return this;
        }

        public Builder saturation(float saturation) {
            foodBuilder.saturationMod(saturation);
            return this;
        }

        public Item.Properties build() {
            return new Item.Properties().food(foodBuilder.build());
        }
    }
}
