package com.voranix.voranixmod.datagen;

import com.voranix.voranixmod.VoranixMod;
import com.voranix.ottoke.common.registration.ModBlocks;
import com.voranix.ottoke.common.registration.ModIngredients;
import com.voranix.ottoke.common.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

/**
 * Spanish counterpart to {@link ModLanguageProvider} - see that class for why both modules'
 * translations live here instead of in each module's own datagen package. Ottoke never had a
 * Spanish translation before this; names here are the plain Spanish words the item IDs were already
 * written in (e.g. {@code banderilla_papa_cruda} -> "Banderilla de Papa Cruda"), not translations of
 * the English display names. Voranix Coins' six entries are Cameloth's original {@code es_es} values,
 * carried over unchanged from the standalone mod's jar.
 */
public final class ModLanguageProviderEs extends LanguageProvider {
    public ModLanguageProviderEs(PackOutput output) {
        super(output, VoranixMod.MODID, "es_es");
    }

    @Override
    protected void addTranslations() {
        add(ModBlocks.FREIDORA.get(), "Freidora");
        add(ModBlocks.CAJA_OTTOKE.get(), "Caja Ottoke");
        add(ModBlocks.MOSTRADOR.get(), "Mostrador");
        add(ModBlocks.CAJA_REGISTRADORA.get(), "Caja Registradora");
        add(ModBlocks.BASURERO.get(), "Basurero");
        add(ModBlocks.MENU.get(), "Menú");
        add(ModBlocks.CARTEL.get(), "Cartel");
        add(ModBlocks.BANNER_OTTOKE.get(), "Bandera Ottoke");
        add(ModBlocks.CARTEL_LUMINOSO.get(), "Cartel Luminoso");

        add(ModItems.BANDERILLA_CRUDA.get(), "Banderilla Cruda");
        add(ModItems.BANDERILLA_CLASICA.get(), "Banderilla Clásica");
        add(ModItems.COLA_OTTOKE.get(), "Cola Ottoke");
        add(ModItems.TE_DURAZNO.get(), "Té de Durazno");
        add(ModItems.LIMON_OTTOKE.get(), "Limón Ottoke");
        add(ModItems.NARANJA_OTTOKE.get(), "Naranja Ottoke");
        add(ModItems.GINGER_OTTOKE.get(), "Jengibre Ottoke");

        add(ModItems.BANDERILLA_PAPA_CRUDA.get(), "Banderilla de Papa Cruda");
        add(ModItems.BANDERILLA_PAPA.get(), "Banderilla de Papa");
        add(ModItems.BANDERILLA_PICANTE_CRUDA.get(), "Banderilla Picante Cruda");
        add(ModItems.BANDERILLA_PICANTE.get(), "Banderilla Picante");
        add(ModItems.BANDERILLA_MOZZARELLA_CRUDA.get(), "Banderilla Mozzarella Cruda");
        add(ModItems.BANDERILLA_MOZZARELLA.get(), "Banderilla Mozzarella");
        add(ModItems.BANDERILLA_PREMIUM_CRUDA.get(), "Banderilla Premium Cruda");
        add(ModItems.BANDERILLA_PREMIUM.get(), "Banderilla Premium");

        add(ModIngredients.HARINA.get(), "Harina");
        add(ModIngredients.SAL.get(), "Sal");
        add(ModIngredients.AZUCAR.get(), "Azúcar");
        add(ModIngredients.ACEITE.get(), "Aceite");
        add(ModIngredients.LECHE.get(), "Leche");
        add(ModIngredients.PALO.get(), "Palo");
        add(ModIngredients.SALCHICHA.get(), "Salchicha");
        add(ModIngredients.MASA.get(), "Masa");
        add(ModIngredients.EMPANIZADO.get(), "Empanizado");
        add(ModIngredients.QUESO.get(), "Queso");

        add("container.ottoke.freidora", "Freidora");
        add("itemGroup.ottoke", "Ottoke Street Food");
        add("screen.ottoke.menu.title", "Menú Ottoke");
        add("category.ottoke.banderillas", "Banderillas");
        add("category.ottoke.bebidas", "Bebidas");

        add("advancements.ottoke.root.title", "Ottoke Street Food");
        add("advancements.ottoke.root.description", "Vende comida callejera deliciosa");
        add("advancements.ottoke.freidora_placed.title", "El Aceite Está Listo");
        add("advancements.ottoke.freidora_placed.description", "Coloca una Freidora");
        add("advancements.ottoke.first_order.title", "Primer Pedido");
        add("advancements.ottoke.first_order.description", "Consigue una Banderilla lista");
        add("advancements.ottoke.apprentice.title", "Aprendiz de Ottoke");
        add("advancements.ottoke.apprentice.description", "Acumula 10 Banderillas del mismo tipo");
        add("advancements.ottoke.eat_picante.title", "¿Seguro Que Aguantas Esto?");
        add("advancements.ottoke.eat_picante.description", "Come una Banderilla Picante");
        add("advancements.ottoke.eat_mozzarella.title", "Doble Queso");
        add("advancements.ottoke.eat_mozzarella.description", "Come una Banderilla Mozzarella");
        add("advancements.ottoke.vip_client.title", "Cliente VIP");
        add("advancements.ottoke.vip_client.description", "Prueba cada comida del menú Ottoke");
        add("advancements.ottoke.fanatic.title", "Fanático de Ottoke");
        add("advancements.ottoke.fanatic.description", "Come 250 Banderillas del mismo tipo");

        add("emi.category.ottoke.freidora", "Freidora");
        add("jade.ottoke.freidora.cooking", "Cocinando");

        add(com.voranix.voranixcoins.common.registration.ModItems.COIN_ESTANO.get(), "Moneda de Estaño");
        add(com.voranix.voranixcoins.common.registration.ModItems.COIN_SILVER.get(), "Moneda de Plata");
        add(com.voranix.voranixcoins.common.registration.ModItems.COIN_GOLD.get(), "Moneda de Oro");
        add(com.voranix.voranixcoins.common.registration.ModItems.COIN_MITHRIL.get(), "Moneda de Mithril");
        add(com.voranix.voranixcoins.common.registration.ModItems.COIN_ORICALCO.get(), "Moneda de Oricalco");
        add(com.voranix.voranixcoins.common.registration.ModItems.COIN_AJNA.get(), "Moneda de Ajna");
        add("itemGroup.voranixcoins", "Monedas Voranix");

        add(com.voranix.maipizza.common.registration.ModBlocks.HORNO_DE_BARRO.get(), "Horno de Barro");
        add(com.voranix.maipizza.common.registration.ModBlocks.MENU.get(), "Menú");
        add(com.voranix.maipizza.common.registration.ModBlocks.CARTEL.get(), "Cartel");
        add(com.voranix.maipizza.common.registration.ModBlocks.CARTEL_LUMINOSO.get(), "Cartel Luminoso");

        add(com.voranix.maipizza.common.registration.ModItems.PIZZA_MARGARITA_CRUDA.get(), "Pizza Margarita Cruda");
        add(com.voranix.maipizza.common.registration.ModBlocks.PIZZA_MARGARITA.get(), "Pizza Margarita");
        add(com.voranix.maipizza.common.registration.ModItems.PIZZA_PEPPERONI_CRUDA.get(), "Pizza Pepperoni Cruda");
        add(com.voranix.maipizza.common.registration.ModBlocks.PIZZA_PEPPERONI.get(), "Pizza Pepperoni");
        add(com.voranix.maipizza.common.registration.ModItems.PIZZA_CHAMPINON_CRUDA.get(), "Pizza de Champiñón Cruda");
        add(com.voranix.maipizza.common.registration.ModBlocks.PIZZA_CHAMPINON.get(), "Pizza de Champiñón");
        add(com.voranix.maipizza.common.registration.ModItems.PIZZA_CUATRO_QUESOS_CRUDA.get(), "Pizza Cuatro Quesos Cruda");
        add(com.voranix.maipizza.common.registration.ModBlocks.PIZZA_CUATRO_QUESOS.get(), "Pizza Cuatro Quesos");
        add(com.voranix.maipizza.common.registration.ModItems.PIZZA_SUPREMA_CRUDA.get(), "Pizza Suprema Cruda");
        add(com.voranix.maipizza.common.registration.ModBlocks.PIZZA_SUPREMA.get(), "Pizza Suprema");
        add(com.voranix.maipizza.common.registration.ModItems.HUEVO_MAIPIZZA.get(), "Huevo de Chef Mai Pizza");

        add(com.voranix.voranixmod.common.registration.ModMobEffects.GROW.get(), "Agrandar");
        add(com.voranix.voranixmod.common.registration.ModMobEffects.SHRINK.get(), "Encoger");

        add("container.maipizza.horno_de_barro", "Horno de Barro");
        add("itemGroup.maipizza", "Mai Pizza");
        add("screen.maipizza.menu.title", "Menú Mai Pizza");
        add("category.maipizza.pizzas", "Pizzas");
    }
}
