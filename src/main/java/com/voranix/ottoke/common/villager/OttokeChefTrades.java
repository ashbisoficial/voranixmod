package com.voranix.ottoke.common.villager;

import com.voranix.ottoke.common.registration.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.List;

public class OttokeChefTrades {
    public static void register() {
    }

    public static List<VillagerTrades.ItemListing> getTrades(int level) {
        return switch (level) {
            case 1 -> List.of(
                    new SimpleTrade(new ItemStack(ModItems.BANDERILLA_CRUDA.get()), 12, 1, 16),
                    new SimpleTrade(new ItemStack(Items.BREAD), 1, new ItemStack(ModItems.BANDERILLA_CLASICA.get()), 1, 16, 1, 0.05F)
            );
            case 2 -> List.of(
                    new SimpleTrade(new ItemStack(ModItems.BANDERILLA_CLASICA.get()), 8, 2, 20)
            );
            default -> List.of();
        };
    }

    private static class SimpleTrade implements VillagerTrades.ItemListing {
        private final ItemStack cost;
        private final int costCount;
        private final ItemStack result;
        private final int resultCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        private SimpleTrade(ItemStack cost, int costCount, int maxUses, int villagerXp) {
            this(cost, costCount, ItemStack.EMPTY, 0, maxUses, villagerXp, 0.05F);
        }

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
