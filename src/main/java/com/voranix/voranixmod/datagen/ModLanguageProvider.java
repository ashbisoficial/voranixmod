package com.voranix.voranixmod.datagen;

import com.voranix.voranixmod.VoranixMod;
import com.voranix.ottoke.common.registration.ModBlocks;
import com.voranix.ottoke.common.registration.ModIngredients;
import com.voranix.ottoke.common.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

/**
 * Unlike every other datagen provider in this project (blockstates, item models, recipes, tags),
 * which each module generates for itself, translations for one locale all have to land in a single
 * file - {@code assets/voranixmod/lang/en_us.json} - because that is the one path the game actually
 * reads per modid+locale; Minecraft has no mechanism to merge several lang files for the same
 * namespace at runtime. Letting {@code OttokeModule} and {@code VoranixCoinsModule} each register
 * their own {@code LanguageProvider} for {@code en_us} used to throw ("Duplicate provider") once both
 * existed, and after renaming them apart to dodge that, the second one to run at datagen time simply
 * overwrote the first's output - neither module's {@code LanguageProvider} knew the other's entries
 * existed. Since the file itself can only have one owner, that owner lives here, at the
 * {@code VoranixMod} umbrella level that already knows about every module - not inside either module,
 * which would create the exact cross-module coupling those modules are supposed to avoid.
 */
