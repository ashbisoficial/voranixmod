package com.voranix.ottoke.common.config;

import com.voranix.voranixmod.VoranixMod;
import net.minecraftforge.common.ForgeConfigSpec;

public final class OttokeConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue FREIDORA_COOKING_TIME;
    public static final ForgeConfigSpec.IntValue BANDERILLA_CLASICA_HUNGER;
    public static final ForgeConfigSpec.DoubleValue BANDERILLA_CLASICA_SATURATION;
    public static final ForgeConfigSpec.IntValue CHEF_PRICE_BANDERILLA_CRUDA;
    public static final ForgeConfigSpec.IntValue CHEF_PRICE_BANDERILLA_CLASICA;
    public static final ForgeConfigSpec.IntValue CHEF_PRICE_BANDERILLA_PAPA;
    public static final ForgeConfigSpec.IntValue CHEF_PRICE_BANDERILLA_PICANTE;
    public static final ForgeConfigSpec.IntValue CHEF_PRICE_BANDERILLA_MOZZARELLA;
    public static final ForgeConfigSpec.IntValue CHEF_PRICE_BANDERILLA_PREMIUM;
    public static final ForgeConfigSpec.DoubleValue FUTURE_EVENT_PROBABILITY;

    static {
        BUILDER.comment("Ottoke Street Food configuration").push(VoranixMod.MODID);

        FREIDORA_COOKING_TIME = BUILDER.comment("Time in ticks required to cook a recipe in the freidora")
                .defineInRange("freidora.cookingTime", 200, 1, Integer.MAX_VALUE);

        BANDERILLA_CLASICA_HUNGER = BUILDER.comment("Hunger value for the Classic Banderilla")
                .defineInRange("food.banderillaClasicaHunger", 8, 0, 20);

        BANDERILLA_CLASICA_SATURATION = BUILDER.comment("Saturation value for the Classic Banderilla")
                .defineInRange("food.banderillaClasicaSaturation", 0.8D, 0.0D, 20.0D);

        // Prices are all in coin_estano base units (see CoinDenomination/CoinEconomy) - the chef
        // trade converts to the best-fitting coin denomination automatically, so a tier's number
        // here only needs to say "how much more than the last tier", not which literal coin.
        CHEF_PRICE_BANDERILLA_CRUDA = BUILDER.comment("Price (in coin_estano base units) for the raw banderilla trade")
                .defineInRange("chef.priceBanderillaCruda", 12, 1, 1_000_000);

        CHEF_PRICE_BANDERILLA_CLASICA = BUILDER.comment("Price (in coin_estano base units) for the classic banderilla trade")
                .defineInRange("chef.priceBanderillaClasica", 8, 1, 1_000_000);

        CHEF_PRICE_BANDERILLA_PAPA = BUILDER.comment("Price (in coin_estano base units) for the potato banderilla trade")
                .defineInRange("chef.priceBanderillaPapa", 10, 1, 1_000_000);

        CHEF_PRICE_BANDERILLA_PICANTE = BUILDER.comment("Price (in coin_estano base units) for the spicy banderilla trade")
                .defineInRange("chef.priceBanderillaPicante", 10, 1, 1_000_000);

        CHEF_PRICE_BANDERILLA_MOZZARELLA = BUILDER.comment("Price (in coin_estano base units) for the mozzarella banderilla trade")
                .defineInRange("chef.priceBanderillaMozzarella", 11, 1, 1_000_000);

        CHEF_PRICE_BANDERILLA_PREMIUM = BUILDER.comment("Price (in coin_estano base units) for the premium banderilla trade")
                .defineInRange("chef.priceBanderillaPremium", 18, 1, 1_000_000);

        FUTURE_EVENT_PROBABILITY = BUILDER.comment("Probability for future server events")
                .defineInRange("events.futureProbability", 0.25D, 0.0D, 1.0D);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    private OttokeConfig() {
    }
}
