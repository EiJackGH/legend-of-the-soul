package com.legendofthesoul.engine;

import com.legendofthesoul.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CrashReporterTest {

    @BeforeEach
    public void setup() {
        // Clear registered player before each test
        CrashReporter.setPlayer(null);
    }

    @Test
    public void testGenerateReportWithoutPlayer() {
        Thread thread = new Thread("Test-Thread-1");
        Throwable exception = new RuntimeException("Something went wrong!");

        String report = CrashReporter.generateReport(thread, exception);

        assertNotNull(report);
        assertTrue(report.contains("CRASH REPORT"));
        assertTrue(report.contains("Test-Thread-1"));
        assertTrue(report.contains("RuntimeException"));
        assertTrue(report.contains("Something went wrong!"));
        assertTrue(report.contains("No player active or registered at the time of crash."));
        assertTrue(report.contains("SYSTEM METADATA"));
    }

    @Test
    public void testGenerateReportWithPlayer() {
        Player player = new Player("Arthur");
        player.addSouls(75); // Should level up once (Arthur level up to level 2 requires level * 50 = 50 souls)
        CrashReporter.setPlayer(player);

        Thread thread = new Thread("Test-Thread-2");
        Throwable exception = new IllegalArgumentException("Invalid argument!");

        String report = CrashReporter.generateReport(thread, exception);

        assertNotNull(report);
        assertTrue(report.contains("CRASH REPORT"));
        assertTrue(report.contains("Test-Thread-2"));
        assertTrue(report.contains("IllegalArgumentException"));
        assertTrue(report.contains("Invalid argument!"));

        // Assert player state is detailed in report
        assertTrue(report.contains("Name:         Arthur"));
        assertTrue(report.contains("Level:        2"));
        assertTrue(report.contains("HP:"));
        assertTrue(report.contains("Attack Power:"));
        assertTrue(report.contains("Souls:        25")); // 75 - 50 = 25
        assertTrue(report.contains("SYSTEM METADATA"));
    }

    @Test
    public void testGenerateReportWithNullParameters() {
        String report = CrashReporter.generateReport(null, null);

        assertNotNull(report);
        assertTrue(report.contains("CRASH REPORT"));
        assertTrue(report.contains("No Exception provided."));
        assertTrue(report.contains("No player active or registered at the time of crash."));
    }
}
