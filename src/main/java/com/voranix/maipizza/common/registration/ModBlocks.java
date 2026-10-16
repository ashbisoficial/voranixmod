package com.voranix.maipizza.common.registration;

import com.voranix.maipizza.common.block.CartelBlock;
import com.voranix.maipizza.common.block.CartelLuminosoBlock;
import com.voranix.maipizza.common.block.MenuBoardBlock;
import com.voranix.maipizza.common.block.PizzaBlock;
import com.voranix.maipizza.common.config.MaiPizzaConfig;
import com.voranix.maipizza.common.cooking.HornoDeBarroBlock;
import com.voranix.maipizza.common.item.MaiPizzaCategory;
import com.voranix.voranixmod.VoranixMod;
import com.voranix.voranixmod.common.item.FoodDefinition;
import com.voranix.voranixmod.common.item.FoodEffect;
import com.voranix.voranixmod.common.registration.ModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

/** Mirrors {@code com.voranix.ottoke.common.registration.ModBlocks} for the Mai Pizza module. */
public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, VoranixMod.MODID);

    public static final RegistryObject<Block> HORNO_DE_BARRO = registerBlock("horno_de_barro", HornoDeBarroBlock::new);

    /** See {@code ModBlocks} in Ottoke for why {@code noOcclusion()} is required on thin, non-cubic panels. */
    public static final RegistryObject<Block> MENU = registerBlock("menu_maipizza",
            () -> new MenuBoardBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).strength(0.8f, 2.0f).noOcclusion().lightLevel(state -> 15)));
    public static final RegistryObject<Block> CARTEL = registerBlock("cartel_maipizza",
            () -> new CartelBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).strength(0.8f, 2.0f).noOcclusion().lightLevel(state -> 15)));
    public static final RegistryObject<Block> CARTEL_LUMINOSO = registerBlock("cartel_luminoso_maipizza",
            () -> new CartelLuminosoBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(0.8f, 2.0f)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(CartelLuminosoBlock.LIT) ? 15 : 0)));

    /**
     * The starter tier - no inherent effect before, now also grants Encoger (see the user's "efecto
     * de tamaño en una pizza existente" request). Config-driven hunger/saturation like Banderilla
     * Clasica. Particles and the HUD icon are both off (the trailing {@code false, false, false}) -
     * the ambient swirl reads as oversized noise on an already-shrunk player, and there's no icon
     * art for this effect yet anyway.
     */
    public static final RegistryObject<Block> PIZZA_MARGARITA = registerPizza("pizza_margarita",
            () -> new FoodDefinition(
                    MaiPizzaConfig.PIZZA_MARGARITA_HUNGER.get(),
                    MaiPizzaConfig.PIZZA_MARGARITA_SATURATION.get().floatValue(),
                    List.of(new FoodEffect(() -> new MobEffectInstance(ModMobEffects.SHRINK.get(), 1200, 0, false, false, false), 1.0F))));

    /** Same portion as Margarita; pepperoni's spice grants Speed I for 15s. */
    public static final RegistryObject<Block> PIZZA_PEPPERONI = registerPizza("pizza_pepperoni",
            () -> new FoodDefinition(8, 0.9F, List.of(
                    new FoodEffect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 0), 1.0F)
            )));

    /** Mushrooms add hunger without much saturation; grants Regeneration I for 10s. */
    public static final RegistryObject<Block> PIZZA_CHAMPINON = registerPizza("pizza_champinon",
            () -> new FoodDefinition(9, 0.7F, List.of(
                    new FoodEffect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 1.0F)
            )));

    /** Same hunger as Margarita but pure fat - the highest saturation of the lateral tiers. Grants Strength I for 15s. */
    public static final RegistryObject<Block> PIZZA_CUATRO_QUESOS = registerPizza("pizza_cuatro_quesos",
            () -> new FoodDefinition(8, 1.0F, List.of(
                    new FoodEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 0), 1.0F)
            )));

    /**
     * The mod's best food on both axes at once, mirroring Banderilla Premium's role: Resistance I
     * for 20s plus Absorption I for 120s (same duration as a vanilla golden apple), plus Agrandar as
     * a third effect (top tier gets the flashier size effect - see {@code PIZZA_MARGARITA} for the
     * other one) - always edible even at a full hunger bar.
     */
    public static final RegistryObject<Block> PIZZA_SUPREMA = registerPizza("pizza_suprema",
            () -> new FoodDefinition(10, 1.2F, List.of(
                    new FoodEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0), 1.0F),
                    new FoodEffect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0F),
                    new FoodEffect(() -> new MobEffectInstance(ModMobEffects.GROW.get(), 1200, 0, false, false, false), 1.0F)
            ), true));

    private ModBlocks() {
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockSupplier) {
        return BLOCKS.register(name, blockSupplier);
    }

    /** Same {@code BlockBehaviour.Properties} vanilla's own {@code CakeBlock} uses - soft, no occlusion (the pizza never fills a full cube), placed/eaten like cake. */
    private static RegistryObject<Block> registerPizza(String name, Supplier<FoodDefinition> definition) {
        return registerBlock(name, () -> new PizzaBlock(MaiPizzaCategory.PIZZA, definition,
                BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(0.5f).sound(SoundType.WOOL).noOcclusion()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
