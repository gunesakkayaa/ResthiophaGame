package com.gunesakkaya.resthiophagame.tile;

import com.gunesakkaya.resthiophagame.main.GamePanel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public int playerStartX = 1; // Default
    public int playerStartY = 1;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];

        tile[0] = new Tile(); // Yol
        tile[0].collision = false;

        tile[1] = new Tile(); // Duvar
        tile[1].collision = true;

        loadMap("/maps/map.txt");
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            int rowCount = 0;
            while ((line = br.readLine()) != null) {
                rowCount++;
            }
            br.close();

            is = getClass().getResourceAsStream(filePath);
            br = new BufferedReader(new InputStreamReader(is));

            mapTileNum = new int[rowCount][];
            int row = 0;
            while ((line = br.readLine()) != null) {
                int colCount = line.length();
                mapTileNum[row] = new int[colCount];
                for (int col = 0; col < colCount; col++) {
                    char ch = line.charAt(col);
                    if (ch == '1' || ch == 'X') {
                        mapTileNum[row][col] = 1; // Wall
                    } else if (ch == 'P') {
                        playerStartX = col;
                        playerStartY = row;
                        mapTileNum[row][col] = 0; // Yol
                    } else {
                        mapTileNum[row][col] = 0; // Yol
                    }
                }
                row++;
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        for (int row = 0; row < mapTileNum.length; row++) {
            for (int col = 0; col < mapTileNum[row].length; col++) {

                int tileNum = mapTileNum[row][col];

                int x = col * gp.tileSize;
                int y = row * gp.tileSize;

                if (tileNum == 1) {
                    g2.setColor(Color.black);
                    g2.fillRect(x, y, gp.tileSize, gp.tileSize);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, gp.tileSize / 2));
                    g2.drawString("X", x + gp.tileSize / 3, y + (gp.tileSize * 2) / 3);
                } else {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(x, y, gp.tileSize, gp.tileSize);
                }
            }
        }
    }
}
