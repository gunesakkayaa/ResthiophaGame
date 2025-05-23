package com.gunesakkaya.resthiophagame.entity;

import com.gunesakkaya.resthiophagame.main.GamePanel;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class Monster extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;
    GamePanel gp;
    public int hp = 100;
    public boolean alive = true;
    public Rectangle solidArea = new Rectangle(8, 8, 32, 32);

    private int moveCooldown = 25;
    private int moveCounter = 0;

    private Random random = new Random();

    public Monster(GamePanel gp, int x, int y) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.speed = 5;
    }

    public void update() {
        if (!alive) return;

        moveCounter++;
        if (moveCounter >= moveCooldown) {
            moveCounter = 0;

            int behavior = random.nextInt(3);
            switch (behavior) {
                case 0:
                case 2:
                    moveRandom();
                    break;
                case 1:
                    moveTowardsPlayer();
                    break;
            }
        }

        attackPlayerIfNearby();
    }

    private void moveRandom() {
        int dir = random.nextInt(4);
        int dx = 0, dy = 0;

        if (dir == 0) dy = -speed;
        else if (dir == 1) dy = speed;
        else if (dir == 2) dx = -speed;
        else if (dir == 3) dx = speed;

        if (!collidesWithWall(x + dx, y + dy)) {
            x += dx;
            y += dy;
        }
    }

    private void moveTowardsPlayer() {
        int playerX = gp.player.x;
        int playerY = gp.player.y;

        int dx = 0;
        int dy = 0;

        if (playerX < x) dx = -speed;
        else if (playerX > x) dx = speed;

        if (playerY < y) dy = -speed;
        else if (playerY > y) dy = speed;

        if (!collidesWithWall(x + dx, y + dy)) {
            x += dx;
            y += dy;
        }
    }

    private boolean collidesWithWall(int newX, int newY) {
        Rectangle futureArea = new Rectangle(newX + solidArea.x, newY + solidArea.y, solidArea.width, solidArea.height);

        int leftCol = futureArea.x / gp.tileSize;
        int rightCol = (futureArea.x + futureArea.width - 1) / gp.tileSize;
        int topRow = futureArea.y / gp.tileSize;
        int bottomRow = (futureArea.y + futureArea.height - 1) / gp.tileSize;

        try {
            return gp.tileM.tile[gp.tileM.mapTileNum[topRow][leftCol]].collision
                    || gp.tileM.tile[gp.tileM.mapTileNum[topRow][rightCol]].collision
                    || gp.tileM.tile[gp.tileM.mapTileNum[bottomRow][leftCol]].collision
                    || gp.tileM.tile[gp.tileM.mapTileNum[bottomRow][rightCol]].collision;
        } catch (ArrayIndexOutOfBoundsException e) {
            return true;

        }
    }

    private void attackPlayerIfNearby() {
        Player player = gp.player;
        // Eğer oyuncuya 1 tile menzildeyse saldır
        if (Math.abs(player.x - this.x) < gp.tileSize && Math.abs(player.y - this.y) < gp.tileSize) {
            int damage = 3; // canavarın saldırı gücü
            player.currentHp -= damage;
            System.out.println("Monster attacked player for " + damage + " damage. Player HP: " + player.currentHp);

            if (player.currentHp <= 0) {
                System.out.println("Player is dead!");
                // Buraya oyuncu öldüğünde yapılacak işlemleri ekleyebilirsin

            }
        }
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
