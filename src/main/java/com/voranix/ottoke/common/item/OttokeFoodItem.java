package com.voranix.ottoke.common.item;

import com.voranix.voranixmod.common.item.FoodDefinition;
import com.voranix.voranixmod.common.item.FoodEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * The one Item subclass every edible item in the mod uses. Takes its {@link FoodDefinition} lazily
 * (a Supplier, not a value) because registration runs before Forge guarantees config is loaded -
 * a food whose hunger/saturation come from OttokeConfig can't resolve those values yet at the point
 * where its RegistryObject supplier runs. Evaluating lazily, inside getFoodProperties(), defers that
 * read until the item is actually used in a loaded game, when config is guaranteed available.
 * <p>
 * A food with fixed, non-config values can use the {@link #OttokeFoodItem(FoodCategory, FoodDefinition, Item.Properties)}
 * convenience constructor below instead of writing its own trivial supplier.
 * <p>
 * {@link #category} exists so generic UI (see {@link FoodCategory#getItems()}, used by the Menu
 * board screen) can list "every Banderilla" or "every Bebida" without maintaining a separate index -
 * it is intentionally a required constructor argument, not optional, so no food can be registered
 * without being classified.
 */
public class OttokeFoodItem extends Item {
    private final FoodCategory category;
    private final Supplier<FoodDefinition> definition;

    public OttokeFoodItem(FoodCategory category, Supplier<FoodDefinition> definition, Item.Properties properties) {
        super(properties);
        this.category = category;
        this.definition = definition;
    }

    public OttokeFoodItem(FoodCategory category, FoodDefinition definition, Item.Properties properties) {
        this(category, () -> definition, properties);
    }

    public FoodCategory getCategory() {
        return this.category;
    }

    @Override
    public boolean isEdible() {
        return true;
    }

    @Override
    public FoodProperties getFoodProperties(ItemStack stack, LivingEntity entity) {
        return this.definition.get().toFoodProperties();
    }

    /**
     * Reuses {@link PotionUtils#addPotionTooltip} - the same formatting vanilla's own Suspicious Stew
     * uses - instead of hand-rolling tick-to-"m:ss" conversion: it already renders "Name (m:ss)" (or
     * "Name II (m:ss)" past amplifier 0), colored per effect category, from a plain
     * {@code List<MobEffectInstance>}. Skipped entirely when this food has no {@link FoodEffect}s
     * (Clasica, Papa) - {@code addPotionTooltip} would otherwise add its own "No Effects" line, which
     * these foods never showed before and shouldn't start showing now.
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        List<FoodEffect> effects = this.definition.get().effects();
        if (effects.isEmpty()) {
            return;
        }
        List<MobEffectInstance> instances = effects.stream().map(FoodEffect::effect).map(Supplier::get).toList();
        PotionUtils.addPotionTooltip(instances, tooltip, 1.0F);
    }
}
