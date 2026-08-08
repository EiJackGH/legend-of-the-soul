package com.legendofthesoul.engine;

import com.legendofthesoul.model.Player;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class CrashReporter implements Thread.UncaughtExceptionHandler {
    private static Player player;

    public static void register() {
        Thread.setDefaultUncaughtExceptionHandler(new CrashReporter());
    }

    public static void setPlayer(Player p) {
        player = p;
    }

    public static String generateReport(Thread t, Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LEGEND OF THE SOUL CRASH REPORT ===\n");
        sb.append("Timestamp: ").append(LocalDateTime.now()).append("\n");
        if (t != null) {
            sb.append("Thread: ").append(t.getName()).append("\n");
        } else {
            sb.append("Thread: Unknown\n");
        }

        sb.append("\n=== PLAYER STATE ===\n");
        if (player == null) {
            sb.append("No player active or registered at the time of crash.\n");
        } else {
            sb.append("Name:         ").append(player.getName()).append("\n");
            sb.append("Level:        ").append(player.getLevel()).append("\n");
            sb.append("HP:           ").append(player.getHealth()).append("/").append(player.getMaxHealth()).append("\n");
            sb.append("Attack Power: ").append(player.getAttackPower()).append("\n");
            sb.append("Souls:        ").append(player.getSouls()).append("\n");
        }

        sb.append("\n=== EXCEPTION INFO ===\n");
        if (e == null) {
            sb.append("No Exception provided.\n");
        } else {
            sb.append("Exception: ").append(e.toString()).append("\n");
            sb.append("Message: ").append(e.getMessage()).append("\n");
            sb.append("\nStack Trace:\n");
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            sb.append(sw.toString());
        }

        sb.append("\n=== SYSTEM METADATA ===\n");
        sb.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        sb.append("OS Name: ").append(System.getProperty("os.name")).append("\n");
        sb.append("OS Arch: ").append(System.getProperty("os.arch")).append("\n");

        return sb.toString();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String filename = "crash-report-" + System.currentTimeMillis() + ".txt";
        System.err.println("\n💥 FATAL GAME CRASH ENCOUNTERED!");
        System.err.println("Writing crash stacktrace to " + filename + "...");

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.print(generateReport(t, e));
            System.err.println("Report saved successfully.");
        } catch (IOException ioException) {
            System.err.println("Failed to write crash report: " + ioException.getMessage());
        }

        System.exit(1);
    }
}
