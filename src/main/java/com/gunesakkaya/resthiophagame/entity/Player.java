package com.gunesakkaya.resthiophagame.entity;

import com.gunesakkaya.resthiophagame.main.GamePanel;
import com.gunesakkaya.resthiophagame.main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public int x, y;
    public int speed;
    public String direction = "";
    public boolean collisionOn = false;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);


    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
    }
    public void setDefaultValues() {

        x = 100;
        y = 100;
        speed = 4;
//        direction = "down";
    }
//    public void getPlayerImage() {
//
//        try {
//
//            player = ImageIO.read(getClass().getResourceAsStream("/images/player.png"));
//
//        }catch(IOException e) {
//            e.printStackTrace();
//        }
//    }
    public void update() {

        if(keyH.upPressed == true) {
            y -= speed;
        }
        else if(keyH.downPressed == true) {
            y += speed;
        }
        else if(keyH.leftPressed == true) {
            x -= speed;
        }
        else if(keyH.rightPressed == true) {
            x += speed;
        }
    }
    public void draw(Graphics2D g2) {

        g2.setColor(Color.white);

        g2.fillRect(x, y, gp.tileSize, gp.tileSize);


    }
}
