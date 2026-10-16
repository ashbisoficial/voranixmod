package com.voranix.voranixmod.common.cooking.system;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Everything needed to run one cooking station's cook cycle, bundled into a single value instead of
 * threaded through {@link CookingSystem#tick} as separate parameters.
 * <p>
 * {@code station} is the {@link CookingStation} itself rather than its container/process/manager
 * pulled apart - if {@link CookingStation}'s contract grows a new capability later, this context
 * doesn't need to change shape to keep exposing it.
 * <p>
 * Why this pays off for stations and recipes that don't exist yet:
 * <ul>
 *     <li><b>Adding context later doesn't break every call site.</b> Right now nothing needs more
 *     than level/pos/station, but a Parrilla checking for a heat source underneath it, a recipe
 *     that varies by light level, or a chance-based recipe needing a {@code RandomSource} are all
 *     realistic next asks. Each becomes one new field here, and every existing caller (which
 *     already just builds and passes a {@code CookingContext}) keeps compiling unchanged. Without
 *     this type, each of those would mean changing the signature of
 *     {@link CookingSystem#tick}, {@link AbstractCookingManager#tick}, and every concrete manager's
 *     hooks, across every station, at once.</li>
 *     <li><b>{@link BlockPos} is already first-class even though nothing reads it yet.</b> Sound
 *     effects when a cook finishes, particles, checking a neighboring block - almost every next
 *     cooking feature needs a position. It is available from day one instead of being bolted on
 *     piecemeal the first time one of those features is implemented.</li>
 *     <li><b>Recipes gain a stable extension point.</b> {@link CookingRecipe#matches(CookingContainer)}
 *     only ever sees the container today. If matching ever needs to depend on where or in what
 *     world it's happening, there is already one established type to add to that signature instead
 *     of accumulating raw parameters on the interface.</li>
 * </ul>
 * <p>
 * {@code Player} is deliberately not a field yet. Level, pos, and station are always available
 * whenever a station ticks - a block sitting in a loaded chunk cooks whether or not anyone is
 * watching it. A player has no natural owner in that ambient tick cycle (only explicit interactions
 * like opening the station's menu have one); adding it now would force every tick-time construction
 * of this context to answer "which player, or null?" for a concept the tick loop doesn't actually
 * have yet.
 */
public record CookingContext(Level level, BlockPos pos, CookingStation station) {
}
