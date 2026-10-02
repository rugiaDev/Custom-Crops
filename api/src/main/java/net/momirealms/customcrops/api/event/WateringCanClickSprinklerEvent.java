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

import net.momirealms.customcrops.api.core.mechanic.sprinkler.SprinklerConfig;
import net.momirealms.customcrops.api.core.mechanic.wateringcan.WateringCanConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Giraffe fork (2026-10-02): fired when a watering can is used on a sprinkler, before any sprinkler handling.
 * <p>Fired on the main thread. Cancelling it makes the click <b>not</b> count as a sprinkler click — the watering can
 * then continues with its normal handling (fill methods, then the fluid ray trace), e.g. filling the can from
 * water under the sprinkler. Not cancelled = the original behaviour (water the sprinkler).
 */
public class WateringCanClickSprinklerEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final ItemStack itemInHand;
    private final EquipmentSlot hand;
    private final WateringCanConfig wateringCanConfig;
    private final SprinklerConfig sprinklerConfig;
    private final Location location;
    private final int waterInCan;
    private final int sprinklerWater;

    public WateringCanClickSprinklerEvent(
            @NotNull Player player,
            @NotNull ItemStack itemInHand,
            @NotNull EquipmentSlot hand,
            @NotNull WateringCanConfig wateringCanConfig,
            @NotNull SprinklerConfig sprinklerConfig,
            @NotNull Location location,
            int waterInCan,
            int sprinklerWater
    ) {
        super(player);
        this.itemInHand = itemInHand;
        this.hand = hand;
        this.wateringCanConfig = wateringCanConfig;
        this.sprinklerConfig = sprinklerConfig;
        this.location = location;
        this.waterInCan = waterInCan;
        this.sprinklerWater = sprinklerWater;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    @NotNull
    public ItemStack itemInHand() {
        return itemInHand;
    }

    @NotNull
    public EquipmentSlot hand() {
        return hand;
    }

    @NotNull
    public WateringCanConfig wateringCanConfig() {
        return wateringCanConfig;
    }

    @NotNull
    public SprinklerConfig sprinklerConfig() {
        return sprinklerConfig;
    }

    /** The sprinkler's block location. */
    @NotNull
    public Location location() {
        return location;
    }

    /** Water currently in the watering can. */
    public int waterInCan() {
        return waterInCan;
    }

    /** Water currently in the sprinkler. */
    public int sprinklerWater() {
        return sprinklerWater;
    }
}
