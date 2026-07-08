package com.voranix.ottoke.datagen;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.cooking.FreidoraRecipeBuilder;
import com.voranix.ottoke.common.registration.ModBlocks;
import com.voranix.ottoke.common.registration.ModIngredients;
import com.voranix.ottoke.common.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import java.util.List;
import java.util.function.Consumer;

/**
 * Datagen replacement for the hand-written JSON under data/ottoke/recipes.
 * <p>
 * The Sal (sand) and Aceite (kelp) smelting sources are placeholders: vanilla has no salt or
 * cooking-oil concept to convert from, so these two are the least-arbitrary vanilla analogues
 * available, not a confirmed design.
 * <p>
 * The base chain (Trigo -&gt; Harina -&gt; Masa -&gt; Empanizado -&gt; Salchicha -&gt; Banderilla
 * Cruda) is a straight line with no branching: every step consumes the single output of the step
 * before it plus exactly one new raw resource. Every finished Banderilla beyond Clasica branches
 * off Banderilla Cruda rather than restarting the chain, and each has its own dedicated raw
 * ("Cruda") precursor - the Freidora only has one ingredient slot, so five finished foods need five
 * distinct inputs or their FreidoraRecipes would all try to match the same item. No finished
 * Banderilla has a crafting-table recipe; the {@link FreidoraRecipeBuilder} calls below are the
 * only way to produce any of them, and all five charge fuel.
 * <p>
 * Progression tiers:
 * <ul>
 *     <li><b>Tier 1 - Clasica:</b> the base chain alone.</li>
 *     <li><b>Tier 2 - Papa / Picante:</b> Banderilla Cruda plus one more ingredient each - a
 *     baked potato (pure Overworld farming) or oil plus Nether-tier blaze powder.</li>
 *     <li><b>Tier 3 - Mozzarella:</b> needs Queso, a new dairy ingredient one smelting step
 *     deeper than Leche - genuinely more advanced than anything Tier 2 needs.</li>
 *     <li><b>Tier 4 - Premium:</b> Queso again plus gold, combining the richest ingredient with
 *     the rarest material - the most expensive Banderilla in every sense (ingredients, cook time,
 *     fuel).</li>
 * </ul>
 * <p>
 * Block recipes are priced by role: the Freidora is the mod's core station and costs iron plus an
 * existing furnace; the six decorative/functional blocks are cheap, single-material conversions
 * matching whatever their block already visually represents (wood, stone, or a little metal).
 */
