package com.legendofthesoul.engine;

import com.legendofthesoul.model.Player;

import java.io.*;

public class SaveManager {
    private static final String SAVE_FILE = "savegame.dat";

    /**
     * Serializes player state to local disk.
     */
    public static boolean saveGame(Player player) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(player);
            System.out.println("💾 Progress successfully saved to " + SAVE_FILE);
            return true;
        } catch (IOException e) {
            System.err.println("❌ Failed to save progress: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deserializes player state from disk if present.
     */
    public static Player loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("⚠️ No save file found (" + SAVE_FILE + "). Starting fresh game.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Player loadedPlayer = (Player) ois.readObject();
            System.out.println("📂 Loaded save file! Welcome back, " + loadedPlayer.getName() + ".");
            return loadedPlayer;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Failed to load save file (corrupted or incompatible): " + e.getMessage());
            return null;
        }
    }
}
