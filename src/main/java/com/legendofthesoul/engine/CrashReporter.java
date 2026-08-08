package com.legendofthesoul.engine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class CrashReporter implements Thread.UncaughtExceptionHandler {

    public static void register() {
        Thread.setDefaultUncaughtExceptionHandler(new CrashReporter());
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String filename = "crash-report-" + System.currentTimeMillis() + ".txt";
        System.err.println("\n💥 FATAL GAME CRASH ENCOUNTERED!");
        System.err.println("Writing crash stacktrace to " + filename + "...");

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=== LEGEND OF THE SOUL CRASH REPORT ===");
            writer.println("Timestamp: " + LocalDateTime.now());
            writer.println("Thread: " + t.getName());
            writer.println("Exception: " + e.toString());
            writer.println("\nStack Trace:");
            e.printStackTrace(writer);
            System.err.println("Report saved successfully.");
        } catch (IOException ioException) {
            System.err.println("Failed to write crash report: " + ioException.getMessage());
        }

        System.exit(1);
    }
}
