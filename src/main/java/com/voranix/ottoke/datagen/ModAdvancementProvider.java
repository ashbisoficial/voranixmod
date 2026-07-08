package com.voranix.ottoke.datagen;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.registration.ModBlocks;
import com.voranix.ottoke.common.registration.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.PlayerPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

/**
 * All triggers here are vanilla's own data-driven criteria (inventory contents, item consumption,
 * block placement, cumulative stats) - no custom {@code CriterionTrigger} is registered, since every
 * requested unlock condition is already expressible with what {@link CriteriaTriggers} ships.
 * <p>
 * Two requested conditions don't have a matching vanilla signal and are approximated:
 * <ul>
 *   <li>"Cocinar" (cook via the Freidora) has no distinct signal from "obtain by any means" - the
 *   {@code CookingContext} the cooking system runs on deliberately excludes the {@code Player}
 *   (see {@code CookingContext}), and villagers also sell finished Banderillas directly. "Primer
 *   Pedido" and "Aprendiz de Ottoke" are therefore keyed on inventory contents (having 1, then 10,
 *   of any finished Banderilla) rather than a true cook-completion event.</li>
 *   <li>"Fanático de Ottoke" (250 Banderillas) is expressed as 250 lifetime uses of any single
 *   Banderilla type (via {@link Stats#ITEM_USED}, which vanilla already increments on every eat),
 *   not a sum across all five types - vanilla's advancement predicates can require several stats
 *   at once (AND) but cannot add them together.</li>
 * </ul>
 * A mod's advancements can only be parented to advancements built earlier in the same {@link #generate}
 * call - {@link Advancement.Builder#build} resolves a {@code parent(ResourceLocation)} through a
 * function that always returns {@code null}, so pointing at a vanilla advancement (e.g.
 * {@code minecraft:husbandry/root}) that this provider never builds throws
 * {@code IllegalStateException("Tried to build incomplete advancement!")}. The tree therefore starts
 * from its own root, reusing an existing vanilla texture as its background per the "no new textures"
 * constraint instead of commissioning tab art.
 */
