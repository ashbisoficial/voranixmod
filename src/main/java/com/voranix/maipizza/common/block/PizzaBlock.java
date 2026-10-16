package com.voranix.maipizza.common.block;

import com.voranix.maipizza.common.item.MaiPizzaCategory;
import com.voranix.voranixmod.common.block.SliceableFoodBlock;
import com.voranix.voranixmod.common.item.CategorizedFood;
import com.voranix.voranixmod.common.item.FoodDefinition;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

/**
 * A cooked Mai Pizza, placed and eaten slice by slice via {@link SliceableFoodBlock} - the
 * block-backed counterpart to {@code MaiPizzaFoodItem}. One class instantiated once per pizza
 * variant in {@code ModBlocks}, mirroring how {@code MaiPizzaFoodItem} used to be instantiated once
 * per variant in the old, Item-based {@code ModItems}.
 */
public class PizzaBlock extends SliceableFoodBlock implements CategorizedFood<MaiPizzaCategory> {
    private final MaiPizzaCategory category;
    private final Supplier<FoodDefinition> definition;

    public PizzaBlock(MaiPizzaCategory category, Supplier<FoodDefinition> definition, BlockBehaviour.Properties properties) {
        super(properties);
        this.category = category;
        this.definition = definition;
    }

    public PizzaBlock(MaiPizzaCategory category, FoodDefinition definition, BlockBehaviour.Properties properties) {
        this(category, () -> definition, properties);
    }

    @Override
    public MaiPizzaCategory getCategory() {
        return this.category;
    }

    @Override
    protected Supplier<FoodDefinition> getFoodDefinition() {
        return this.definition;
    }
}
