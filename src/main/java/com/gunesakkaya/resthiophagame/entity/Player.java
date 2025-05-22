package com.gunesakkaya.resthiophagame.entity;

import com.gunesakkaya.resthiophagame.main.GamePanel;
import com.gunesakkaya.resthiophagame.main.KeyHandler;

import java.awt.*;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public int x, y;
    public int speed;
    public String direction = "";
    public boolean collisionOn = false;
    public int maxHp = 100;
    public int currentHp = 100;



    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
    }

    public void update() {
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


        }
    }

    public boolean collisionWithTiles(Rectangle area) {
        int leftCol = area.x / gp.tileSize;
        int rightCol = (area.x + area.width - 1) / gp.tileSize;
        int topRow = area.y / gp.tileSize;
        int bottomRow = (area.y + area.height - 1) / gp.tileSize;

        // Harita sınırı dışı kontrolü
        if (topRow < 0 || bottomRow >= gp.tileM.mapTileNum.length || leftCol < 0 || rightCol >= gp.tileM.mapTileNum[0].length) {
            return true; // dışarı çıktıysa çarpışma kabul et
        }

        if (gp.tileM.tile[gp.tileM.mapTileNum[topRow][leftCol]].collision) return true;
        if (gp.tileM.tile[gp.tileM.mapTileNum[topRow][rightCol]].collision) return true;
        if (gp.tileM.tile[gp.tileM.mapTileNum[bottomRow][leftCol]].collision) return true;
        if (gp.tileM.tile[gp.tileM.mapTileNum[bottomRow][rightCol]].collision) return true;

        return false;
    }
    public void draw(Graphics2D g2) {
        // Oyuncuyu çiz
        g2.setColor(Color.white);
        g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        // ==== HP Bar ====
        int barWidth = gp.tileSize;
        int barHeight = 6;
        int barX = x;
        int barY = y - 10; // karakterin üstünde olsun

        // HP oranı
        double hpRatio = (double) currentHp / maxHp;
        int hpBarFilled = (int) (barWidth * hpRatio);

        // Dış çerçeve
        g2.setColor(Color.GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);

        // Doluluk (kırmızı)
        g2.setColor(Color.RED);
        g2.fillRect(barX, barY, hpBarFilled, barHeight);

        // Opsiyonel: yazıyla HP
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString(currentHp + "/" + maxHp, barX + 2, barY + barHeight - 1);
    }

}
