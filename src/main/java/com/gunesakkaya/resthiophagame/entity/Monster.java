package com.gunesakkaya.resthiophagame.entity;

import com.gunesakkaya.resthiophagame.main.GamePanel;

import java.awt.*;

public class Monster extends Entity {

    GamePanel gp;
    public int hp = 100;
    public boolean alive = true;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    public Monster(GamePanel gp, int x, int y) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.speed = 0; // şimdilik sabit
    }

    public void draw(Graphics2D g2, int tileSize) {
        if (!alive) return;

        g2.setColor(Color.BLACK);
        g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, gp.tileSize / 2));
        g2.drawString("M", x + gp.tileSize / 3, y + (gp.tileSize * 2) / 3);
    }
}
