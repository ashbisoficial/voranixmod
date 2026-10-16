package com.voranix.maipizza.common.block;

import com.voranix.voranixmod.common.block.HorizontalPanelBlock;

/**
 * Purely decorative - mirrors {@code com.voranix.ottoke.common.block.CartelBlock}. Its own type
 * (rather than registering {@link HorizontalPanelBlock} directly) is kept for consistency with every
 * other block in this package.
 */
public class CartelBlock extends HorizontalPanelBlock {
    public CartelBlock(Properties properties) {
        super(properties);
    }
}
