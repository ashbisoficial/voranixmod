package com.voranix.maipizza.common.registration;

import com.voranix.maipizza.common.villager.MaiPizzaChefSpawnEggItem;
import com.voranix.voranixmod.VoranixMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * The five raw ("Cruda") pizzas plus the chef spawn egg. The five cooked pizzas themselves are
 * {@code PizzaBlock}s now (see {@code ModBlocks}/{@code ModBlockItems}) - placed and eaten slice by
 * slice like vanilla cake, unlike these raw items, which are still consumed whole in one click (or,
 * for these, simply carried to the Horno de Barro - none of them are edible).
 * <p>
 * No ingredient sub-items (dough, sauce, cheese, toppings) exist yet - only the raw pizza itself, so
 * there is nothing yet to craft a raw pizza <em>from</em>. That's deliberate: the user asked for the
 * registration/cooking/trading structure now and models/textures (and, implicitly, the exact
 * ingredient list) later - adding placeholder ingredients before their real shape is known would be
 * guessing, not scaffolding.
 */
public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, VoranixMod.MODID);

    public static final RegistryObject<Item> PIZZA_MARGARITA_CRUDA = ITEMS.register("pizza_margarita_cruda",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PIZZA_PEPPERONI_CRUDA = ITEMS.register("pizza_pepperoni_cruda",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PIZZA_CHAMPINON_CRUDA = ITEMS.register("pizza_champinon_cruda",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PIZZA_CUATRO_QUESOS_CRUDA = ITEMS.register("pizza_cuatro_quesos_cruda",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PIZZA_SUPREMA_CRUDA = ITEMS.register("pizza_suprema_cruda",
            () -> new Item(new Item.Properties()));

    /** Spawns a Chef Mai Pizza (a villager with the {@code chef_maipizza} profession) - see {@link MaiPizzaChefSpawnEggItem}. */
    public static final RegistryObject<Item> HUEVO_MAIPIZZA = ITEMS.register("huevo_maipizza",
            () -> new MaiPizzaChefSpawnEggItem(() -> EntityType.VILLAGER, 0xE6A15C, 0xC23B22, new Item.Properties()));

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
