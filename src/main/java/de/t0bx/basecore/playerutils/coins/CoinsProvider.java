/*
 * Copyright (c) 2025.  Tobias Schuster
 *
 *  Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *  You may download and share this code with attribution, but you may not modify it or use it for commercial purposes.
 *
 *  To view a copy of this license, visit:
 * https://creativecommons.org/licenses/by-nc-nd/4.0/
 */

package de.t0bx.basecore.playerutils.coins;

import de.t0bx.basecore.BaseAPI;
import de.t0bx.basecore.database.IMySQLManager;
import de.t0bx.basecore.event.PlayerCoinsChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class CoinsProvider implements ICoinsProvider {

    private final ConcurrentHashMap<UUID, Integer> coinsCache;

    private final IMySQLManager mySQLManager;

    public CoinsProvider() {
        this.coinsCache = new ConcurrentHashMap<>();
        this.mySQLManager = BaseAPI.getApi().getMySQLManager();
    }

    @Override
    public int getCoins(UUID uuid) {
        if (this.coinsCache.containsKey(uuid)) {
            return this.coinsCache.get(uuid);
        }

        String sql = "SELECT coins FROM player_coins WHERE uuid = ?";

        this.mySQLManager.queryAsync(sql, resultSet -> {
            try {
                return resultSet.getInt("coins");
            } catch (SQLException exception) {
                BaseAPI.getApi().getLogger().severe(exception.getMessage());
                return 0;
            }
        }, uuid.toString()).thenApply(coins -> {
           if (!coins.isEmpty()) {
               return coins.getFirst();
           } else {
               return 0;
           }
        });
        return 0;
    }

    @Override
    public void addCoins(UUID uuid, int amount) {
        int currentCoins = this.getCoins(uuid);
        int newCoins = currentCoins + amount;
        this.coinsCache.put(uuid, newCoins);

        this.savePlayer(uuid);

        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            PlayerCoinsChangeEvent event = new PlayerCoinsChangeEvent(player, newCoins, currentCoins, amount);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @Override
    public void removeCoins(UUID uuid, int amount) {
        int currentCoins = this.getCoins(uuid);
        int newCoins = currentCoins - amount;
        this.coinsCache.put(uuid, newCoins);

        this.savePlayer(uuid);

        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            PlayerCoinsChangeEvent event = new PlayerCoinsChangeEvent(player, newCoins, currentCoins, amount);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @Override
    public void setCoins(UUID uuid, int amount) {
        int currentCoins = this.getCoins(uuid);
        this.coinsCache.put(uuid, amount);

        this.savePlayer(uuid);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            PlayerCoinsChangeEvent event = new PlayerCoinsChangeEvent(player, amount, currentCoins, amount);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    public void loadPlayer(UUID uuid) {
        if (this.coinsCache.containsKey(uuid)) return;

        this.doesPlayerExist(uuid).thenAccept(playerExist -> {
           if (playerExist) {
               String sql = "SELECT coins FROM player_coins WHERE uuid = ?";

               this.mySQLManager.queryAsync(sql, resultSet -> {
                   try {
                       this.coinsCache.put(uuid, resultSet.getInt("coins"));
                   } catch (SQLException exception) {
                       throw new RuntimeException(exception);
                   }
                   return null;
               }, uuid.toString());
           } else {
               this.insertNewPlayer(uuid);
               this.coinsCache.put(uuid, 0);
           }
        });
    }

    private CompletableFuture<Boolean> doesPlayerExist(UUID uuid) {
        String sql = "SELECT uuid FROM player_coins WHERE uuid = ?";

        return this.mySQLManager.queryAsync(sql, resultSet -> {
            try {
                return resultSet.getString("uuid");
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }, uuid.toString()).thenApply(result -> {
           if (!result.isEmpty()) {
               return result.getFirst().equals(uuid.toString());
           }
           return false;
        });
    }

    private void insertNewPlayer(UUID uuid) {
        String sql = "INSERT INTO player_coins(uuid, coins) VALUES(?, ?)";

        this.mySQLManager.updateAsync(sql, uuid.toString(), 0);
    }

    public void savePlayer(UUID uuid) {
        if (!this.coinsCache.containsKey(uuid)) return;

        String sql = "UPDATE player_coins SET coins = ? WHERE uuid = ?";
        this.mySQLManager.updateAsync(sql, this.coinsCache.get(uuid), uuid.toString());
    }

    public void saveAllPlayers() {
        for (UUID uuid : this.coinsCache.keySet()) {
            this.savePlayer(uuid);
        }
    }
}
