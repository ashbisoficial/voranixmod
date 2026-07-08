package com.voranix.ottoke.common.registration;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.config.OttokeConfig;
import com.voranix.ottoke.common.item.FoodCategory;
import com.voranix.ottoke.common.item.FoodDefinition;
import com.voranix.ottoke.common.item.OttokeDrinkItem;
import com.voranix.ottoke.common.item.OttokeFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Every Banderilla is two items: a non-edible "Cruda" (raw, assembled at a crafting table) and the
 * finished food it becomes once fried. This split exists because the Freidora only has one
 * ingredient slot - {@link com.voranix.ottoke.common.cooking.system.CookingContainer} matches a
 * single item, so five different finished foods need five distinct raw inputs, or their
 * {@code FreidoraRecipe}s would all compete to match the same item and only one could ever win.
 * <p>
 * Nutrition values here are fixed ({@link FoodDefinition} built inline, not behind a config-reading
 * supplier) because nothing asked for these four to be server-tunable the way Banderilla Clasica's
 * historically has been - see the class javadoc on {@link OttokeConfig} if that changes later.
 * <p>
 * Every finished food is tagged with a {@link FoodCategory} at construction - that is what lets the
 * Menu board's screen list "every Banderilla" / "every Bebida" without a maintained item list.
 */
public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, OttokeStreetFood.MODID);

    public static final RegistryObject<Item> BANDERILLA_CRUDA = ITEMS.register("banderilla_cruda",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BANDERILLA_CLASICA = ITEMS.register("banderilla_clasica",
            () -> new OttokeFoodItem(
                    FoodCategory.BANDERILLA,
                    () -> new FoodDefinition(
                            OttokeConfig.BANDERILLA_CLASICA_HUNGER.get(),
                            OttokeConfig.BANDERILLA_CLASICA_SATURATION.get().floatValue()),
                    new Item.Properties()));

    public static final RegistryObject<Item> BANDERILLA_PAPA_CRUDA = ITEMS.register("banderilla_papa_cruda",
            () -> new Item(new Item.Properties()));

    /** More filling than Clasica (extra hunger from the potato) but less rich (lower saturation). */
    public static final RegistryObject<Item> BANDERILLA_PAPA = ITEMS.register("banderilla_papa",
            () -> new OttokeFoodItem(FoodCategory.BANDERILLA, new FoodDefinition(9, 0.7F), new Item.Properties()));

    public static final RegistryObject<Item> BANDERILLA_PICANTE_CRUDA = ITEMS.register("banderilla_picante_cruda",
            () -> new Item(new Item.Properties()));

    /** Same portion as Clasica; the added oil raises saturation slightly without adding hunger. */
    public static final RegistryObject<Item> BANDERILLA_PICANTE = ITEMS.register("banderilla_picante",
            () -> new OttokeFoodItem(FoodCategory.BANDERILLA, new FoodDefinition(8, 0.9F), new Item.Properties()));

    public static final RegistryObject<Item> BANDERILLA_MOZZARELLA_CRUDA = ITEMS.register("banderilla_mozzarella_cruda",
            () -> new Item(new Item.Properties()));

    /** Cheese adds no extra volume (same hunger as Clasica) but is pure fat - the highest saturation of the lateral tiers. */
    public static final RegistryObject<Item> BANDERILLA_MOZZARELLA = ITEMS.register("banderilla_mozzarella",
            () -> new OttokeFoodItem(FoodCategory.BANDERILLA, new FoodDefinition(8, 1.0F), new Item.Properties()));

    public static final RegistryObject<Item> BANDERILLA_PREMIUM_CRUDA = ITEMS.register("banderilla_premium_cruda",
            () -> new Item(new Item.Properties()));

    /** The mod's best food on both axes at once - cheese plus gold, strictly better than every other tier. */
    public static final RegistryObject<Item> BANDERILLA_PREMIUM = ITEMS.register("banderilla_premium",
            () -> new OttokeFoodItem(FoodCategory.BANDERILLA, new FoodDefinition(10, 1.2F), new Item.Properties()));

    /**
     * All four drinks share one shape: bottled (stacks to 16, like vanilla's honey/potions, not
     * food-stacked to 64), returns a vanilla glass bottle on finish, no effects yet, tagged
     * {@link FoodCategory#BEBIDA}. Only the nutrition differs, so that's the only thing each call
     * site below has to state - see {@link #registerDrink} for where that shape actually lives.
     */
    public static final RegistryObject<Item> COLA_OTTOKE = registerDrink("cola_ottoke", new FoodDefinition(2, 0.3F));

    /** Slightly more saturating than a soda - tea reads as marginally more "nutritious" than Cola/Soda de Uva. */
    public static final RegistryObject<Item> TE_DURAZNO = registerDrink("te_durazno", new FoodDefinition(2, 0.4F));

    /** Same sugary tier as Cola, but a bit more filling - grape soda is heavier on syrup. */
    public static final RegistryObject<Item> SODA_UVA = registerDrink("soda_uva", new FoodDefinition(3, 0.3F));

    /** Plain water: barely restores hunger and has almost no staying power, unlike the flavored drinks. */
    public static final RegistryObject<Item> AGUA_MINERAL = registerDrink("agua_mineral", new FoodDefinition(1, 0.1F));

    private ModItems() {
    }

    private static RegistryObject<Item> registerDrink(String name, FoodDefinition definition) {
        return ITEMS.register(name, () -> new OttokeDrinkItem(
                FoodCategory.BEBIDA,
                definition,
                Items.GLASS_BOTTLE,
                new Item.Properties().stacksTo(16)));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
