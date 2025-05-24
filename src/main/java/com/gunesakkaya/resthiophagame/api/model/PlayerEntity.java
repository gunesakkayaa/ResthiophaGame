package com.gunesakkaya.resthiophagame.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int coins;
    private int level;
    private int currentHp;
    private int maxHp;

    public PlayerEntity() {
        this.coins = 0;
        this.level = 1;
        this.maxHp = 200;
        this.currentHp = 200;
    }

    // Getters & Setters
    public Long getId() { return id; }

    public int getCoins() { return coins; }
    public void setCoins(int coins) { this.coins = coins; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getCurrentHp() { return currentHp; }
    public void setCurrentHp(int currentHp) { this.currentHp = currentHp; }

    public int getMaxHp() { return maxHp; }
    public void setMaxHp(int maxHp) { this.maxHp = maxHp; }
}
