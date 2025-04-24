/*
 * Copyright (c) 2025.  Tobias Schuster
 *
 *  Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *  You may download and share this code with attribution, but you may not modify it or use it for commercial purposes.
 *
 *  To view a copy of this license, visit:
 * https://creativecommons.org/licenses/by-nc-nd/4.0/
 */

package de.t0bx.basecore.listener;

import de.t0bx.basecore.BaseAPI;
import de.t0bx.basecore.playerutils.scoreboard.ScoreboardBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ScoreboardBuilder scoreboardBuilder = new ScoreboardBuilder(player, Bukkit.getScoreboardManager().getNewScoreboard());

        if (player.hasMetadata("score")) {
            player.removeMetadata("score", BaseAPI.getApi());
        }

        player.setMetadata("score", new FixedMetadataValue(BaseAPI.getApi(), scoreboardBuilder));
    }
}
