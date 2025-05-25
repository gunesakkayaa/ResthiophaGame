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


}