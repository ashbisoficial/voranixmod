package com.voranix.maipizza.common.item;

import com.voranix.voranixmod.common.item.CategorizedFood;
import com.voranix.voranixmod.common.item.FoodDefinition;
import com.voranix.voranixmod.common.item.FoodEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * Mai Pizza's edible item, mirroring {@code com.voranix.ottoke.common.item.OttokeFoodItem} exactly -
 * see that class for the full rationale (lazy {@link FoodDefinition} supplier for config-driven
 * values, {@link #category} required at construction, potion-tooltip reuse for effects).
 */
public class MaiPizzaFoodItem extends Item implements CategorizedFood<MaiPizzaCategory> {
    private final MaiPizzaCategory category;
    private final Supplier<FoodDefinition> definition;

    public MaiPizzaFoodItem(MaiPizzaCategory category, Supplier<FoodDefinition> definition, Item.Properties properties) {
        super(properties);
        this.category = category;
        this.definition = definition;
    }

    public MaiPizzaFoodItem(MaiPizzaCategory category, FoodDefinition definition, Item.Properties properties) {
        this(category, () -> definition, properties);
    }

    public MaiPizzaCategory getCategory() {
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
