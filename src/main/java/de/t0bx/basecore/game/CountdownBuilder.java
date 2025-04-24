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

import de.t0bx.basecore.BaseAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class CountdownBuilder {
    private String message;
    private AtomicInteger countdownTime;
    private List<Integer> executedSeconds;
    private List<Player> executedPlayers;
    private Sound executedSound;
    private Function<Void, Boolean> finish;

    public CountdownBuilder() {
        this.message = "%countdown%";
        this.countdownTime = new AtomicInteger(30);
        this.executedSeconds = List.of(10, 5);
        this.executedPlayers = null;
        this.executedSound = null;
        this.finish = null;
    }

    /**
     * Set the Message that is getting send every executedSeconds
     * default = %countdown%
     * @param message The message use %countdown% for the seconds
     * @return returns the CountdownBuilder
     */
    public CountdownBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Set the Executed Seconds when a message should be send
     * default = 10, 5
     * @param executedSeconds List of Integers when the messages should appear
     * @return returns the CountdownBuilder
     */
    public CountdownBuilder setExecutedSeconds(List<Integer> executedSeconds) {
        this.executedSeconds = executedSeconds;
        return this;
    }


    /**
     * Set the Executed Players who are affected from the Coutdown
     * default = null
     * @param executedPlayers List of Players
     * @return returns the CountdownBuilder
     */
    public CountdownBuilder setExecutedPlayers(List<Player> executedPlayers) {
        this.executedPlayers = executedPlayers;
        return this;
    }

    /**
     * Set the Function what should happen after the countdown is at 0
     * default = null
     * @param finish the Function what should happen
     * @return returns the CountdownBuilder
     */
    public CountdownBuilder setFinish(Function<Void, Boolean> finish) {
        this.finish = finish;
        return this;
    }

    /**
     * Sets the Executed sound what should be when every executed Seconds gets Executed
     * default = null
     * @param executedSound the executed Sound
     * @return returns the CountdownBuilder
     */
    public CountdownBuilder setExecutedSound(Sound executedSound) {
        this.executedSound = executedSound;
        return this;
    }

    /**
     * Sets the Countdowntime how long the Countdown should be
     * default = 30
     * @param countdownTime the Countdowntime
     * @return returns the CountdownBuilder
     */
    public CountdownBuilder setCountdownTime(AtomicInteger countdownTime) {
        this.countdownTime = countdownTime;
        return this;
    }

    /**
     * Starts the Countdown
     * @return the TaskId
     */
    public int startCountdown()  {
        return new BukkitRunnable() {
            public void run() {
                countdownTime.getAndDecrement();

                if (countdownTime.get() == 0) {
                    if (finish != null) {
                        finish.apply(null);
                    }
                    this.cancel();
                    return;
                }

                if (executedSeconds.contains(countdownTime.get())) {
                    if (executedPlayers != null) {
                        executedPlayers.forEach(player -> {
                            player.sendMessage(MiniMessage.miniMessage().deserialize(message.replace("%countdown%", String.valueOf(countdownTime.get()))));
                            if (executedSound != null) {
                                player.playSound(player.getLocation(), executedSound, 0.5F, 1.0F);
                            }
                        });
                    }
                }
            }
        }.runTaskTimer(BaseAPI.getApi(), 20L, 20L).getTaskId();
    }

    /**
     * build countdown from function
     * @param function the countdown, use 'return true;' if countdown is at 0 otherwise 'return false;'
     * @return the TaskId
     */
    public static int build(Function<BukkitRunnable, Boolean> function)  {
        return new BukkitRunnable() {
            public void run() {
                boolean cancelled = function.apply(this);
                if (cancelled) {
                    cancel();
                }
            }
        }.runTaskTimer(BaseAPI.getApi(), 20L, 20L).getTaskId();
    }
}
