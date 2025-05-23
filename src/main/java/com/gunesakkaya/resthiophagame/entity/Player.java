package com.gunesakkaya.resthiophagame.entity;

import com.gunesakkaya.resthiophagame.main.GamePanel;
import com.gunesakkaya.resthiophagame.main.KeyHandler;

import java.awt.*;
import java.util.Random;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public int x, y;
    public int speed;
    public String direction = "";
    public boolean collisionOn = false;
    public int maxHp = 200;
    public int currentHp = 200;
    public Gear equippedSword = null;
    public Gear equippedShoes = null;
    private int moveCooldown = 20;
    private int moveCounter = 0;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    public boolean alive = true;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 10;
        alive = true;
        currentHp = maxHp;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (!alive) return;

        moveCounter++;

        int cooldown = moveCooldown;
        if (equippedShoes != null) {
            cooldown -= equippedShoes.value;
            if (cooldown < 5) cooldown = 5;
        }

        if (moveCounter >= cooldown) {
            int newX = x;
            int newY = y;

            if (keyH.upPressed) {
                newY -= speed;
                direction = "up";
            } else if (keyH.downPressed) {
                newY += speed;
                direction = "down";
            } else if (keyH.leftPressed) {
                newX -= speed;
                direction = "left";
            } else if (keyH.rightPressed) {
                newX += speed;
                direction = "right";
            }

            Rectangle futureArea = new Rectangle(newX + solidArea.x, newY + solidArea.y, solidArea.width, solidArea.height);

            if (!collisionWithTiles(futureArea)) {
                x = newX;
                y = newY;
                moveCounter = 0;
            }
        }

        attackNearbyMonsters();
    }

    private void attackNearbyMonsters() {
        for (Monster m : gp.monsters) {
            if (!m.alive) continue;

            if (Math.abs(m.x - this.x) < gp.tileSize && Math.abs(m.y - this.y) < gp.tileSize) {
                int damage = 2;
                if (equippedSword != null) {
                    damage += equippedSword.value;
                }
                m.hp -= damage;
                System.out.println("Attacked monster for " + damage + " damage. Remaining HP: " + m.hp);

                if (m.hp <= 0) {
                    m.alive = false;
                    lootDrop(m);
                }
            }
        }
    }

    public void takeDamage(int damage) {
        if (!alive) return;

        currentHp -= damage;
        System.out.println("Player took " + damage + " damage. HP: " + currentHp + "/" + maxHp);
        if (currentHp <= 0) {
            currentHp = 0;
            die();
        }
    }

    private void die() {
        alive = false;
        System.out.println("Player died!");
        gp.gameThread = null;
    }

    private void lootDrop(Monster m) {
        Random rand = new Random();
        Gear.Type type = rand.nextBoolean() ? Gear.Type.SWORD : Gear.Type.SHOES;
        int value = rand.nextInt(3) + 1;
        Gear loot = new Gear(type, value);
        System.out.println("Monster dropped: " + loot);

        if (type == Gear.Type.SWORD) {
            equippedSword = loot;
        } else {
            equippedShoes = loot;
        }
    }

    public boolean collisionWithTiles(Rectangle area) {
        int leftCol = area.x / gp.tileSize;
        int rightCol = (area.x + area.width - 1) / gp.tileSize;
        int topRow = area.y / gp.tileSize;
        int bottomRow = (area.y + area.height - 1) / gp.tileSize;

        if (topRow < 0 || bottomRow >= gp.tileM.mapTileNum.length || leftCol < 0 || rightCol >= gp.tileM.mapTileNum[0].length) {
            return true;
        }

        if (gp.tileM.tile[gp.tileM.mapTileNum[topRow][leftCol]].collision) return true;
        if (gp.tileM.tile[gp.tileM.mapTileNum[topRow][rightCol]].collision) return true;
        if (gp.tileM.tile[gp.tileM.mapTileNum[bottomRow][leftCol]].collision) return true;
        if (gp.tileM.tile[gp.tileM.mapTileNum[bottomRow][rightCol]].collision) return true;

        return false;
    }

    public void draw(Graphics2D g2) {
        if (!alive) return;

        g2.setColor(Color.white);
        g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("P", x + gp.tileSize / 3, y + gp.tileSize / 1.5f);

        int barWidth = gp.tileSize;
        int barHeight = 6;
        int barX = x;
        int barY = y - 10;

        double hpRatio = (double) currentHp / maxHp;
        int hpBarFilled = (int) (barWidth * hpRatio);

        g2.setColor(Color.GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);

        g2.setColor(Color.RED);
        g2.fillRect(barX, barY, hpBarFilled, barHeight);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString(currentHp + "/" + maxHp, barX + 2, barY + barHeight - 1);
    }
}
