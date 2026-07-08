package com.voranix.ottoke.common.cooking.system;

/**
 * How well a specific cook turned out. Pure data - this enum makes no decisions and knows nothing
 * about how a quality gets picked; see {@link CookingQualityEvaluator} for that.
 */
public enum CookingQuality {
    /** The cook barely started, or was interrupted early enough that the result is effectively unchanged. */
    RAW,
    /** A cook that completed without anything noteworthy - the default outcome. */
    NORMAL,
    /** A cook that completed under ideal conditions, however a future evaluator defines "ideal". */
    PERFECT,
    /** A cook that ran well past what it needed. */
    BURNT
}
