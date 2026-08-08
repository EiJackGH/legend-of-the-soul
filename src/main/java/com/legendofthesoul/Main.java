package com.legendofthesoul;

import com.legendofthesoul.engine.CrashReporter;
import com.legendofthesoul.engine.GameEngine;

public class Main {
    public static void main(String[] args) {
        // Register standard crash reporter handler
        CrashReporter.register();

        GameEngine game = new GameEngine("Soul Wanderer");
        game.start();
    }
}