public final class ModAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {
    private static final ResourceLocation ROOT_BACKGROUND = new ResourceLocation("minecraft", "textures/block/warped_planks.png");
    private static final int FANATIC_THRESHOLD = 250;
    private static final int APPRENTICE_THRESHOLD = 10;

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> writer, ExistingFileHelper existingFileHelper) {
        Advancement root = Advancement.Builder.advancement()
                .display(ModItems.BANDERILLA_CRUDA.get(),
                        title("root"), description("root"),
                        ROOT_BACKGROUND, FrameType.TASK, false, false, false)
                .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                .save(writer, id("root"));

        Advancement freidoraPlaced = Advancement.Builder.advancement()
                .parent(root)
                .display(ModBlocks.FREIDORA.get(),
                        title("freidora_placed"), description("freidora_placed"),
                        null, FrameType.TASK, true, true, false)
                .addCriterion("placed_freidora",
                        ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.FREIDORA.get()))
                .save(writer, id("freidora_placed"));

        Advancement firstOrder = Advancement.Builder.advancement()
                .parent(freidoraPlaced)
                .display(ModItems.BANDERILLA_CLASICA.get(),
                        title("first_order"), description("first_order"),
                        null, FrameType.TASK, true, true, false)
                .addCriterion("has_clasica", hasItem(ModItems.BANDERILLA_CLASICA.get()))
                .addCriterion("has_papa", hasItem(ModItems.BANDERILLA_PAPA.get()))
                .addCriterion("has_picante", hasItem(ModItems.BANDERILLA_PICANTE.get()))
                .addCriterion("has_mozzarella", hasItem(ModItems.BANDERILLA_MOZZARELLA.get()))
                .addCriterion("has_premium", hasItem(ModItems.BANDERILLA_PREMIUM.get()))
                .requirements(RequirementsStrategy.OR)
                .save(writer, id("first_order"));

        Advancement apprentice = Advancement.Builder.advancement()
                .parent(firstOrder)
                .display(ModItems.BANDERILLA_CLASICA.get(),
                        title("apprentice"), description("apprentice"),
                        null, FrameType.TASK, true, true, false)
                .addCriterion("has_10_clasica", hasItemCount(ModItems.BANDERILLA_CLASICA.get(), APPRENTICE_THRESHOLD))
                .addCriterion("has_10_papa", hasItemCount(ModItems.BANDERILLA_PAPA.get(), APPRENTICE_THRESHOLD))
                .addCriterion("has_10_picante", hasItemCount(ModItems.BANDERILLA_PICANTE.get(), APPRENTICE_THRESHOLD))
                .addCriterion("has_10_mozzarella", hasItemCount(ModItems.BANDERILLA_MOZZARELLA.get(), APPRENTICE_THRESHOLD))
                .addCriterion("has_10_premium", hasItemCount(ModItems.BANDERILLA_PREMIUM.get(), APPRENTICE_THRESHOLD))
                .requirements(RequirementsStrategy.OR)
                .save(writer, id("apprentice"));

        Advancement eatPicante = Advancement.Builder.advancement()
                .parent(firstOrder)
                .display(ModItems.BANDERILLA_PICANTE.get(),
                        title("eat_picante"), description("eat_picante"),
                        null, FrameType.CHALLENGE, true, true, false)
                .addCriterion("ate_picante", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BANDERILLA_PICANTE.get()))
                .save(writer, id("eat_picante"));

        Advancement eatMozzarella = Advancement.Builder.advancement()
                .parent(firstOrder)
                .display(ModItems.BANDERILLA_MOZZARELLA.get(),
                        title("eat_mozzarella"), description("eat_mozzarella"),
                        null, FrameType.TASK, true, true, false)
                .addCriterion("ate_mozzarella", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BANDERILLA_MOZZARELLA.get()))
                .save(writer, id("eat_mozzarella"));

        Advancement vipClient = Advancement.Builder.advancement()
                .parent(firstOrder)
                .display(ModItems.BANDERILLA_PREMIUM.get(),
                        title("vip_client"), description("vip_client"),
                        null, FrameType.GOAL, true, true, false)
                .addCriterion("ate_clasica", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BANDERILLA_CLASICA.get()))
                .addCriterion("ate_papa", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BANDERILLA_PAPA.get()))
                .addCriterion("ate_picante", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BANDERILLA_PICANTE.get()))
                .addCriterion("ate_mozzarella", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BANDERILLA_MOZZARELLA.get()))
                .addCriterion("ate_premium", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BANDERILLA_PREMIUM.get()))
                .addCriterion("drank_cola", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.COLA_OTTOKE.get()))
                .addCriterion("drank_te", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.TE_DURAZNO.get()))
                .addCriterion("drank_soda", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.SODA_UVA.get()))
                .addCriterion("drank_agua", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.AGUA_MINERAL.get()))
                .save(writer, id("vip_client"));

        Advancement.Builder.advancement()
                .parent(vipClient)
                .display(ModItems.BANDERILLA_PREMIUM.get(),
                        title("fanatic"), description("fanatic"),
                        null, FrameType.CHALLENGE, true, true, false)
                .addCriterion("ate_250_clasica", eatenAtLeast(ModItems.BANDERILLA_CLASICA.get(), FANATIC_THRESHOLD))
                .addCriterion("ate_250_papa", eatenAtLeast(ModItems.BANDERILLA_PAPA.get(), FANATIC_THRESHOLD))
                .addCriterion("ate_250_picante", eatenAtLeast(ModItems.BANDERILLA_PICANTE.get(), FANATIC_THRESHOLD))
                .addCriterion("ate_250_mozzarella", eatenAtLeast(ModItems.BANDERILLA_MOZZARELLA.get(), FANATIC_THRESHOLD))
                .addCriterion("ate_250_premium", eatenAtLeast(ModItems.BANDERILLA_PREMIUM.get(), FANATIC_THRESHOLD))
                .requirements(RequirementsStrategy.OR)
                .save(writer, id("fanatic"));
    }

    private static InventoryChangeTrigger.TriggerInstance hasItem(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
    }

    private static InventoryChangeTrigger.TriggerInstance hasItemCount(ItemLike item, int count) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(
                ItemPredicate.Builder.item().of(item).withCount(MinMaxBounds.Ints.atLeast(count)).build());
    }

    private static PlayerTrigger.TriggerInstance eatenAtLeast(Item item, int count) {
        PlayerPredicate playerPredicate = PlayerPredicate.Builder.player()
                .addStat(Stats.ITEM_USED.get(item), MinMaxBounds.Ints.atLeast(count))
                .build();
        EntityPredicate entityPredicate = EntityPredicate.Builder.entity()
                .subPredicate(playerPredicate)
                .build();
        return new PlayerTrigger.TriggerInstance(CriteriaTriggers.TICK.getId(), EntityPredicate.wrap(entityPredicate));
    }

    private static String id(String path) {
        return OttokeStreetFood.MODID + ":ottoke/" + path;
    }

    private static Component title(String path) {
        return Component.translatable("advancements.ottoke." + path + ".title");
    }

    private static Component description(String path) {
        return Component.translatable("advancements.ottoke." + path + ".description");
    }
}
