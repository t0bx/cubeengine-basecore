/*
 * Copyright (c) 2025.  Tobias Schuster
 *
 *  Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *  You may download and share this code with attribution, but you may not modify it or use it for commercial purposes.
 *
 *  To view a copy of this license, visit:
 * https://creativecommons.org/licenses/by-nc-nd/4.0/
 */

package de.t0bx.basecore.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommandBase implements CommandExecutor, TabCompleter {

    protected final JavaPlugin plugin;

    private final String commandName;

    public AbstractCommandBase(JavaPlugin plugin, String commandName) {
        this.plugin = plugin;
        this.commandName = commandName;

        plugin.getCommand(commandName).setExecutor(this);
        plugin.getCommand(commandName).setTabCompleter(this);
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(commandName)) {
            return execute(sender, args);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase(commandName)) {
            return tabComplete(sender, args);
        }
        return new ArrayList<>();
    }

    protected boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }
}
