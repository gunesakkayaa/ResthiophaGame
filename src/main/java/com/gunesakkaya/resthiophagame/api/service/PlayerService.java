package com.gunesakkaya.resthiophagame.api.service;

import com.gunesakkaya.resthiophagame.api.model.PlayerEntity;
import com.gunesakkaya.resthiophagame.api.repository.PlayerRepository;
import com.gunesakkaya.resthiophagame.entity.Player;
import com.gunesakkaya.resthiophagame.main.GamePanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private GamePanel gamePanel;

    @Autowired
    private PlayerRepository playerRepository;

    public Player getPlayer() {
        return gamePanel.player;
    }


    public PlayerEntity getOrCreatePlayerEntity() {
        // Örnek: tek bir oyuncu kaydı varsa onu getir, yoksa oluştur
        return playerRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> playerRepository.save(new PlayerEntity()));
    }

    public PlayerEntity savePlayerEntity(PlayerEntity playerEntity) {
        return playerRepository.save(playerEntity);
    }
}
