package com.voranix.ottoke.datagen;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.registration.ModBlocks;
import com.voranix.ottoke.common.registration.ModIngredients;
import com.voranix.ottoke.common.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public final class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, OttokeStreetFood.MODID, "en_us");
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
        add(ModBlocks.CARTEL_LUMINOSO.get(), "Luminous Sign");

        add(ModItems.BANDERILLA_CRUDA.get(), "Raw Banderilla");
        add(ModItems.BANDERILLA_CLASICA.get(), "Classic Banderilla");
        add(ModItems.COLA_OTTOKE.get(), "Ottoke Cola");
        add(ModItems.TE_DURAZNO.get(), "Peach Tea");
        add(ModItems.SODA_UVA.get(), "Grape Soda");
        add(ModItems.AGUA_MINERAL.get(), "Mineral Water");

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
    }
}
