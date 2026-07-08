package com.voranix.ottoke.common.registration;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.item.IngredientCategory;
import com.voranix.ottoke.common.item.IngredientItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers crafting-chain ingredients (raw pantry goods and the intermediate goods made from
 * them), kept separate from {@link ModItems} which holds finished, directly-consumable food.
 * That split means a future recipe pass only ever has to look in one place for "things you cook
 * with" versus "things you eat".
 * <p>
 * Every entry is registered through {@link #registerIngredient}, which forces a category to be
 * supplied - there is no way to add an ingredient here without classifying it. Adding a new
 * ingredient for a future recipe is one line; it never requires a new Item subclass or a new
 * registry.
 */
public final class ModIngredients {
    public static final DeferredRegister<Item> INGREDIENTS =
            DeferredRegister.create(ForgeRegistries.ITEMS, OttokeStreetFood.MODID);

    public static final RegistryObject<Item> HARINA = registerIngredient("harina", IngredientCategory.BASE);
    public static final RegistryObject<Item> SAL = registerIngredient("sal", IngredientCategory.BASE);
    public static final RegistryObject<Item> AZUCAR = registerIngredient("azucar", IngredientCategory.BASE);
    public static final RegistryObject<Item> ACEITE = registerIngredient("aceite", IngredientCategory.FAT);
    public static final RegistryObject<Item> LECHE = registerIngredient("leche", IngredientCategory.DAIRY);
    public static final RegistryObject<Item> PALO = registerIngredient("palo", IngredientCategory.STRUCTURAL);
    public static final RegistryObject<Item> SALCHICHA = registerIngredient("salchicha", IngredientCategory.PROTEIN);
    public static final RegistryObject<Item> MASA = registerIngredient("masa", IngredientCategory.PROCESSED);
    public static final RegistryObject<Item> EMPANIZADO = registerIngredient("empanizado", IngredientCategory.PROCESSED);
    public static final RegistryObject<Item> QUESO = registerIngredient("queso", IngredientCategory.DAIRY);

    private ModIngredients() {
    }

    private static RegistryObject<Item> registerIngredient(String name, IngredientCategory category) {
        return INGREDIENTS.register(name, () -> new IngredientItem(category, new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        INGREDIENTS.register(eventBus);
    }
}