public final class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, VoranixMod.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModBlocks.FREIDORA.get(), "Fryer");
        add(ModBlocks.CAJA_OTTOKE.get(), "Ottoke Box");
        add(ModBlocks.MOSTRADOR.get(), "Counter");
        add(ModBlocks.CAJA_REGISTRADORA.get(), "Register");
        add(ModBlocks.BASURERO.get(), "Trash Bin");
        add(ModBlocks.MENU.get(), "Menu");
        add(ModBlocks.CARTEL.get(), "Sign");
        add(ModBlocks.BANNER_OTTOKE.get(), "Ottoke Banner");
        add(ModBlocks.CARTEL_LUMINOSO.get(), "Glowing Sign");

        add(ModItems.BANDERILLA_CRUDA.get(), "Raw Banderilla");
        add(ModItems.BANDERILLA_CLASICA.get(), "Classic Banderilla");
        add(ModItems.COLA_OTTOKE.get(), "Ottoke Cola");
        add(ModItems.TE_DURAZNO.get(), "Peach Tea");
        add(ModItems.LIMON_OTTOKE.get(), "Lemon Soda");
        add(ModItems.NARANJA_OTTOKE.get(), "Orange Soda");
        add(ModItems.GINGER_OTTOKE.get(), "Ginger Ale");

        add(ModItems.BANDERILLA_PAPA_CRUDA.get(), "Raw Potato Banderilla");
        add(ModItems.BANDERILLA_PAPA.get(), "Potato Banderilla");
        add(ModItems.BANDERILLA_PICANTE_CRUDA.get(), "Raw Spicy Banderilla");
        add(ModItems.BANDERILLA_PICANTE.get(), "Spicy Banderilla");
        add(ModItems.BANDERILLA_MOZZARELLA_CRUDA.get(), "Raw Mozzarella Banderilla");
        add(ModItems.BANDERILLA_MOZZARELLA.get(), "Mozzarella Banderilla");
        add(ModItems.BANDERILLA_PREMIUM_CRUDA.get(), "Raw Premium Banderilla");
        add(ModItems.BANDERILLA_PREMIUM.get(), "Premium Banderilla");

        add(ModIngredients.HARINA.get(), "Flour");
        add(ModIngredients.SAL.get(), "Salt");
        add(ModIngredients.AZUCAR.get(), "Sugar");
        add(ModIngredients.ACEITE.get(), "Oil");
        add(ModIngredients.LECHE.get(), "Milk");
        add(ModIngredients.PALO.get(), "Stick");
        add(ModIngredients.SALCHICHA.get(), "Sausage");
        add(ModIngredients.MASA.get(), "Dough");
        add(ModIngredients.EMPANIZADO.get(), "Breaded");
        add(ModIngredients.QUESO.get(), "Cheese");

        add("container.ottoke.freidora", "Freidora");
        add("itemGroup.ottoke", "Ottoke Street Food");
        add("screen.ottoke.menu.title", "Ottoke Menu");
        add("category.ottoke.banderillas", "Banderillas");
        add("category.ottoke.bebidas", "Drinks");

        add("advancements.ottoke.root.title", "Ottoke Street Food");
        add("advancements.ottoke.root.description", "Sell delicious street food");
        add("advancements.ottoke.freidora_placed.title", "The Oil Is Ready");
        add("advancements.ottoke.freidora_placed.description", "Place a Freidora");
        add("advancements.ottoke.first_order.title", "First Order");
        add("advancements.ottoke.first_order.description", "Get a finished Banderilla");
        add("advancements.ottoke.apprentice.title", "Ottoke Apprentice");
        add("advancements.ottoke.apprentice.description", "Stock up 10 Banderillas of the same kind");
        add("advancements.ottoke.eat_picante.title", "Sure You Can Handle This?");
        add("advancements.ottoke.eat_picante.description", "Eat a Spicy Banderilla");
        add("advancements.ottoke.eat_mozzarella.title", "Double Cheese");
        add("advancements.ottoke.eat_mozzarella.description", "Eat a Mozzarella Banderilla");
        add("advancements.ottoke.vip_client.title", "VIP Client");
        add("advancements.ottoke.vip_client.description", "Taste every food the Ottoke menu offers");
        add("advancements.ottoke.fanatic.title", "Ottoke Fanatic");
        add("advancements.ottoke.fanatic.description", "Eat 250 Banderillas of the same kind");

        // Mod compatibility (see the com.voranix.ottoke.compat package): only ever read by JEI/EMI/Jade
        // if that specific mod is installed, but generated unconditionally like every other key here.
        add("emi.category.ottoke.freidora", "Freidora");
        add("jade.ottoke.freidora.cooking", "Cooking");

        add(com.voranix.voranixcoins.common.registration.ModItems.COIN_ESTANO.get(), "Tin Coin");
        add(com.voranix.voranixcoins.common.registration.ModItems.COIN_SILVER.get(), "Silver Coin");
        add(com.voranix.voranixcoins.common.registration.ModItems.COIN_GOLD.get(), "Gold Coin");
        add(com.voranix.voranixcoins.common.registration.ModItems.COIN_MITHRIL.get(), "Mithril Coin");
        add(com.voranix.voranixcoins.common.registration.ModItems.COIN_ORICALCO.get(), "Orichalcum Coin");
        add(com.voranix.voranixcoins.common.registration.ModItems.COIN_AJNA.get(), "Ajna Coin");
        add("itemGroup.voranixcoins", "Voranix Coins");

        add(com.voranix.maipizza.common.registration.ModBlocks.HORNO_DE_BARRO.get(), "Clay Oven");
        add(com.voranix.maipizza.common.registration.ModBlocks.MENU.get(), "Menu");
        add(com.voranix.maipizza.common.registration.ModBlocks.CARTEL.get(), "Sign");
        add(com.voranix.maipizza.common.registration.ModBlocks.CARTEL_LUMINOSO.get(), "Glowing Sign");

        add(com.voranix.maipizza.common.registration.ModItems.PIZZA_MARGARITA_CRUDA.get(), "Raw Pizza Margarita");
        add(com.voranix.maipizza.common.registration.ModBlocks.PIZZA_MARGARITA.get(), "Pizza Margarita");
        add(com.voranix.maipizza.common.registration.ModItems.PIZZA_PEPPERONI_CRUDA.get(), "Raw Pizza Pepperoni");
        add(com.voranix.maipizza.common.registration.ModBlocks.PIZZA_PEPPERONI.get(), "Pizza Pepperoni");
        add(com.voranix.maipizza.common.registration.ModItems.PIZZA_CHAMPINON_CRUDA.get(), "Raw Mushroom Pizza");
        add(com.voranix.maipizza.common.registration.ModBlocks.PIZZA_CHAMPINON.get(), "Mushroom Pizza");
        add(com.voranix.maipizza.common.registration.ModItems.PIZZA_CUATRO_QUESOS_CRUDA.get(), "Raw Four Cheese Pizza");
        add(com.voranix.maipizza.common.registration.ModBlocks.PIZZA_CUATRO_QUESOS.get(), "Four Cheese Pizza");
        add(com.voranix.maipizza.common.registration.ModItems.PIZZA_SUPREMA_CRUDA.get(), "Raw Pizza Suprema");
        add(com.voranix.maipizza.common.registration.ModBlocks.PIZZA_SUPREMA.get(), "Pizza Suprema");
        add(com.voranix.maipizza.common.registration.ModItems.HUEVO_MAIPIZZA.get(), "Chef Mai Pizza Egg");

        add(com.voranix.voranixmod.common.registration.ModMobEffects.GROW.get(), "Grow");
        add(com.voranix.voranixmod.common.registration.ModMobEffects.SHRINK.get(), "Shrink");

        add("container.maipizza.horno_de_barro", "Clay Oven");
        add("itemGroup.maipizza", "Mai Pizza");
        add("screen.maipizza.menu.title", "Mai Pizza Menu");
        add("category.maipizza.pizzas", "Pizzas");
    }
}
