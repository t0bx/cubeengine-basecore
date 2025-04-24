/*
 * Copyright (c) 2025.  Tobias Schuster
 *
 *  Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *  You may download and share this code with attribution, but you may not modify it or use it for commercial purposes.
 *
 *  To view a copy of this license, visit:
 * https://creativecommons.org/licenses/by-nc-nd/4.0/
 */

package de.t0bx.basecore.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface IMySQLManager {

    void connect() throws SQLException;

    void disconnect() throws SQLException;

    void update(String query, Object... params) throws SQLException;

    CompletableFuture<Void> updateAsync(String query, Object... params);

    <T> List<T> query(String query, Function<ResultSet, T> resultHandler, Object... params) throws SQLException;

    <T> CompletableFuture<List<T>> queryAsync(String query, Function<ResultSet, T> resultHandler, Object... params);

}
