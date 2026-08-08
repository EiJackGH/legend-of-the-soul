package com.legendofthesoul;

import com.legendofthesoul.engine.GameEngine;

import com.legendofthesoul.engine.CrashReporter;

public class Main {
    public static void main(String[] args) {
        // Register default uncaught exception handler for crash reporting
        Thread.setDefaultUncaughtExceptionHandler(new CrashReporter());

        // Initializes the game engine with the default hero title
        GameEngine game = new GameEngine("Soul Wanderer");
        game.start();
    }
}
