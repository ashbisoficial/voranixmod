package com.voranix.ottoke.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

/**
 * An {@link OttokeFoodItem} that is drunk rather than eaten and returns a container item when
 * finished - the same pattern vanilla uses for {@code HoneyBottle} (food properties + a drink
 * animation + a container swapped in on finish), not a new mechanic invented for this mod.
 * <p>
 * Reuses {@link OttokeFoodItem#getFoodProperties} and {@code isEdible()} as-is: hunger/saturation
 * still come from a {@link FoodDefinition}, exactly like solid food. Only the use animation and what
 * happens when the use finishes differ.
 */
public class OttokeDrinkItem extends OttokeFoodItem {
    private final Item container;

    public OttokeDrinkItem(FoodCategory category, Supplier<FoodDefinition> definition, Item container, Properties properties) {
        super(category, definition, properties);
        this.container = container;
    }

    public OttokeDrinkItem(FoodCategory category, FoodDefinition definition, Item container, Properties properties) {
        super(category, definition, properties);
        this.container = container;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            return ItemUtils.createFilledResult(result, player, new ItemStack(this.container));
        }
        return result;
    }
}
