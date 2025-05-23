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

            int behavior = random.nextInt(3); //buralar monsterında hal ve hareketi belirledik
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

    private void moveRandom() { //bir aşağı yukarı sağ sol falan gidiyo burada
        int dir = random.nextInt(4);

        for (int i = 0; i < speed; i++) {
            int dx = 0, dy = 0;

            if (dir == 0) dy = -1;
            else if (dir == 1) dy = 1;
            else if (dir == 2) dx = -1;
            else if (dir == 3) dx = 1;

            if (!collidesWithWall(x + dx, y + dy)) {
                x += dx;
                y += dy;
            } else {
                break;
            }
        }
    }

    private void moveTowardsPlayer() {
        int dx = 0, dy = 0;
        if (gp.player.x < x) dx = -1;
        else if (gp.player.x > x) dx = 1;

        if (gp.player.y < y) dy = -1;
        else if (gp.player.y > y) dy = 1;

        for (int i = 0; i < speed; i++) {
            int newX = x + dx;
            int newY = y + dy;

            if (!collidesWithWall(newX, newY)) {
                x = newX;
                y = newY;
            } else {
                break;
            }
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
        if (Math.abs(player.x - this.x) < gp.tileSize && Math.abs(player.y - this.y) < gp.tileSize) {
            int damage = 3;
            player.currentHp -= damage;
            System.out.println("Monster attacked player for " + damage + " damage. Player HP: " + player.currentHp);

            if (player.currentHp <= 0) {
                System.out.println("Player is dead!");
            }
        }
    }

    public void draw(Graphics2D g2, int tileSize) {
        if (!alive) return;

        int drawX = x + solidArea.x;
        int drawY = y + solidArea.y;

        g2.setColor(Color.BLACK);
        g2.fillRect(drawX, drawY, solidArea.width, solidArea.height);

        String text = "M";
        Font font = new Font("Verdana", Font.TRUETYPE_FONT, gp.tileSize / 2);
        g2.setFont(font);
        FontMetrics metrics = g2.getFontMetrics(font);

        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();

        int textX = drawX + (solidArea.width - textWidth) / 2;
        int textY = drawY + ((solidArea.height - textHeight) / 2) + metrics.getAscent();

        g2.setColor(Color.WHITE);
        g2.drawString(text, textX, textY);
    }

}