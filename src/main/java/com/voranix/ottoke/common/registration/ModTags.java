package com.voranix.ottoke.common.registration;

import com.voranix.voranixmod.VoranixMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Every {@link TagKey} the mod either consumes (as a recipe ingredient) or contributes its own
 * items to. Recipes should reference these constants instead of a specific {@code voranixmod:} or
 * other mod's item id wherever an ingredient represents a category (a sausage, milk, flour) rather
 * than one exact item - that is what lets another mod's equivalent item work in an Ottoke recipe,
 * and an Ottoke ingredient work in another mod's recipe, without either mod knowing about the other.
 * <p>
 * Unlike {@code RegistryObject}, a {@code TagKey} needs no event bus registration - it is just a
 * typed reference to a tag id, resolved from whatever datapacks (ours and anyone else's) contribute
 * matching {@code data/<namespace>/tags/items/<path>.json} files at load time. This class only
 * declares the vocabulary; the actual item-to-tag membership lives in those JSON files.
 */
public final class ModTags {
    private ModTags() {
    }

    public static final class Items {
        /**
         * Cross-mod convention tags, under the shared {@code forge:} namespace. These are not part
         * of Forge's own built-in tag set (unlike e.g. {@code forge:ingots}) - they are community
         * conventions popularized by food-focused mods, and only mean something once some mod's
         * data pack populates them. We populate them with our own items below so that any other
         * mod already consuming these same tag names works with Ottoke ingredients for free.
         */
        public static final TagKey<Item> SAUSAGES = forge("sausages");
        public static final TagKey<Item> MILK = forge("milk");
        public static final TagKey<Item> FLOUR = forge("flour");
        public static final TagKey<Item> SUGAR = forge("sugar");
        public static final TagKey<Item> DOUGH = forge("dough");

        /**
         * Ottoke-specific tags: no cross-mod convention was found for these. Using our own
         * namespace here (instead of guessing a {@code forge:} name nothing else uses) keeps the
         * distinction between "this is a shared convention" and "this is our own category" honest -
         * see the class-level docs on {@link ModTags} for the decision rule.
         */
        public static final TagKey<Item> SALT = ottoke("salt");
        public static final TagKey<Item> COOKING_OIL = ottoke("cooking_oil");
        public static final TagKey<Item> BREADED_COATINGS = ottoke("breaded_coatings");
        /**
         * Deliberately not {@code forge:rods/wooden} (a real, established Forge tag for tool-handle
         * material): a corn dog skewer and a pickaxe handle share a material, not a role. Reusing
         * the tool-handle tag would let other mods' handle-crafting recipes accept a food skewer,
         * which is not a compatibility win, just an accident.
         */
        public static final TagKey<Item> SKEWERS = ottoke("skewers");

        private Items() {
        }

        private static TagKey<Item> forge(String path) {
            return ItemTags.create(new ResourceLocation("forge", path));
        }

        private static TagKey<Item> ottoke(String path) {
            return ItemTags.create(new ResourceLocation(VoranixMod.MODID, path));
        }
    }
}
