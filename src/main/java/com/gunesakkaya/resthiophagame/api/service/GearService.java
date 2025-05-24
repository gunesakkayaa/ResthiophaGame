package com.gunesakkaya.resthiophagame.api.service;

import com.gunesakkaya.resthiophagame.api.model.Gear;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class GearService {

    private static final List<Gear> swordLoots = Arrays.asList(
            new Gear(Gear.Type.SWORD, 1, "Rusty Blade"),
            new Gear(Gear.Type.SWORD, 2, "Knight’s Edge"),
            new Gear(Gear.Type.SWORD, 3, "Crystal Sword")
    );

    private static final List<Gear> shoeLoots = Arrays.asList(
            new Gear(Gear.Type.SHOES, 0.5, "Worn Shoes"),
            new Gear(Gear.Type.SHOES, 1, "Leather Boots"),
            new Gear(Gear.Type.SHOES, 2, "Phantom Steps")
    );

    // Random nesnesini tanımladık
    private static final Random rand = new Random();

    public Gear getRandomLoot() {
        // sword yada shoes döndürür
        if (rand.nextBoolean()) {
            return swordLoots.get(rand.nextInt(swordLoots.size()));
        } else {
            return shoeLoots.get(rand.nextInt(shoeLoots.size()));
        }
    }

    public List<Gear> getAllItems() {
        // hem sword hem shoes'u birlikte döndürmek istersen. mesela tüm listeyi koyup mağaza açarız oyun içinde. ama şuan ihtiyacımız yok
        return Stream.concat(swordLoots.stream(), shoeLoots.stream())
                .collect(Collectors.toList());
    }
}
