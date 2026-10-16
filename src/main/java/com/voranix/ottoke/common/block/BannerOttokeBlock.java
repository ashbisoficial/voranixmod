package com.voranix.ottoke.common.block;

import com.voranix.voranixmod.common.block.HorizontalPanelBlock;

/**
 * Purely decorative - no BlockEntity, no inventory. All placement/rotation behavior lives in
 * {@link HorizontalPanelBlock}; this class exists only to give Banner Ottoke its own registered
 * type, consistent with every other block in this package.
 */
public class BannerOttokeBlock extends HorizontalPanelBlock {
    public BannerOttokeBlock(Properties properties) {
        super(properties);
    }
}
