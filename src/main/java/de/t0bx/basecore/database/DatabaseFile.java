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

import de.t0bx.basecore.BaseAPI;
import de.t0bx.basecore.json.JsonDocument;

import java.io.File;

public class DatabaseFile {

    private JsonDocument jsonDocument;
    private final File databaseFile;

    public DatabaseFile() {
        this.databaseFile = new File(BaseAPI.getApi().getDataFolder(), "database.json");
    }

    public void createDefault() {
        try {
            this.jsonDocument = JsonDocument.loadDocument(this.databaseFile);

            if (this.jsonDocument == null) {
                this.jsonDocument = new JsonDocument();
                this.jsonDocument.append("host", "localhost");
                this.jsonDocument.append("port", 3306);
                this.jsonDocument.append("username", "root");
                this.jsonDocument.append("password", "password");
                this.jsonDocument.append("database", "database");
            }
            this.jsonDocument.save(this.databaseFile);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getDatabaseHost() {
        this.jsonDocument = JsonDocument.loadDocument(this.databaseFile);
        if (this.jsonDocument != null) {
            return this.jsonDocument.getString("host");
        }
        return null;
    }

    public int getDatabasePort() {
        this.jsonDocument = JsonDocument.loadDocument(this.databaseFile);
        if (this.jsonDocument != null) {
            return this.jsonDocument.getInt("port");
        }
        return 0;
    }

    public String getDatabaseUsername() {
        this.jsonDocument = JsonDocument.loadDocument(this.databaseFile);
        if (this.jsonDocument != null) {
            return this.jsonDocument.getString("username");
        }
        return null;
    }

    public String getDatabasePassword() {
        this.jsonDocument = JsonDocument.loadDocument(this.databaseFile);
        if (this.jsonDocument != null) {
            return this.jsonDocument.getString("password");
        }
        return null;
    }

    public String getDatabaseName() {
        this.jsonDocument = JsonDocument.loadDocument(this.databaseFile);
        if (this.jsonDocument != null) {
            return this.jsonDocument.getString("database");
        }
        return null;
    }
}
