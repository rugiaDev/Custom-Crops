/*
 *  Copyright (C) <2024> <XiaoMoMi>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.momirealms.customcrops.api.event;

import net.momirealms.customcrops.api.core.block.PotBlock;
import net.momirealms.customcrops.api.core.mechanic.pot.PotConfig;
import net.momirealms.customcrops.api.core.mechanic.sprinkler.SprinklerConfig;
import net.momirealms.customcrops.api.core.world.CustomCropsBlockState;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Giraffe fork (2026-09-15): fired for every pot a working sprinkler is about to water.
 * <p>Fired on the main thread, right before the water is added. Cancelling it skips watering that pot.
 * Listeners may change the pot state (e.g. add a fertilizer with {@link PotBlock#addFertilizer})
 * and update its appearance themselves.
 */
public class SprinklerWaterPotEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Location sprinklerLocation;
    private final CustomCropsBlockState sprinklerState;
    private final SprinklerConfig sprinklerConfig;
    private final Location potLocation;
    private final PotBlock potBlock;
    private final CustomCropsBlockState potState;
    private final PotConfig potConfig;

    public SprinklerWaterPotEvent(
            @NotNull Location sprinklerLocation,
            @NotNull CustomCropsBlockState sprinklerState,
            @NotNull SprinklerConfig sprinklerConfig,
            @NotNull Location potLocation,
            @NotNull PotBlock potBlock,
            @NotNull CustomCropsBlockState potState,
            @NotNull PotConfig potConfig
    ) {
        this.sprinklerLocation = sprinklerLocation;
        this.sprinklerState = sprinklerState;
        this.sprinklerConfig = sprinklerConfig;
        this.potLocation = potLocation;
        this.potBlock = potBlock;
        this.potState = potState;
        this.potConfig = potConfig;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /** @return the location of the working sprinkler */
    @NotNull
    public Location sprinklerLocation() {
        return sprinklerLocation;
    }

    /** @return the block state of the working sprinkler */
    @NotNull
    public CustomCropsBlockState sprinklerState() {
        return sprinklerState;
    }

    /** @return the config of the working sprinkler */
    @NotNull
    public SprinklerConfig sprinklerConfig() {
        return sprinklerConfig;
    }

    /** @return the location of the pot being watered */
    @NotNull
    public Location potLocation() {
        return potLocation;
    }

    /** @return the pot block mechanic, for calling pot APIs on {@link #potState()} */
    @NotNull
    public PotBlock potBlock() {
        return potBlock;
    }

    /** @return the block state of the pot being watered */
    @NotNull
    public CustomCropsBlockState potState() {
        return potState;
    }

    /** @return the config of the pot being watered */
    @NotNull
    public PotConfig potConfig() {
        return potConfig;
    }
}
