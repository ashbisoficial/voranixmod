package com.voranix.voranixmod.common.economy;

import com.voranix.voranixcoins.common.registration.ModItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

/**
 * The conversion rate between {@code voranixcoins}' six denominations - none existed before this
 * (see the "no conversion rate... nothing here invents one" comment on
 * {@code voranixcoins.common.registration.ModItems}, written before this class). Each tier is worth
 * 10x the one below it, mirroring the decimal jumps of the Chilean peso (10, 100, 1.000, 10.000,
 * 100.000, 1.000.000 - {@link #realWorldPesos()} carries that flavor into tooltips/lore) and
 * matching the "moneda A equivale a 10 monedas B" rate the user asked for directly.
 * <p>
 * {@link #coin} is a {@link RegistryObject}, resolved lazily via {@link #item()} for the same
 * reason {@code OttokeChefTrades.CHEF_TRADE_CURRENCY} is: this enum's constants are built before
 * Forge's {@code RegisterEvent} populates {@code voranixcoins.ModItems}.
 */
public enum CoinDenomination {
    ESTANO(ModItems.COIN_ESTANO, 1, 10),
    SILVER(ModItems.COIN_SILVER, 10, 100),
    GOLD(ModItems.COIN_GOLD, 100, 1_000),
    MITHRIL(ModItems.COIN_MITHRIL, 1_000, 10_000),
    ORICALCO(ModItems.COIN_ORICALCO, 10_000, 100_000),
    AJNA(ModItems.COIN_AJNA, 100_000, 1_000_000);

    private final RegistryObject<Item> coin;
    private final int baseUnits;
    private final int realWorldPesos;

    CoinDenomination(RegistryObject<Item> coin, int baseUnits, int realWorldPesos) {
        this.coin = coin;
        this.baseUnits = baseUnits;
        this.realWorldPesos = realWorldPesos;
    }

    public Item item() {
        return this.coin.get();
    }

    /** How many {@link #ESTANO} (the base denomination) one of this coin is worth. */
    public int baseUnits() {
        return this.baseUnits;
    }

    /** Lore-only Chilean-peso equivalent (CLP) for tooltips - not used in any price math. */
    public int realWorldPesos() {
        return this.realWorldPesos;
    }
}
