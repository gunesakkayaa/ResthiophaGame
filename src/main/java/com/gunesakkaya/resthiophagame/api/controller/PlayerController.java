//package com.gunesakkaya.resthiophagame.api.controller;
//
//import com.gunesakkaya.resthiophagame.entity.Player;
//import com.gunesakkaya.resthiophagame.main.GamePanel;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/player")
//public class PlayerController {
//
//    private final GamePanel gamePanel;
//
//    public PlayerController(GamePanel gamePanel) {
//        this.gamePanel = gamePanel;
//    }
//
//    @GetMapping("/coins")
//    public int getCoins() {
//        Player player = gamePanel.player;
//        return player.coins;
//    }
//}
