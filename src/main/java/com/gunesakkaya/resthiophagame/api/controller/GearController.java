package com.gunesakkaya.resthiophagame.api.controller;
import java.util.List;
import com.gunesakkaya.resthiophagame.api.model.Gear;
import com.gunesakkaya.resthiophagame.api.service.GearService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
public class GearController {

    private final GearService gearService;

    public GearController(GearService gearService) {
        this.gearService = gearService;
    }

    @GetMapping("/random")
    public Gear getRandomItem() {
        System.out.println(">>> GET /api/items/random endpoint triggered");
        return gearService.getRandomLoot();
    }
//
//    @GetMapping
//    public List<Gear> getAllItems() {
//        return gearService.getAllItems();
//    }


}

