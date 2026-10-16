package com.voranix.maipizza.datagen;

import com.voranix.maipizza.common.cooking.HornoDeBarroRecipeBuilder;
import com.voranix.maipizza.common.registration.ModBlocks;
import com.voranix.maipizza.common.registration.ModItems;
import com.voranix.voranixmod.VoranixMod;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

/**
 * Datagen recipes for Mai Pizza, mirroring {@code com.voranix.ottoke.datagen.ModRecipeProvider}'s
 * shape. Only the block recipes and the five Horno de Barro baking recipes exist so far - there is
 * no ingredient chain yet to assemble a raw pizza from (see {@code ModItems}' javadoc for why that's
 * deliberate), so no shapeless recipe produces any {@code *_CRUDA} item; they're creative-tab/command
 * only until that chain exists.
 */
public final class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    /**
     * {@code RecipeProvider#getName()} is {@code final} and returns the fixed string
     * {@code "Recipes"} with no per-modid variation - two separate instances registered as top-level
     * providers in the same {@code GatherDataEvent} would collide (exactly like two
     * {@code LanguageProvider}s for the same locale do - see
     * {@code com.voranix.voranixmod.datagen.ModLanguageProvider}'s javadoc). Rather than overriding
     * {@code getName()} (not possible here) this class is never itself registered as a provider;
     * {@code com.voranix.voranixmod.datagen.ModRecipeProvider} constructs one and calls this method
     * directly instead, which is why it's {@code public} rather than the usual {@code protected}
     * override.
     */
    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
        // Horno de Barro: a brick oven, priced like Freidora's furnace-plus-iron shape but with
        // bricks around a furnace instead of iron around a furnace - matching what the block
        // actually represents (a wood/clay-fired brick oven, not a metal fryer).
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.HORNO_DE_BARRO.get())
                .pattern("BBB")
                .pattern("BFB")
                .pattern("BBB")
                .define('B', Items.BRICK)
                .define('F', Items.FURNACE)
                .unlockedBy("has_brick", has(Items.BRICK))
                .save(writer, id("horno_de_barro"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.MENU.get())
                .requires(Items.OAK_PLANKS)
                .requires(Items.PAPER)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(writer, id("menu_maipizza"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.CARTEL.get())
                .requires(Items.OAK_PLANKS, 2)
                .requires(Items.STICK)
                .unlockedBy("has_oak_planks", has(Items.OAK_PLANKS))
                .save(writer, id("cartel_maipizza"));

        HornoDeBarroRecipeBuilder.baking(
                        Ingredient.of(ModItems.PIZZA_MARGARITA_CRUDA.get()),
                        new ItemStack(ModBlocks.PIZZA_MARGARITA.get()),
                        160)
                .fuelCost(1)
                .save(writer, id("horno_de_barro_pizza_margarita"));

        HornoDeBarroRecipeBuilder.baking(
                        Ingredient.of(ModItems.PIZZA_PEPPERONI_CRUDA.get()),
                        new ItemStack(ModBlocks.PIZZA_PEPPERONI.get()),
                        160)
                .fuelCost(1)
                .save(writer, id("horno_de_barro_pizza_pepperoni"));

        HornoDeBarroRecipeBuilder.baking(
                        Ingredient.of(ModItems.PIZZA_CHAMPINON_CRUDA.get()),
                        new ItemStack(ModBlocks.PIZZA_CHAMPINON.get()),
                        160)
                .fuelCost(1)
                .save(writer, id("horno_de_barro_pizza_champinon"));

        HornoDeBarroRecipeBuilder.baking(
                        Ingredient.of(ModItems.PIZZA_CUATRO_QUESOS_CRUDA.get()),
                        new ItemStack(ModBlocks.PIZZA_CUATRO_QUESOS.get()),
                        180)
                .fuelCost(1)
                .save(writer, id("horno_de_barro_pizza_cuatro_quesos"));

        HornoDeBarroRecipeBuilder.baking(
                        Ingredient.of(ModItems.PIZZA_SUPREMA_CRUDA.get()),
                        new ItemStack(ModBlocks.PIZZA_SUPREMA.get()),
                        200)
                .fuelCost(2)
                .save(writer, id("horno_de_barro_pizza_suprema"));
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(VoranixMod.MODID, path);
    }
}
