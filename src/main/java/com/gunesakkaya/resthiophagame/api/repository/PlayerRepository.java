package com.gunesakkaya.resthiophagame.api.repository;

import com.gunesakkaya.resthiophagame.api.model.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
}
