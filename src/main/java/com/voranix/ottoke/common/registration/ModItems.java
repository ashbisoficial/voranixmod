package com.voranix.ottoke.common.registration;

import com.voranix.voranixmod.VoranixMod;
import com.voranix.ottoke.common.config.OttokeConfig;
import com.voranix.ottoke.common.item.FoodCategory;
import com.voranix.voranixmod.common.item.FoodDefinition;
import com.voranix.voranixmod.common.item.FoodEffect;
import com.voranix.ottoke.common.item.OttokeDrinkItem;
import com.voranix.ottoke.common.item.OttokeFoodItem;
import com.voranix.ottoke.common.villager.OttokeChefSpawnEggItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

/**
 * Every Banderilla is two items: a non-edible "Cruda" (raw, assembled at a crafting table) and the
 * finished food it becomes once fried. This split exists because the Freidora only has one
 * ingredient slot - {@link com.voranix.voranixmod.common.cooking.system.CookingContainer} matches a
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
            DeferredRegister.create(ForgeRegistries.ITEMS, VoranixMod.MODID);

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

    /** Same portion as Clasica; the added oil raises saturation slightly without adding hunger. Spice gives Speed I for 15s. */
    public static final RegistryObject<Item> BANDERILLA_PICANTE = ITEMS.register("banderilla_picante",
            () -> new OttokeFoodItem(FoodCategory.BANDERILLA, new FoodDefinition(8, 0.9F, List.of(
                    new FoodEffect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 0), 1.0F)
            )), new Item.Properties()));

    public static final RegistryObject<Item> BANDERILLA_MOZZARELLA_CRUDA = ITEMS.register("banderilla_mozzarella_cruda",
            () -> new Item(new Item.Properties()));

    /** Cheese adds no extra volume (same hunger as Clasica) but is pure fat - the highest saturation of the lateral tiers. Grants Strength I for 15s. */
    public static final RegistryObject<Item> BANDERILLA_MOZZARELLA = ITEMS.register("banderilla_mozzarella",
            () -> new OttokeFoodItem(FoodCategory.BANDERILLA, new FoodDefinition(8, 1.0F, List.of(
                    new FoodEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 0), 1.0F)
            )), new Item.Properties()));

    public static final RegistryObject<Item> BANDERILLA_PREMIUM_CRUDA = ITEMS.register("banderilla_premium_cruda",
            () -> new Item(new Item.Properties()));

    /**
     * The mod's best food on both axes at once - cheese plus gold, strictly better than every other tier.
     * Also the mod's best <em>effects</em>: Resistance I for 20s plus Absorption I for 120s - the same
     * Absorption duration as a vanilla golden apple, matching the "gilded" theming of this tier.
     */
    public static final RegistryObject<Item> BANDERILLA_PREMIUM = ITEMS.register("banderilla_premium",
            () -> new OttokeFoodItem(FoodCategory.BANDERILLA, new FoodDefinition(10, 1.2F, List.of(
                    new FoodEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0), 1.0F),
                    new FoodEffect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0F)
            ), true), new Item.Properties()));

    /**
     * All five drinks share one shape: bottled (stacks to 16, like vanilla's honey/potions, not
     * food-stacked to 64), returns a vanilla glass bottle on finish, tagged {@link FoodCategory#BEBIDA}.
     * Nutrition and effects differ per drink, so those are the only things each call site below
     * states - see {@link #registerDrink} for where the shared shape actually lives.
     */
    public static final RegistryObject<Item> COLA_OTTOKE = registerDrink("cola_ottoke", new FoodDefinition(2, 0.3F, List.of(
            new FoodEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 0), 1.0F)
    )));

    /** Slightly more saturating than a soda - tea reads as marginally more "nutritious" than the rest. */
    public static final RegistryObject<Item> TE_DURAZNO = registerDrink("te_durazno", new FoodDefinition(2, 0.4F, List.of(
            new FoodEffect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400, 1), 1.0F)
    )));

    /** Same portion as Cola, but a touch lighter - citrus reads as less syrupy than a cola. */
    public static final RegistryObject<Item> LIMON_OTTOKE = registerDrink("limon_ottoke", new FoodDefinition(2, 0.25F, List.of(
            new FoodEffect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0), 1.0F)
    )));

    /** Same sugary tier as Cola, but a bit more filling - orange soda is heavier on syrup. */
    public static final RegistryObject<Item> NARANJA_OTTOKE = registerDrink("naranja_ottoke", new FoodDefinition(3, 0.3F, List.of(
            new FoodEffect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1), 1.0F)
    )));

    /** Lightest of the four sodas - less syrupy than Cola, Limon or Naranja, and the least filling of them. */
    public static final RegistryObject<Item> GINGER_OTTOKE = registerDrink("ginger_ottoke", new FoodDefinition(1, 0.2F, List.of(
            new FoodEffect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 3000, 0), 1.0F)
    )));

    /** Spawns a Chef Ottoke (a villager with the {@code chef_ottoke} profession) - see {@link OttokeChefSpawnEggItem}. */
    public static final RegistryObject<Item> HUEVO_OTTOKE = ITEMS.register("huevo_ottoke",
            () -> new OttokeChefSpawnEggItem(() -> EntityType.VILLAGER, 0xB33A2E, 0xB98461, new Item.Properties()));

    private ModItems() {
    }

    /**
     * Every drink is drinkable past a full hunger bar - unlike food, a drink's appeal (the
     * effect, the refreshment) has nothing to do with being hungry, so gating it on hunger the way
     * plain food is would just be an annoying vanilla default carried over without thought. Rebuilt
     * here as the one place all five {@link FoodDefinition}s pass through, rather than adding
     * {@code alwaysEdible=true} to each call site above.
     */
    private static RegistryObject<Item> registerDrink(String name, FoodDefinition definition) {
        FoodDefinition alwaysDrinkable = new FoodDefinition(definition.nutrition(), definition.saturation(), definition.effects(), true);
        return ITEMS.register(name, () -> new OttokeDrinkItem(
                FoodCategory.BEBIDA,
                alwaysDrinkable,
                Items.GLASS_BOTTLE,
                new Item.Properties().stacksTo(16)));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
