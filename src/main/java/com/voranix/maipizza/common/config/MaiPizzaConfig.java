package com.voranix.maipizza.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

/** Mirrors {@code com.voranix.ottoke.common.config.OttokeConfig}'s shape for the Mai Pizza module. */
public final class MaiPizzaConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue HORNO_DE_BARRO_COOKING_TIME;
    public static final ForgeConfigSpec.IntValue PIZZA_MARGARITA_HUNGER;
    public static final ForgeConfigSpec.DoubleValue PIZZA_MARGARITA_SATURATION;
    public static final ForgeConfigSpec.IntValue CHEF_PRICE_PIZZA_MARGARITA_CRUDA;
    public static final ForgeConfigSpec.IntValue CHEF_PRICE_PIZZA_MARGARITA;
    public static final ForgeConfigSpec.IntValue CHEF_PRICE_PIZZA_PEPPERONI;
    public static final ForgeConfigSpec.IntValue CHEF_PRICE_PIZZA_CHAMPINON;
    public static final ForgeConfigSpec.IntValue CHEF_PRICE_PIZZA_CUATRO_QUESOS;
    public static final ForgeConfigSpec.IntValue CHEF_PRICE_PIZZA_SUPREMA;

    static {
        BUILDER.comment("Mai Pizza configuration").push("maipizza");

        HORNO_DE_BARRO_COOKING_TIME = BUILDER.comment("Time in ticks required to cook a recipe in the horno de barro")
                .defineInRange("hornoDeBarro.cookingTime", 200, 1, Integer.MAX_VALUE);

        PIZZA_MARGARITA_HUNGER = BUILDER.comment("Hunger value for the Pizza Margarita")
                .defineInRange("food.pizzaMargaritaHunger", 8, 0, 20);

        PIZZA_MARGARITA_SATURATION = BUILDER.comment("Saturation value for the Pizza Margarita")
                .defineInRange("food.pizzaMargaritaSaturation", 0.8D, 0.0D, 20.0D);

        // Prices are in coin_estano base units, same convention as OttokeConfig - see that class's
        // comment. Every pizza tier is priced above its Banderilla counterpart (Cruda > Cruda,
        // Margarita > Clasica, etc.) since a pizza feeds more people than a single banderilla.
        CHEF_PRICE_PIZZA_MARGARITA_CRUDA = BUILDER.comment("Price (in coin_estano base units) for the raw pizza margarita trade")
                .defineInRange("chef.pricePizzaMargaritaCruda", 16, 1, 1_000_000);

        CHEF_PRICE_PIZZA_MARGARITA = BUILDER.comment("Price (in coin_estano base units) for the pizza margarita trade")
                .defineInRange("chef.pricePizzaMargarita", 11, 1, 1_000_000);

        CHEF_PRICE_PIZZA_PEPPERONI = BUILDER.comment("Price (in coin_estano base units) for the pizza pepperoni trade")
                .defineInRange("chef.pricePizzaPepperoni", 13, 1, 1_000_000);

        CHEF_PRICE_PIZZA_CHAMPINON = BUILDER.comment("Price (in coin_estano base units) for the pizza champiñón trade")
                .defineInRange("chef.pricePizzaChampinon", 13, 1, 1_000_000);

        CHEF_PRICE_PIZZA_CUATRO_QUESOS = BUILDER.comment("Price (in coin_estano base units) for the pizza cuatro quesos trade")
                .defineInRange("chef.pricePizzaCuatroQuesos", 14, 1, 1_000_000);

        CHEF_PRICE_PIZZA_SUPREMA = BUILDER.comment("Price (in coin_estano base units) for the pizza suprema trade")
                .defineInRange("chef.pricePizzaSuprema", 25, 1, 1_000_000);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    private MaiPizzaConfig() {
    }
}
