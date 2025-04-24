/*
 * Copyright (c) 2025.  Tobias Schuster
 *
 *  Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *  You may download and share this code with attribution, but you may not modify it or use it for commercial purposes.
 *
 *  To view a copy of this license, visit:
 * https://creativecommons.org/licenses/by-nc-nd/4.0/
 */

package de.t0bx.basecore.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerCoinsChangeEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final int currentCoins;
    private final int oldCoins;
    private final int changedCoins;

    public PlayerCoinsChangeEvent(@NotNull Player who, int currentCoins, int oldCoins, int changedCoins) {
        super(who);
        this.currentCoins = currentCoins;
        this.oldCoins = oldCoins;
        this.changedCoins = changedCoins;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
