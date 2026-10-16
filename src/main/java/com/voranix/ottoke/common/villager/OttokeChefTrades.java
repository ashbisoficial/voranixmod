package com.voranix.ottoke.common.villager;

import com.voranix.ottoke.common.config.OttokeConfig;
import com.voranix.ottoke.common.registration.ModItems;
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
 * Chef Ottoke's full catalog: the whole Banderilla ladder (Cruda through Premium), all offered from
 * villager level 1 - the user's explicit call, over the level-gated alternative, so every product is
 * purchasable from the very first trade instead of requiring the villager to level up first. This
 * villager only ever sells Ottoke's own items, never Mai Pizza's (see {@code MaiPizzaChefTrades} for
 * its mirror). Prices are still config-driven per tier (see {@link OttokeConfig}), but now resolve to
 * whichever {@link CoinDenomination} fits best via {@link CoinEconomy} instead of always charging in
 * {@code coin_estano} - {@link #coinTrade} is the one place that conversion happens.
 */
public class OttokeChefTrades {
    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(OttokeChefTrades::addTrades);
    }

    private static void addTrades(VillagerTradesEvent event) {
        if (event.getType() != OttokeChefProfession.PROFESSION.get()) {
            return;
        }
        event.getTrades().get(1).addAll(getTrades());
    }

    public static List<VillagerTrades.ItemListing> getTrades() {
        return List.of(
                coinTrade(OttokeConfig.CHEF_PRICE_BANDERILLA_CRUDA.get(), ModItems.BANDERILLA_CRUDA, 16, 0),
                new SimpleTrade(new ItemStack(Items.BREAD), 1, new ItemStack(ModItems.BANDERILLA_CLASICA.get()), 1, 16, 1, 0.05F),
                coinTrade(OttokeConfig.CHEF_PRICE_BANDERILLA_CLASICA.get(), ModItems.BANDERILLA_CLASICA, 20, 2),
                coinTrade(OttokeConfig.CHEF_PRICE_BANDERILLA_PAPA.get(), ModItems.BANDERILLA_PAPA, 16, 5),
                coinTrade(OttokeConfig.CHEF_PRICE_BANDERILLA_PICANTE.get(), ModItems.BANDERILLA_PICANTE, 16, 5),
                coinTrade(OttokeConfig.CHEF_PRICE_BANDERILLA_MOZZARELLA.get(), ModItems.BANDERILLA_MOZZARELLA, 12, 10),
                coinTrade(OttokeConfig.CHEF_PRICE_BANDERILLA_PREMIUM.get(), ModItems.BANDERILLA_PREMIUM, 8, 20)
        );
    }

    /** Converts a price in {@code coin_estano} base units into "N of the best-fitting coin" via {@link CoinEconomy}. */
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
