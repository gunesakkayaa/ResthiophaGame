package com.gunesakkaya.resthiophagame.entity;

import com.gunesakkaya.resthiophagame.api.model.Gear;
import com.gunesakkaya.resthiophagame.main.GamePanel;
import com.gunesakkaya.resthiophagame.main.KeyHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public int x, y;
    public int speed;
    public String direction = "";
    public boolean collisionOn = false;
    public int maxHp = 200;
    public int currentHp = 200;
    private int moveCooldown = 30;
    private int moveCounter = 0;
    public Gear equippedShoes = null;
    public Gear equippedSword = null;
    private long lastHealTime = System.currentTimeMillis();
    private final int healAmount = 3;
    private final int healInterval = 1000; // 1000 ms = 1 saniye

    private int coins = 0;
    private List<Gear> inventory = new ArrayList<>();

    public Rectangle solidArea = new Rectangle(8, 8, 32, 32);

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 10;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        moveCounter++;

        int cooldown = getEffectiveMoveCooldown();

        if (moveCounter >= cooldown) {
            for (int i = 0; i < speed; i++) {
                int newX = x;
                int newY = y;

                if (keyH.upPressed) {
                    newY -= 1;
                    direction = "up";
                } else if (keyH.downPressed) {
                    newY += 1;
                    direction = "down";
                } else if (keyH.leftPressed) {
                    newX -= 1;
                    direction = "left";
                } else if (keyH.rightPressed) {
                    newX += 1;
                    direction = "right";
                }

                Rectangle futureArea = new Rectangle(newX + solidArea.x, newY + solidArea.y, solidArea.width, solidArea.height);

                if (!collisionWithTiles(futureArea)) {
                    x = newX;
                    y = newY;
                } else {
                    break;
                }
            }
            moveCounter = 0;
        }
        long now = System.currentTimeMillis();
        if (now - lastHealTime >= healInterval) {
            currentHp = Math.min(currentHp + healAmount, maxHp);
            lastHealTime = now;
        }
        attackNearbyMonsters();
    }

    private void attackNearbyMonsters() {
        for (Monster m : gp.monsters) {
            if (!m.alive) continue;

            if (Math.abs(m.x - this.x) < gp.tileSize && Math.abs(m.y - this.y) < gp.tileSize) {
                int baseDamage = 5;
                if (equippedSword != null) {
                    baseDamage += (int) equippedSword.getValue(); //örn. +3 lük 8 damage gibi
                }
                int damage = baseDamage;

                m.hp -= damage;
                System.out.println("Attacked monster for " + damage + " damage. Remaining HP: " + m.hp);

                if (m.hp <= 0) {
                    m.alive = false;
                    System.out.println("Monster defeated.");

                    Gear loot = gp.gearService.getRandomLoot();

                    boolean isBetter = false;
                    if (loot.getType() == Gear.Type.SHOES) {
                        isBetter = equippedShoes == null || loot.getValue() > equippedShoes.getValue();
                    } else if (loot.getType() == Gear.Type.SWORD) {
                        isBetter = equippedSword == null || loot.getValue() > equippedSword.getValue();
                    }

                    if (isBetter) {
                        System.out.println("Better gear found. Auto-equipped.");
                        if (loot.getType() == Gear.Type.SHOES) equippedShoes = loot;
                        else if (loot.getType() == Gear.Type.SWORD) equippedSword = loot;
                    } else {
                        int coinValue = loot.getSellValue();
                        addCoins(coinValue);
                        System.out.println("Sold for " + coinValue + " coins.");

                    }

                    addToInventory(loot);
                    gp.setLootMessage("Loot: " + loot.getName());
                    System.out.println("Dropped: " + loot.getName() + " (+" + loot.getValue() + ") [" + loot.getType() + "]");
                }
            }
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

    public int getEffectiveMoveCooldown() {
        double baseCooldown = moveCooldown;

        if (equippedShoes != null) {
            baseCooldown = baseCooldown / (2.0 + equippedShoes.getValue()); //30/(2+x) hızında gideriz
        }

        return (int) baseCooldown;
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    public int getCoins() {
        return coins;
    }

    public void addToInventory(Gear loot) {
        inventory.add(loot);
    }

    public List<Gear> getInventory() {
        return inventory;
    }

    public void draw(Graphics2D g2) {
        int drawX = x + solidArea.x;
        int drawY = y + solidArea.y;

        g2.setColor(Color.white);
        g2.fillRect(drawX, drawY, solidArea.width, solidArea.height);

        int barWidth = gp.tileSize;
        int barHeight = 6;
        int barX = drawX;
        int barY = drawY - 10;

        double hpRatio = (double) currentHp / maxHp;
        int hpBarFilled = (int) (barWidth * hpRatio);

        g2.setFont(new Font("Consolas", Font.BOLD, gp.tileSize / 2));
        g2.setColor(Color.BLACK);
        g2.drawString("P", drawX + gp.tileSize / 4, drawY + gp.tileSize / 2 + 4);

        g2.setColor(Color.GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);

        g2.setColor(Color.RED);
        g2.fillRect(barX, barY, hpBarFilled, barHeight);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString(currentHp + "/" + maxHp, barX + 2, barY + barHeight - 1);
    }
}
