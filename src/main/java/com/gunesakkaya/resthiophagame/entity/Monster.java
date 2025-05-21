package com.gunesakkaya.resthiophagame.entity;

import com.gunesakkaya.resthiophagame.main.GamePanel;

import java.awt.*;
import java.util.UUID;

public class Monster extends Entity {

    public int hp = 10;
    public boolean alive = true;
    public final UUID id = UUID.randomUUID();

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    GamePanel gp;

    public Monster(GamePanel gp, int x, int y) {
        this.gp = gp;
        this.x = x;
        this.y = y;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            alive = false;
        }
    }

    public void draw(Graphics2D g2, int tileSize) {
        if (alive) {
            g2.setColor(Color.RED);
            g2.fillRect(x, y, tileSize, tileSize);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, tileSize / 2));
            g2.drawString("M", x + tileSize / 3, y + (tileSize * 2) / 3);
        }
    }
}
