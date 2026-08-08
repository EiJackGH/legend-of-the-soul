package com.legendofthesoul.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.legendofthesoul.model.Player;

import java.io.File;
import java.io.IOException;

public class SaveManager {
    private static final String SAVE_FILE = "savegame.json";
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static boolean saveGame(Player player) {
        try {
            mapper.writeValue(new File(SAVE_FILE), player);
            System.out.println("💾 Progress saved to " + SAVE_FILE);
            return true;
        } catch (IOException e) {
            System.err.println("❌ Failed to save game: " + e.getMessage());
            return false;
        }
    }

    public static Player loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("⚠️ No save file found. Starting fresh.");
            return null;
        }

        try {
            Player player = mapper.readValue(file, Player.class);
            System.out.println("📂 Loaded save profile for " + player.getName() + "!");
            return player;
        } catch (IOException e) {
            System.err.println("❌ Failed to parse save file: " + e.getMessage());
            return null;
        }
    }
}
