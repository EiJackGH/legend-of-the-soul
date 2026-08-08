package com.legendofthesoul.engine;

import com.legendofthesoul.model.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CrashReporter implements Thread.UncaughtExceptionHandler {
    private static volatile Player playerInstance;
    private static final String CRASH_REPORT_FILE = "crash-report.txt";

    /**
     * Set/update the current player instance to be included in case of a crash.
     */
    public static void setPlayer(Player player) {
        playerInstance = player;
    }

    /**
     * UncaughtExceptionHandler callback method.
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            String report = generateReport(t, e);

            // Print error/notice to console
            System.err.println("\n==================================================");
            System.err.println("💥 AN UNEXPECTED CRASH OCCURRED! 💥");
            System.err.println("==================================================");
            System.err.println("A detailed report has been saved to: " + CRASH_REPORT_FILE);
            System.err.println("We apologize for the inconvenience!");
            System.err.println("==================================================\n");

            // Write report to local disk file
            saveReportToFile(report);

        } catch (Exception ex) {
            System.err.println("Failed to completely generate or save the crash report: " + ex.getMessage());
            e.printStackTrace();
        } finally {
            // Clean exit with code 1
            System.exit(1);
        }
    }

    /**
     * Generates a string-formatted report including thread, exception, stack trace, player info, and system environment.
     */
    public static String generateReport(Thread t, Throwable e) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        sb.append("==================================================\n");
        sb.append("                 CRASH REPORT                     \n");
        sb.append("==================================================\n");
        sb.append("Timestamp:    ").append(LocalDateTime.now().format(formatter)).append("\n");
        if (t != null) {
            sb.append("Thread:       ").append(t.getName()).append(" (ID: ").append(t.getId()).append(")\n");
        }
        sb.append("\n[EXCEPTION DETAILS]\n");
        if (e != null) {
            sb.append("Exception:    ").append(e.getClass().getName()).append("\n");
            sb.append("Message:      ").append(e.getMessage()).append("\n");
            sb.append("\nStack Trace:\n");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            sb.append(sw.toString());
        } else {
            sb.append("No Exception provided.\n");
        }

        sb.append("\n[PLAYER STATE]\n");
        Player current = playerInstance;
        if (current != null) {
            sb.append("Name:         ").append(current.getName()).append("\n");
            sb.append("Level:        ").append(current.getLevel()).append("\n");
            sb.append("HP:           ").append(current.getHealth()).append("/").append(current.getMaxHealth()).append("\n");
            sb.append("Attack Power: ").append(current.getAttackPower()).append("\n");
            sb.append("Souls:        ").append(current.getSouls()).append("\n");
        } else {
            sb.append("No player active or registered at the time of crash.\n");
        }

        sb.append("\n[SYSTEM METADATA]\n");
        sb.append("OS Name:      ").append(System.getProperty("os.name")).append("\n");
        sb.append("OS Arch:      ").append(System.getProperty("os.arch")).append("\n");
        sb.append("OS Version:   ").append(System.getProperty("os.version")).append("\n");
        sb.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        sb.append("Java Vendor:  ").append(System.getProperty("java.vendor")).append("\n");
        sb.append("VM Name:      ").append(System.getProperty("java.vm.name")).append("\n");
        sb.append("Available Processors: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
        sb.append("Free Memory (MB):     ").append(Runtime.getRuntime().freeMemory() / (1024 * 1024)).append("\n");
        sb.append("Total Memory (MB):    ").append(Runtime.getRuntime().totalMemory() / (1024 * 1024)).append("\n");
        sb.append("Max Memory (MB):      ").append(Runtime.getRuntime().maxMemory() / (1024 * 1024)).append("\n");
        sb.append("==================================================\n");

        return sb.toString();
    }

    private static void saveReportToFile(String report) {
        try (FileWriter writer = new FileWriter(CRASH_REPORT_FILE)) {
            writer.write(report);
        } catch (IOException ex) {
            System.err.println("Error saving report to file: " + ex.getMessage());
        }
    }
}
