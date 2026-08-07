package com.legendofthesoul;

import com.legendofthesoul.engine.GameEngine;

public class Main {
    public static void main(String[] args) {
        // Initializes the game engine with the default hero title
        GameEngine game = new GameEngine("Soul Wanderer");
        game.start();
    }
}
