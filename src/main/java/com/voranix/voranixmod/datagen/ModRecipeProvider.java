package com.voranix.voranixmod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.function.Consumer;

/**
 * Combines every module's recipes into the one {@code RecipeProvider} a single
 * {@code GatherDataEvent} can register - see {@code ModLanguageProvider}'s javadoc for the same
 * "only one owner of this name" constraint ({@code RecipeProvider#getName()} is {@code final} and
 * always returns {@code "Recipes"}). Each module's own {@code ModRecipeProvider} keeps its full
 * recipe content exactly where it was; this just constructs one of each and calls its (now
 * {@code public}) {@code buildRecipes} directly, since a {@code protected} override on a sibling
 * subclass isn't callable across packages the way a widened-visibility method is.
 */
public final class ModRecipeProvider extends RecipeProvider {
    private final PackOutput output;

    public ModRecipeProvider(PackOutput output) {
        super(output);
        this.output = output;
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        new com.voranix.ottoke.datagen.ModRecipeProvider(this.output).buildRecipes(writer);
        new com.voranix.maipizza.datagen.ModRecipeProvider(this.output).buildRecipes(writer);
    }
}
