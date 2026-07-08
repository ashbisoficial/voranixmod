package com.voranix.ottoke.compat.emi;

import com.voranix.ottoke.common.cooking.FreidoraRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

/**
 * One EMI entry per {@link FreidoraRecipe}. Layout mirrors {@code FreidoraJeiCategory}'s: an input
 * slot, EMI's own built-in filling arrow (no texture of ours involved), an output slot - nothing
 * fancier, consistent with the rest of the mod never shipping custom art.
 */
final class FreidoraEmiRecipe extends BasicEmiRecipe {
    private final EmiIngredient input;
    private final EmiStack output;

    FreidoraEmiRecipe(EmiRecipeCategory category, FreidoraRecipe recipe) {
        super(category, recipe.getId(), 54, 18);
        this.input = EmiIngredient.of(recipe.getIngredient());
        this.output = EmiStack.of(recipe.getCookingResult().result());
        this.inputs.add(this.input);
        this.outputs.add(this.output);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(this.input, 0, 1);
        widgets.addFillingArrow(18, 1, 5000);
        widgets.addSlot(this.output, 36, 1);
    }
}
