package com.voranix.maipizza.common.villager;

import com.voranix.maipizza.common.config.MaiPizzaConfig;
import com.voranix.maipizza.common.registration.ModBlockItems;
import com.voranix.maipizza.common.registration.ModItems;
import com.voranix.voranixmod.common.economy.CoinDenomination;
import com.voranix.voranixmod.common.economy.CoinEconomy;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

/**
 * Mirrors {@code com.voranix.ottoke.common.villager.OttokeChefTrades} exactly: full catalog (Cruda
 * through Suprema), all offered from villager level 1 - see that class for why. Cooked pizzas are now
 * {@code PizzaBlock}s, so their trade results come from {@link ModBlockItems}, not {@link ModItems}
 * (which still only has the five raw {@code *_CRUDA} items).
 */
public class MaiPizzaChefTrades {
    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(MaiPizzaChefTrades::addTrades);
    }

    private static void addTrades(VillagerTradesEvent event) {
        if (event.getType() != MaiPizzaChefProfession.PROFESSION.get()) {
            return;
        }
        event.getTrades().get(1).addAll(getTrades());
    }

    public static List<VillagerTrades.ItemListing> getTrades() {
        return List.of(
                coinTrade(MaiPizzaConfig.CHEF_PRICE_PIZZA_MARGARITA_CRUDA.get(), ModItems.PIZZA_MARGARITA_CRUDA, 16, 0),
                new SimpleTrade(new ItemStack(Items.WHEAT), 1, new ItemStack(ModBlockItems.PIZZA_MARGARITA.get()), 1, 16, 1, 0.05F),
                coinTrade(MaiPizzaConfig.CHEF_PRICE_PIZZA_MARGARITA.get(), ModBlockItems.PIZZA_MARGARITA, 20, 2),
                coinTrade(MaiPizzaConfig.CHEF_PRICE_PIZZA_PEPPERONI.get(), ModBlockItems.PIZZA_PEPPERONI, 16, 5),
                coinTrade(MaiPizzaConfig.CHEF_PRICE_PIZZA_CHAMPINON.get(), ModBlockItems.PIZZA_CHAMPINON, 16, 5),
                coinTrade(MaiPizzaConfig.CHEF_PRICE_PIZZA_CUATRO_QUESOS.get(), ModBlockItems.PIZZA_CUATRO_QUESOS, 12, 10),
                coinTrade(MaiPizzaConfig.CHEF_PRICE_PIZZA_SUPREMA.get(), ModBlockItems.PIZZA_SUPREMA, 8, 20)
        );
    }

    private static VillagerTrades.ItemListing coinTrade(int priceInBaseUnits, RegistryObject<Item> result, int maxUses, int villagerXp) {
        CoinDenomination denomination = CoinEconomy.bestDenominationFor(priceInBaseUnits);
        int count = CoinEconomy.countIn(priceInBaseUnits, denomination);
        return new SimpleTrade(new ItemStack(denomination.item()), count, new ItemStack(result.get()), 1, maxUses, villagerXp, 0.05F);
    }

    private static class SimpleTrade implements VillagerTrades.ItemListing {
        private final ItemStack cost;
        private final int costCount;
        private final ItemStack result;
        private final int resultCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        private SimpleTrade(ItemStack cost, int costCount, ItemStack result, int resultCount, int maxUses, int villagerXp, float priceMultiplier) {
            this.cost = cost;
            this.costCount = costCount;
            this.result = result;
            this.resultCount = resultCount;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            ItemStack costStack = this.cost.copy();
            costStack.setCount(this.costCount);
            ItemStack resultStack = this.result.copy();
            if (!resultStack.isEmpty()) {
                resultStack.setCount(this.resultCount);
            }
            return new MerchantOffer(costStack, ItemStack.EMPTY, resultStack, 0, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
}
