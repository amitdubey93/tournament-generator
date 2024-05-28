package io.h2o.ufc.controller;

import io.h2o.ufc.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @GetMapping("/player")
    public String getAllPlayers(Model model) {
        System.out.println(playerService.findAll());
        model.addAttribute("playerList", playerService.findAll());
        return "player";
    }
}
