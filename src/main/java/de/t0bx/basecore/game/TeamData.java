/*
 * Copyright (c) 2025.  Tobias Schuster
 *
 *  Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *  You may download and share this code with attribution, but you may not modify it or use it for commercial purposes.
 *
 *  To view a copy of this license, visit:
 * https://creativecommons.org/licenses/by-nc-nd/4.0/
 */

package de.t0bx.basecore.game;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeamData {
    @Getter
    private String name;

    @Setter
    private Component displayName;

    private NamedTextColor color;

    @Getter
    @Setter
    private int maxPlayers;

    private List<Player> players;

    @Getter
    private Team scoreboardTeam;
    private Map<String, Object> customData;

    @Getter
    private boolean friendlyFire;

    @Getter
    @Setter
    private int score;

    private Scoreboard scoreboard;

    public TeamData(String name, Component displayName, NamedTextColor color, int maxPlayers, Team scoreboardTeam, Scoreboard scoreboard) {
        this.name = name;
        this.displayName = displayName;
        this.color = color;
        this.maxPlayers = maxPlayers;
        this.scoreboardTeam = scoreboardTeam;
        this.scoreboard = scoreboard;
    }


    public boolean addPlayer(Player player) {
        if (this.players.size() >= maxPlayers) {
            return false;
        }

        removePlayer(player);

        this.players.add(player);
        this.scoreboardTeam.addEntry(player.getName());

        player.customName(MiniMessage.miniMessage().deserialize(this.color + player.getName()));

        player.setScoreboard(scoreboard);

        return true;
    }

    public boolean removePlayer(Player player) {
        if (!this.players.contains(player)) {
            return false;
        }

        players.remove(player);
        scoreboardTeam.removeEntry(player.getName());

        return true;
    }

    public boolean containsPlayer(Player player) {
        return this.players.contains(player);
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
        this.scoreboardTeam.setAllowFriendlyFire(friendlyFire);
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void setCustomData(String key, Object value) {
        this.customData.put(key, value);
    }

    public Object getCustomData(String key) {
        return this.customData.get(key);
    }

    public String getDisplayName() {
        return this.displayName.toString();
    }

    public NamedTextColor getChatColor() {
        return color;
    }

    public void setChatColor(NamedTextColor color) {
        this.color = color;
        scoreboardTeam.color(color);

        for (Player player : players) {
            player.customName(MiniMessage.miniMessage().deserialize(color + player.getName()));
        }
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public int getPlayerCount() {
        return players.size();
    }

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }
}
