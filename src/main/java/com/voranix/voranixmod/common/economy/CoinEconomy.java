package com.voranix.voranixmod.common.economy;

/**
 * Turns a price expressed in {@link CoinDenomination#ESTANO} base units into "which coin, how
 * many" - so a villager trade for an expensive item (Pizza Suprema, Banderilla Premium) charges a
 * handful of a high-value coin instead of dozens of {@code coin_estano}, the way every trade in
 * this codebase priced things before this class existed.
 */
public final class CoinEconomy {
    private CoinEconomy() {
    }

    /** The highest denomination that still prices the amount at 64 coins or fewer (one stack) - falls back to {@link CoinDenomination#ESTANO} for tiny amounts. */
    public static CoinDenomination bestDenominationFor(int amountInBaseUnits) {
        CoinDenomination best = CoinDenomination.ESTANO;
        for (CoinDenomination denomination : CoinDenomination.values()) {
            int count = amountInBaseUnits / denomination.baseUnits();
            if (count >= 1 && count <= 64) {
                best = denomination;
            }
        }
        return best;
    }

    /** How many of {@code denomination} a price of {@code amountInBaseUnits} costs, rounded up so a trade never undercharges. */
    public static int countIn(int amountInBaseUnits, CoinDenomination denomination) {
        return Math.max(1, (amountInBaseUnits + denomination.baseUnits() - 1) / denomination.baseUnits());
    }
}
