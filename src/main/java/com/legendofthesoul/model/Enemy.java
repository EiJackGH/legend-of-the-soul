package com.legendofthesoul.model;

public class Enemy extends Entity {
    private final int soulReward;

    public Enemy(String name, int health, int attackPower, int soulReward) {
        super(name, health, attackPower);
        this.soulReward = soulReward;
    }

    public int getSoulReward() {
        return soulReward;
    }
}
