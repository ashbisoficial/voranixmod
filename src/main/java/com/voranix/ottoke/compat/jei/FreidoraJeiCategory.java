package com.voranix.ottoke.compat.jei;

import com.voranix.voranixmod.VoranixMod;
import com.voranix.ottoke.common.cooking.FreidoraRecipe;
import com.voranix.ottoke.common.registration.ModBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * The Freidora's JEI recipe category: one ingredient slot, JEI's own built-in recipe arrow (no
 * texture of ours), one result slot. No PNG background exists for it - {@link #getWidth()}/
 * {@link #getHeight()} just reserve layout space instead of pointing at an {@link IDrawable}
 * background, consistent with the rest of the mod having no custom art yet (and with JEI's own
 * 15.20.0 guidance: an {@code IRecipeCategory} with no real background image now sizes itself
 * through these two methods rather than the deprecated {@code getBackground()}).
 */
final class FreidoraJeiCategory implements IRecipeCategory<FreidoraRecipe> {
    static final RecipeType<FreidoraRecipe> TYPE =
            RecipeType.create(VoranixMod.MODID, "freidora", FreidoraRecipe.class);

    private static final int WIDTH = 82;
    private static final int HEIGHT = 26;

    private final IDrawable icon;
    private final IDrawable arrow;

    FreidoraJeiCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.FREIDORA.get()));
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public RecipeType<FreidoraRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.ottoke.freidora");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FreidoraRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 5)
                .addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 5)
                .addItemStack(recipe.getCookingResult().result());
    }

    @Override
    public void draw(FreidoraRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics guiGraphics,
                      double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 21, 5);
    }
}
