/*
 * Copyright (c) 2025.  Tobias Schuster
 *
 *  Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *  You may download and share this code with attribution, but you may not modify it or use it for commercial purposes.
 *
 *  To view a copy of this license, visit:
 * https://creativecommons.org/licenses/by-nc-nd/4.0/
 */

package de.t0bx.basecore.playerutils.uuid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class UUIDFetcher {
    private static final String MINECRAFT_API_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final String XBOX_API_URL = "https://mcprofile.io/api/v1/bedrock/gamertag/";
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    private static final ConcurrentHashMap<String, UUID> NAME_UUID_CACHE = new ConcurrentHashMap<>();
    private static final Pattern BEDROCK_USERNAME_PATTERN = Pattern.compile("^\\.(.+)$");
    private static final Gson GSON = new GsonBuilder().create();

    public static UUID getUUID(String name) throws Exception {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name darf nicht null oder leer sein");
        }

        if (NAME_UUID_CACHE.containsKey(name)) {
            return NAME_UUID_CACHE.get(name);
        }

        boolean isBedrock = name.startsWith(".");
        String actualName = isBedrock ? name.substring(1) : name;

        if (isBedrock) {
            return fetchBedrockUUID(actualName);
        } else {
            return fetchJavaUUID(actualName);
        }
    }

    public static CompletableFuture<UUID> getUUIDAsync(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getUUID(name);
            } catch (Exception e) {
                throw new RuntimeException("Fehler beim Abrufen der UUID für " + name, e);
            }
        }, EXECUTOR);
    }

    private static UUID fetchJavaUUID(String name) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(MINECRAFT_API_URL + name).openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
                String id = response.get("id").getAsString();

                UUID uuid = UUID.fromString(id.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
                NAME_UUID_CACHE.put(name, uuid);
                NAME_UUID_CACHE.put("." + name, uuid);

                return uuid;
            }
        } else {
            throw new Exception("Konnte UUID für Java-Spieler nicht abrufen: HTTP " + connection.getResponseCode());
        }
    }

    private static UUID fetchBedrockUUID(String name) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(XBOX_API_URL + name).openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
                String id = response.get("floodgateuid").getAsString();

                UUID uuid = UUID.fromString(id);
                NAME_UUID_CACHE.put("." + name, uuid);

                return uuid;
            }
        } else {
            throw new Exception("Konnte UUID für Bedrock-Spieler nicht abrufen: HTTP " + connection.getResponseCode());
        }
    }
}
