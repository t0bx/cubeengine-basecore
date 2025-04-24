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

import java.util.UUID;

public interface ICoinsProvider {

    int getCoins(UUID uuid);

    void addCoins(UUID uuid, int amount);

    void removeCoins(UUID uuid, int amount);

    void setCoins(UUID uuid, int amount);
}
