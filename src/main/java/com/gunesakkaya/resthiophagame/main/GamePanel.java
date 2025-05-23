package com.gunesakkaya.resthiophagame.main;

import com.gunesakkaya.resthiophagame.entity.Monster;
import com.gunesakkaya.resthiophagame.entity.Player;
import com.gunesakkaya.resthiophagame.tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 13;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 20;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    int FPS = 60;

    public ArrayList<Monster> monsters = new ArrayList<>();
    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    public Thread gameThread;
    public Player player;

    private static class RespawnInfo { //yeniden doğmasıyla alakalı bilgileri tanımlıyoruz
        int x, y;
        long respawnTime;

        RespawnInfo(int x, int y, long respawnTime) {
            this.x = x;
            this.y = y;
            this.respawnTime = respawnTime;
        }
    }

    private final java.util.List<RespawnInfo> pendingRespawns = new ArrayList<>();


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        int px = tileM.playerStartX * tileSize;
        int py = tileM.playerStartY * tileSize;
        player = new Player(this, keyH);
        player.setPosition(px, py);

        spawnMonsters();
    }

    public void spawnMonsters() {
        monsters.add(new Monster(this, tileSize * 4, tileSize * 5));
        monsters.add(new Monster(this, tileSize * 10, tileSize * 7));
        monsters.add(new Monster(this, tileSize * 15, tileSize * 2));
        monsters.add(new Monster(this, tileSize * 10, tileSize * 10));
    }
    public void restartGame() {
        int px = tileM.playerStartX * tileSize;
        int py = tileM.playerStartY * tileSize;

        player.setPosition(px, py);
        player.currentHp = player.maxHp;
        monsters.clear();
        spawnMonsters();
        gameState = GameState.PLAYING;
    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();

    }
    public enum GameState {
        PLAYING,
        GAME_OVER
    }

    public GameState gameState = GameState.PLAYING;


    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        if (gameState == GameState.PLAYING) {
            player.update();

            // Monster güncelleme + ölüm kontrolü
            java.util.Iterator<Monster> iterator = monsters.iterator();
            while (iterator.hasNext()) {
                Monster monster = iterator.next();
                monster.update();

                if (!monster.alive) {
                    iterator.remove(); // ölen monster oyundan çıkar
                    long respawnTime = System.currentTimeMillis() + 20000; // respawn süresi belirledik (20 saniye)
                    pendingRespawns.add(new RespawnInfo(monster.x, monster.y, respawnTime));
                }
            }

            // Respawn kontrolü
            long now = System.currentTimeMillis();
            java.util.Iterator<RespawnInfo> respawnIterator = pendingRespawns.iterator();
            while (respawnIterator.hasNext()) {
                RespawnInfo info = respawnIterator.next();
                if (now >= info.respawnTime) {
                    monsters.add(new Monster(this, info.x, info.y));
                    respawnIterator.remove();
                }
            }

            // player ölünce gamer over metoduna geçer
            if (player.currentHp <= 0) {
                gameState = GameState.GAME_OVER;
            }
        }
    }



    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);
        player.draw(g2);

        for (Monster monster : monsters) {
            monster.draw(g2, tileSize);
        }
        if (gameState == GameState.GAME_OVER) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            String msg = "Game Over – Play again? (Y/N)";
            int msgWidth = g2.getFontMetrics().stringWidth(msg);
            g2.drawString(msg, (screenWidth - msgWidth) / 2, screenHeight / 2);
        }


        g2.dispose();
    }
}
