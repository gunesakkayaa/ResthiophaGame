package com.gunesakkaya.resthiophagame.main;

import java.awt.*;

public class GameObject {
    public int worldX, worldY;
    public int speed;
    public boolean collisionOn = false;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); // Örn: tile size
}
