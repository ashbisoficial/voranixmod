package com.voranix.ottoke.datagen;

import com.voranix.voranixmod.VoranixMod;
import com.voranix.ottoke.common.registration.ModIngredients;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * Tag membership this mod contributes to <em>other</em> mods' shared tags - the mirror image of
 * {@code ModTags}, which declares tags this mod would consume. A tag id is just a shared bucket
 * that any datapack (ours included) can add entries to; claiming membership costs nothing and stays
 * completely inert if nothing ever reads that tag, so unlike a recipe or a Java plugin class, this
 * needs no {@code ModLoadedCondition}/{@code ModCompat} guard at all - see {@code ModRecipeProvider}'s
 * compat section for where that guard genuinely is needed.
 * <p>
 * No block tags are contributed (yet), so the {@code blockTags} lookup {@link ItemTagsProvider}
 * requires is a permanently-empty, already-completed future rather than a whole separate
 * {@code BlockTagsProvider} this mod has no other use for.
 */
public final class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()),
                VoranixMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Create's Basin "dough by mixing" recipe (and anything else keyed off this shared
        // convention tag) accepts Harina as equivalent to Create's own wheat flour with no recipe
        // duplication on our side - see ModRecipeProvider's compat section for the reverse
        // direction (Create's flour working as an input for Harina).
        tag(ItemTags.create(new ResourceLocation("forge", "flour/wheat")))
                .add(ModIngredients.HARINA.get());
    }
}
