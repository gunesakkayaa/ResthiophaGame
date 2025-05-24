package com.gunesakkaya.resthiophagame.api.controller;

import com.gunesakkaya.resthiophagame.api.model.Gear;
import com.gunesakkaya.resthiophagame.api.model.PlayerEntity;
import com.gunesakkaya.resthiophagame.api.service.PlayerService;
import com.gunesakkaya.resthiophagame.entity.Player;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // 🎮 Oyun içindeki canlı oyuncu objesine dayalı endpointler
    @GetMapping("/coins")
    public int getCoins() {
        return playerService.getPlayer().getCoins();
    }

    @GetMapping("/inventory")
    public List<Gear> getInventory() {
        return playerService.getPlayer().getInventory();
    }

    @GetMapping("/equipment")
    public Map<String, Gear> getEquipment() {
        Player player = playerService.getPlayer();
        Map<String, Gear> equipped = new HashMap<>();
        equipped.put("sword", player.equippedSword);
        equipped.put("shoes", player.equippedShoes);
        return equipped;
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        Player player = playerService.getPlayer();
        Map<String, Object> summary = new HashMap<>();
        summary.put("coins", player.getCoins());
        summary.put("inventory", player.getInventory());
        summary.put("equippedSword", player.equippedSword);
        summary.put("equippedShoes", player.equippedShoes);
        return summary;
    }

    // 🗃 Veritabanı tabanlı endpointler
    @GetMapping
    public PlayerEntity getPlayerEntityFromDb() {
        return playerService.getOrCreatePlayerEntity();
    }

    @PostMapping("/save")
    public PlayerEntity savePlayerEntity(@RequestBody PlayerEntity entity) {
        return playerService.savePlayerEntity(entity);
    }
}