public final class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModIngredients.HARINA.get())
                .requires(Items.WHEAT)
                .unlockedBy("has_wheat", has(Items.WHEAT))
                .save(writer, id("harina_from_trigo"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModIngredients.LECHE.get())
                .requires(Items.MILK_BUCKET)
                .unlockedBy("has_milk_bucket", has(Items.MILK_BUCKET))
                .save(writer, id("leche_from_milk_bucket"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModIngredients.AZUCAR.get())
                .requires(Items.SUGAR)
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .save(writer, id("azucar_from_sugar"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModIngredients.PALO.get())
                .requires(Items.STICK)
                .unlockedBy("has_stick", has(Items.STICK))
                .save(writer, id("palo_from_stick"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.SAND), RecipeCategory.MISC,
                        ModIngredients.SAL.get(), 0.1F, 200)
                .unlockedBy("has_sand", has(Items.SAND))
                .save(writer, id("sal_from_sand"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.KELP), RecipeCategory.MISC,
                        ModIngredients.ACEITE.get(), 0.1F, 200)
                .unlockedBy("has_kelp", has(Items.KELP))
                .save(writer, id("aceite_from_kelp"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModIngredients.LECHE.get()), RecipeCategory.MISC,
                        ModIngredients.QUESO.get(), 0.1F, 200)
                .unlockedBy("has_leche", has(ModIngredients.LECHE.get()))
                .save(writer, id("queso_from_leche"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModIngredients.SALCHICHA.get())
                .requires(Items.COOKED_PORKCHOP)
                .requires(ModIngredients.SAL.get())
                .unlockedBy("has_cooked_porkchop", has(Items.COOKED_PORKCHOP))
                .save(writer, id("salchicha_from_cooked_porkchop_and_sal"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModIngredients.MASA.get())
                .requires(ModIngredients.HARINA.get())
                .requires(ModIngredients.LECHE.get())
                .unlockedBy("has_harina", has(ModIngredients.HARINA.get()))
                .save(writer, id("masa_from_harina_leche"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModIngredients.EMPANIZADO.get())
                .requires(ModIngredients.MASA.get())
                .requires(ModIngredients.AZUCAR.get())
                .unlockedBy("has_masa", has(ModIngredients.MASA.get()))
                .save(writer, id("empanizado_from_masa_azucar"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BANDERILLA_CRUDA.get())
                .requires(ModIngredients.EMPANIZADO.get())
                .requires(ModIngredients.SALCHICHA.get())
                .requires(ModIngredients.PALO.get())
                .unlockedBy("has_empanizado", has(ModIngredients.EMPANIZADO.get()))
                .save(writer, id("banderilla_cruda_from_empanizado_salchicha_palo"));

        // Tier 2 (lateral to Clasica, one extra ingredient each): Papa adds bulk from a baked
        // potato; Picante adds heat from oil plus Nether-tier blaze powder.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BANDERILLA_PAPA_CRUDA.get())
                .requires(ModItems.BANDERILLA_CRUDA.get())
                .requires(Items.BAKED_POTATO)
                .unlockedBy("has_banderilla_cruda", has(ModItems.BANDERILLA_CRUDA.get()))
                .save(writer, id("banderilla_papa_cruda_from_banderilla_cruda_and_baked_potato"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BANDERILLA_PICANTE_CRUDA.get())
                .requires(ModItems.BANDERILLA_CRUDA.get())
                .requires(ModIngredients.ACEITE.get())
                .requires(Items.BLAZE_POWDER)
                .unlockedBy("has_banderilla_cruda", has(ModItems.BANDERILLA_CRUDA.get()))
                .save(writer, id("banderilla_picante_cruda_from_banderilla_cruda_aceite_blaze_powder"));

        // Tier 3: Mozzarella needs Queso, a dedicated dairy ingredient that costs an extra smelting
        // step beyond Leche - deeper into the chain than anything Tier 2 needs.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BANDERILLA_MOZZARELLA_CRUDA.get())
                .requires(ModItems.BANDERILLA_CRUDA.get())
                .requires(ModIngredients.QUESO.get())
                .unlockedBy("has_queso", has(ModIngredients.QUESO.get()))
                .save(writer, id("banderilla_mozzarella_cruda_from_banderilla_cruda_and_queso"));

        // Tier 4 (top): Queso plus gold - the richest ingredient and the rarest material combined.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BANDERILLA_PREMIUM_CRUDA.get())
                .requires(ModItems.BANDERILLA_CRUDA.get())
                .requires(ModIngredients.QUESO.get())
                .requires(Items.GOLD_NUGGET)
                .unlockedBy("has_queso", has(ModIngredients.QUESO.get()))
                .save(writer, id("banderilla_premium_cruda_from_banderilla_cruda_queso_gold_nugget"));

        FreidoraRecipeBuilder.frying(
                        Ingredient.of(ModItems.BANDERILLA_CRUDA.get()),
                        new ItemStack(ModItems.BANDERILLA_CLASICA.get()),
                        160)
                .fuelCost(1)
                .experience(0.2F)
                .save(writer, id("freidora_banderilla"));

        FreidoraRecipeBuilder.frying(
                        Ingredient.of(ModItems.BANDERILLA_PAPA_CRUDA.get()),
                        new ItemStack(ModItems.BANDERILLA_PAPA.get()),
                        160)
                .fuelCost(1)
                .experience(0.2F)
                .save(writer, id("freidora_banderilla_papa"));

        FreidoraRecipeBuilder.frying(
                        Ingredient.of(ModItems.BANDERILLA_PICANTE_CRUDA.get()),
                        new ItemStack(ModItems.BANDERILLA_PICANTE.get()),
                        160)
                .fuelCost(1)
                .experience(0.25F)
                .save(writer, id("freidora_banderilla_picante"));

        FreidoraRecipeBuilder.frying(
                        Ingredient.of(ModItems.BANDERILLA_MOZZARELLA_CRUDA.get()),
                        new ItemStack(ModItems.BANDERILLA_MOZZARELLA.get()),
                        180)
                .fuelCost(1)
                .experience(0.25F)
                .save(writer, id("freidora_banderilla_mozzarella"));

        FreidoraRecipeBuilder.frying(
                        Ingredient.of(ModItems.BANDERILLA_PREMIUM_CRUDA.get()),
                        new ItemStack(ModItems.BANDERILLA_PREMIUM.get()),
                        200)
                .fuelCost(2)
                .experience(0.4F)
                .save(writer, id("freidora_banderilla_premium"));

        // Freidora: the mod's core station - iron plus an already-built furnace as the heat
        // source, deliberately pricier than any of the decorative blocks below.
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.FREIDORA.get())
                .pattern("III")
                .pattern("IFI")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('F', Items.FURNACE)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(writer, id("freidora"));

        // Decorative/functional blocks: cheap, single-material conversions matching what each
        // block already visually represents.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.CAJA_OTTOKE.get())
                .requires(Items.OAK_PLANKS, 4)
                .unlockedBy("has_oak_planks", has(Items.OAK_PLANKS))
                .save(writer, id("caja_ottoke"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.MOSTRADOR.get())
                .requires(Items.OAK_PLANKS, 3)
                .unlockedBy("has_oak_planks", has(Items.OAK_PLANKS))
                .save(writer, id("mostrador"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.CAJA_REGISTRADORA.get())
                .requires(Items.IRON_INGOT, 2)
                .requires(Items.OAK_PLANKS)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(writer, id("caja_registradora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.BASURERO.get())
                .requires(Items.COBBLESTONE, 3)
                .unlockedBy("has_cobblestone", has(Items.COBBLESTONE))
                .save(writer, id("basurero"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.MENU.get())
                .requires(Items.OAK_PLANKS)
                .requires(Items.PAPER)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(writer, id("menu"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.CARTEL.get())
                .requires(Items.OAK_PLANKS, 2)
                .requires(Items.STICK)
                .unlockedBy("has_oak_planks", has(Items.OAK_PLANKS))
                .save(writer, id("cartel"));

        buildCompatRecipes(writer);
    }

    /**
     * Optional cross-mod recipes: alternate, existing-item-only paths into ingredients the base
     * chain already produces by hand. Every one of these is wrapped in a
     * {@link ModLoadedCondition} - see {@link ExternalItemShapelessRecipe} for why that condition,
     * not the other mod's absence, is what keeps an uninstalled recipe from ever being parsed.
     * Nothing here changes what the vanilla recipes already produce; these only add more ways to
     * reach the same ingredients when the other mod happens to be installed too.
     */
    private void buildCompatRecipes(Consumer<FinishedRecipe> writer) {
        // Farmer's Delight: three of its own ingredients are close enough to ours (bottled milk,
        // cooked bacon, wheat dough) that treating them as direct substitutes needs no new items on
        // our side - see ModCompat's class doc for why this is data-only, with no compile-time
        // dependency on Farmer's Delight at all.
        ExternalItemShapelessRecipe.saveConditional(writer, id("leche_from_farmers_delight_milk_bottle"),
                List.of("farmersdelight:milk_bottle"),
                new ItemStack(ModIngredients.LECHE.get()),
                new ModLoadedCondition("farmersdelight"));

        ExternalItemShapelessRecipe.saveConditional(writer, id("salchicha_from_farmers_delight_bacon"),
                List.of("farmersdelight:cooked_bacon", "ottoke:sal"),
                new ItemStack(ModIngredients.SALCHICHA.get()),
                new ModLoadedCondition("farmersdelight"));

        ExternalItemShapelessRecipe.saveConditional(writer, id("masa_from_farmers_delight_wheat_dough"),
                List.of("farmersdelight:wheat_dough"),
                new ItemStack(ModIngredients.MASA.get()),
                new ModLoadedCondition("farmersdelight"));

        // Create: its Millstone already turns wheat into create:wheat_flour, which is exactly what
        // Harina is - a mechanized alternative to harina_from_trigo, not a new ingredient. The
        // reverse direction (our Harina feeding Create's own flour-consuming recipes, e.g. its
        // Basin dough mixing) is handled once, generically, by ModItemTagsProvider adding Harina to
        // forge:flour/wheat - Create's own recipes already key off that tag.
        ExternalItemShapelessRecipe.saveConditional(writer, id("harina_from_create_wheat_flour"),
                List.of("create:wheat_flour"),
                new ItemStack(ModIngredients.HARINA.get()),
                new ModLoadedCondition("create"));
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(OttokeStreetFood.MODID, path);
    }
}
