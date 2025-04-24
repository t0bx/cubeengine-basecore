/*
 * Copyright (c) 2025.  Tobias Schuster
 *
 *  Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *  You may download and share this code with attribution, but you may not modify it or use it for commercial purposes.
 *
 *  To view a copy of this license, visit:
 *  https://creativecommons.org/licenses/by-nc-nd/4.0/
 */

package de.t0bx.basecore;

import de.t0bx.basecore.database.DatabaseFile;
import de.t0bx.basecore.listener.PlayerJoinListener;
import de.t0bx.basecore.playerutils.coins.CoinsProvider;
import de.t0bx.basecore.playerutils.coins.ICoinsProvider;
import de.t0bx.basecore.database.IMySQLManager;
import de.t0bx.basecore.database.MySQLManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

@Getter
public final class BaseAPI extends JavaPlugin {

    @Getter
    private static BaseAPI api;

    private DatabaseFile databaseFile;

    private IMySQLManager mySQLManager;

    private ICoinsProvider coinsProvider;

    @Override
    public void onEnable() {
        api = this;
        this.getLogger().info("Loading BaseCore...");

        this.databaseFile = new DatabaseFile();
        this.databaseFile.createDefault();

        this.getLogger().info("Trying to connect to database...");
        this.mySQLManager = new MySQLManager(this.databaseFile.getDatabaseHost(),
                this.databaseFile.getDatabasePort(),
                this.databaseFile.getDatabaseUsername(),
                this.databaseFile.getDatabasePassword(),
                this.getDatabaseFile().getDatabaseName()
        );

        try {
            this.mySQLManager.connect();
            this.createDefaultSQLTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.coinsProvider = new CoinsProvider();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);

        this.getLogger().info("BaseCore started. Version >> " + this.getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        try {
            this.mySQLManager.disconnect();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void createDefaultSQLTable() {
        String coinsTable = "CREATE TABLE IF NOT EXISTS player_coins(uuid VARCHAR(36) PRIMARY KEY, coins INT(100) NOT NULL)";
        this.mySQLManager.updateAsync(coinsTable);
    }
}
