/*
 * Copyright (c) 2025.  Tobias Schuster
 *
 *  Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *  You may download and share this code with attribution, but you may not modify it or use it for commercial purposes.
 *
 *  To view a copy of this license, visit:
 * https://creativecommons.org/licenses/by-nc-nd/4.0/
 */

package de.t0bx.basecore.playerutils.scoreboard;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ScoreboardBuilder {

    private final Scoreboard scoreboard;
    private final Player player;
    private Objective objective;
    @Getter
    private final static Map<Player, ScoreboardBuilder> scoreboardBuilders = new HashMap<>();

    public ScoreboardBuilder(Player player, Scoreboard scoreboard) {
        this.player = player;
        this.scoreboard = scoreboard;

        scoreboardBuilders.remove(player);

        scoreboardBuilders.put(player, this);
        this.player.setScoreboard(this.scoreboard);
    }

    public void createSidebar(String displayName) {
        if (this.scoreboard.getObjective("display") != null) {
            this.scoreboard.getObjective("display").unregister();
        }

        this.objective = this.scoreboard.registerNewObjective("display", "dummy", MiniMessage.miniMessage().deserialize(displayName));
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }


    public void setDisplayName(String displayName) {
        this.scoreboard.getObjective("display").displayName(MiniMessage.miniMessage().deserialize(displayName));
    }

    public void setLine(String content, int line){
        Team team = getTeamByLine(line);

        if(team == null) return;

        team.prefix(MiniMessage.miniMessage().deserialize(content));
        this.showLine(line);
    }

    public void removeLine(int line){
        this.hideLine(line);
    }

    public Team getTeamByLine(int line) {
        EntryName name = EntryName.getEntryNameByScore(line);

        if(name == null) return null;

        Team team = this.scoreboard.getEntryTeam(name.getEntryName());

        if(team != null) return team;

        team = this.scoreboard.registerNewTeam(name.name());
        team.addEntry(name.getEntryName());

        return team;
    }


    public void showLine(int line) {
        EntryName name = EntryName.getEntryNameByScore(line);

        if (name == null) return;

        if (this.objective.getScore(name.getEntryName()).isScoreSet()) return;

        this.objective.getScore(name.getEntryName()).setScore(line);
    }

    public void hideLine(int line) {
        EntryName name = EntryName.getEntryNameByScore(line);

        if (name == null) return;

        if (this.objective.getScore(name.getEntryName()).isScoreSet()) return;

        this.scoreboard.resetScores(name.getEntryName());
    }

    public Team getOrCreateTeam(String teamName) {
        Team team = this.scoreboard.getTeam(teamName);
        if (team != null) return team;
        return this.scoreboard.registerNewTeam(teamName);
    }

    public void setTab(Player player, Component prefix, Component suffix, String teamName, String displayName) {
        Team team = this.getOrCreateTeam(teamName);
        if (prefix != null) team.prefix(prefix);
        if (suffix != null) team.suffix(suffix);
        team.color(NamedTextColor.GRAY);
        team.addEntry(player.getName());
        player.customName(MiniMessage.miniMessage().deserialize(displayName + player.getName() + "</gradient>"));
        player.displayName(team.prefix().append(MiniMessage.miniMessage().deserialize("<grey>" + player.getName())));
    }
}
