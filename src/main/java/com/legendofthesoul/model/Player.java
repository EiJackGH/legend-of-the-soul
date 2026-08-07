package com.legendofthesoul.model;

import java.io.Serializable;

public class Player extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private int souls;
    private int level;

    public Player(String name) {
        super(name, 100, 20);
        this.souls = 0;
        this.level = 1;
    }

    public void addSouls(int amount) {
        this.souls += amount;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int requiredSouls = level * 50;
        if (souls >= requiredSouls) {
            level++;
            souls -= requiredSouls;
            maxHealth += 25;
            health = maxHealth;
            attackPower += 8;
            System.out.printf("%n🌟 SOUL ASCENSION! You reached Level %d! Max HP and Attack Increased.%n", level);
        }
    }

    public void heal() {
        int healAmount = 30;
        this.health = Math.min(maxHealth, health + healAmount);
        System.out.printf("✨ You channel energy and restore %d HP. Current HP: %d/%d%n", healAmount, health, maxHealth);
    }

    public int getSouls() { return souls; }
    public int getLevel() { return level; }
}
