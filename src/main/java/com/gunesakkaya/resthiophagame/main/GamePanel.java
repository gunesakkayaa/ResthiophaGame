package com.gunesakkaya.resthiophagame.main;

import com.gunesakkaya.resthiophagame.api.service.GearService;
import com.gunesakkaya.resthiophagame.entity.Monster;
import com.gunesakkaya.resthiophagame.entity.Player;
import com.gunesakkaya.resthiophagame.tile.TileManager;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 13;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
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
    public GearService gearService = new GearService();

    private static class RespawnInfo {
        int x, y;
        long time;
        RespawnInfo(int x, int y, long time) {
            this.x = x;
            this.y = y;
            this.time = time;
        }
    }

    private final List<RespawnInfo> pendingRespawns = new ArrayList<>();

    private String lootMessage = "";
    private long lootMessageStartTime = 0;
    private final int lootMessageDuration = 3000;

    private Rectangle cancelButton = new Rectangle();

    public void setLootMessage(String message) {
        this.lootMessage = message;
        this.lootMessageStartTime = System.currentTimeMillis();
    }

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(keyH);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (player.isInventoryOpen() && cancelButton.contains(e.getPoint())) {
                    player.toggleInventory();
                    repaint();
                }
            }
        });

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
        monsters.add(new Monster(this, tileSize * 16, tileSize * 15));

    }

    public void restartGame() {
        int px = tileM.playerStartX * tileSize;
        int py = tileM.playerStartY * tileSize;

        player.setPosition(px, py);
        player.currentHp = player.maxHp;
        monsters.clear();
        pendingRespawns.clear();
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
            if (!player.isInventoryOpen()) {
                player.update();

                if (!player.isWaitingForEquipChoice()) {
                    Iterator<Monster> iterator = monsters.iterator();
                    while (iterator.hasNext()) {
                        Monster monster = iterator.next();
                        monster.update();

                        if (!monster.alive) {
                            iterator.remove();
                            long respawnTime = System.currentTimeMillis() + 20000;
                            pendingRespawns.add(new RespawnInfo(monster.x, monster.y, respawnTime));
                        }
                    }

                    long now = System.currentTimeMillis();
                    Iterator<RespawnInfo> respawnIterator = pendingRespawns.iterator();
                    while (respawnIterator.hasNext()) {
                        RespawnInfo info = respawnIterator.next();
                        if (now >= info.time) {
                            monsters.add(new Monster(this, info.x, info.y));
                            respawnIterator.remove();
                        }
                    }
                }

                if (player.currentHp <= 0) {
                    gameState = GameState.GAME_OVER;
                }
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

        if (!lootMessage.isEmpty()) {
            long now = System.currentTimeMillis();
            if (now - lootMessageStartTime < lootMessageDuration) {
                int boxX = 20;
                int boxY = screenHeight - 50;
                int boxWidth = 250;
                int boxHeight = 30;

                g2.setColor(Color.BLACK);
                g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                g2.drawString(lootMessage, boxX + 10, boxY + 20);
            } else {
                lootMessage = "";
            }
        }

        if (player.isWaitingForEquipChoice()) {
            int boxWidth = 400;
            int boxHeight = 100;
            int boxX = (screenWidth - boxWidth) / 2;
            int boxY = (screenHeight - boxHeight) / 2;

            g2.setColor(new Color(30, 30, 30, 230));
            g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 16));

            String line1 = "You found: " + player.getPendingLootName();
            String line2 = "[Q] Equip     [E] Sell";

            g2.drawString(line1, boxX + 20, boxY + 40);
            g2.drawString(line2, boxX + 20, boxY + 70);
        }

        if (player.isInventoryOpen()) {
            int boxWidth = 300;
            int boxHeight = 200;
            int boxX = (screenWidth - boxWidth) / 2;
            int boxY = (screenHeight - boxHeight) / 2;

            g2.setColor(new Color(20, 20, 20, 240));
            g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Inventory", boxX + 20, boxY + 30);

            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            int lineY = boxY + 60;

            if (player.getInventory().isEmpty()) {
                g2.drawString("Inventory is empty.", boxX + 20, lineY);
            } else {
                for (var gear : player.getInventory()) {
                    String itemText = "- " + gear.getName() + " (+" + gear.getValue() + ") [" + gear.getType() + "]";
                    g2.drawString(itemText, boxX + 20, lineY);
                    lineY += 20;
                }
            }

            // Mouse ile tıklanabilir Cancel butonu
            cancelButton.setBounds(boxX + 20, boxY + boxHeight - 40, 80, 25);
            g2.setColor(Color.DARK_GRAY);
            g2.fillRoundRect(cancelButton.x, cancelButton.y, cancelButton.width, cancelButton.height, 10, 10);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.drawString("Cancel", cancelButton.x + 15, cancelButton.y + 17);
        }

        if (gameState == GameState.GAME_OVER) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            int boxWidth = 400;
            int boxHeight = 150;
            int boxX = (screenWidth - boxWidth) / 2;
            int boxY = (screenHeight - boxHeight) / 2;

            g2.setColor(new Color(30, 30, 30, 220));
            g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

            g2.setColor(Color.RED);
            g2.setFont(new Font("Verdana", Font.BOLD, 36));
            String gameOverText = "GAME OVER";
            int textWidth = g2.getFontMetrics().stringWidth(gameOverText);
            g2.drawString(gameOverText, boxX + (boxWidth - textWidth) / 2, boxY + 50);

            g2.setFont(new Font("Verdana", Font.BOLD, 24));
            g2.setColor(Color.WHITE);
            String yesText = "Retry(R)";
            String noText = "Close(N)";
            int yesWidth = g2.getFontMetrics().stringWidth(yesText);
            int noWidth = g2.getFontMetrics().stringWidth(noText);

            g2.drawString(yesText, boxX + boxWidth / 4 - yesWidth / 2, boxY + 110);
            g2.drawString(noText, boxX + (boxWidth * 3 / 4) - noWidth / 2, boxY + 110);
        }

        // 🪙 Coin sayısını ekrana yaz
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Coins: " + player.getCoins(), 20, 30);


        g2.dispose();
    }
}