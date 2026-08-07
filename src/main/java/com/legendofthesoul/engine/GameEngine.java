package com.legendofthesoul.engine;

import com.legendofthesoul.model.Player;
import com.legendofthesoul.model.Enemy;
import java.util.Scanner;
import java.util.Random;

public class GameEngine {
    private Player player;
    private final String defaultName;
    private final Scanner scanner;
    private final Random random;

    public GameEngine(String defaultName) {
        this.defaultName = defaultName;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    public void start() {
        System.out.println("========================================");
        System.out.println("⚔️  Welcome to LEGEND OF THE SOUL  ⚔️");
        System.out.println("========================================");

        // Attempt to load saved game
        this.player = SaveManager.loadGame();

        if (this.player == null) {
            System.out.print("Enter your hero's name (or press Enter for '" + defaultName + "'): ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = defaultName;
            }
            this.player = new Player(name);
            System.out.println("\n✨ A new legend begins! Good luck, " + player.getName() + ".");
        }

        gameLoop();
    }

    private void gameLoop() {
        boolean running = true;
        while (running && player.isAlive()) {
            printStatus();
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Explore (Battle Enemies)");
            System.out.println("2. Channel Energy (Heal)");
            System.out.println("3. Save Progress");
            System.out.println("4. Quit Game");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    battle();
                    break;
                case "2":
                    player.heal();
                    break;
                case "3":
                    SaveManager.saveGame(player);
                    break;
                case "4":
                    System.out.println("Saving game before quitting...");
                    SaveManager.saveGame(player);
                    running = false;
                    System.out.println("Farewell, brave wanderer!");
                    break;
                default:
                    System.out.println("⚠️ Invalid choice. Try again.");
            }
        }

        if (!player.isAlive()) {
            System.out.println("\n💀 You have fallen in battle... Your journey has ended.");
            System.out.println("========================================");
        }
    }

    private void printStatus() {
        System.out.println("\n----------------------------------------");
        System.out.printf("👤 Hero: %s | Level: %d%n", player.getName(), player.getLevel());
        System.out.printf("❤️  HP: %d/%d | ⚔️  Attack: %d%n", player.getHealth(), player.getMaxHealth(), player.getAttackPower());
        System.out.printf("🌟 Souls: %d%n", player.getSouls());
        System.out.println("----------------------------------------");
    }

    private void battle() {
        String[] enemyNames = {"Void Larva", "Shadow Wraith", "Grave Sentinel", "Soul Devourer"};
        String enemyName = enemyNames[random.nextInt(enemyNames.length)];
        int enemyHealth = 40 + player.getLevel() * 15 + random.nextInt(20);
        int enemyAttack = 10 + player.getLevel() * 4 + random.nextInt(5);
        int soulReward = 15 + player.getLevel() * 10 + random.nextInt(10);

        Enemy enemy = new Enemy(enemyName, enemyHealth, enemyAttack, soulReward);
        System.out.printf("%n👹 An enemy approaches: %s (HP: %d, Attack: %d)!%n", enemy.getName(), enemy.getHealth(), enemy.getAttackPower());

        while (enemy.isAlive() && player.isAlive()) {
            System.out.println("\nCombat Options:");
            System.out.println("1. Strike");
            System.out.println("2. Flee");
            System.out.print("> ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("2")) {
                if (random.nextBoolean()) {
                    System.out.println("💨 You successfully escaped back to safety!");
                    return;
                } else {
                    System.out.println("❌ You failed to escape!");
                }
            } else if (!choice.equals("1")) {
                System.out.println("⚠️ Hesitation costs you! You missed your turn.");
            } else {
                // Player attacks enemy
                int damageDealt = player.getAttackPower() - 3 + random.nextInt(7);
                damageDealt = Math.max(1, damageDealt);
                enemy.takeDamage(damageDealt);
                System.out.printf("⚔️ You strike %s for %d damage! (%d HP remaining)%n", enemy.getName(), damageDealt, enemy.getHealth());
            }

            if (enemy.isAlive()) {
                // Enemy attacks player
                int damageTaken = enemy.getAttackPower() - 2 + random.nextInt(5);
                damageTaken = Math.max(1, damageTaken);
                player.takeDamage(damageTaken);
                System.out.printf("💥 %s attacks you for %d damage! (Your HP: %d/%d)%n", enemy.getName(), damageTaken, player.getHealth(), player.getMaxHealth());
            }
        }

        if (player.isAlive()) {
            System.out.printf("%n🎉 You defeated %s!%n", enemy.getName());
            System.out.printf("✨ Collected %d souls.%n", enemy.getSoulReward());
            player.addSouls(enemy.getSoulReward());
        }
    }
}
