package com.voranix.voranixcoins.common.registration;

import com.voranix.voranixmod.VoranixMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * The six coin denominations, lowest to highest value - order matters here: it is reused as-is for
 * {@link ModCreativeTabs}' display order, and is the order Cameloth's original standalone
 * {@code Voranix Coins} mod (now folded into this module - see {@code VoranixCoinsModule}) shipped
 * them in. Rarity climbs with value purely for the item-name color players already associate with
 * "more valuable" from vanilla (uncommon/rare/epic); it has no gameplay effect of its own.
 * <p>
 * No conversion rate between denominations exists yet - the original mod never defined one, and
 * nothing here invents one. A module that actually spends these (see
 * {@code com.voranix.ottoke.common.villager.OttokeChefTrades}) currently prices in a single
 * denomination for that reason.
 */
public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, VoranixMod.MODID);

    public static final RegistryObject<Item> COIN_ESTANO = registerCoin("coin_estano", Rarity.COMMON);
    public static final RegistryObject<Item> COIN_SILVER = registerCoin("coin_silver", Rarity.UNCOMMON);
    public static final RegistryObject<Item> COIN_GOLD = registerCoin("coin_gold", Rarity.RARE);
    public static final RegistryObject<Item> COIN_MITHRIL = registerCoin("coin_mithril", Rarity.RARE);
    public static final RegistryObject<Item> COIN_ORICALCO = registerCoin("coin_oricalco", Rarity.EPIC);
    public static final RegistryObject<Item> COIN_AJNA = registerCoin("coin_ajna", Rarity.EPIC);

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static RegistryObject<Item> registerCoin(String name, Rarity rarity) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().rarity(rarity)));
    }
}
