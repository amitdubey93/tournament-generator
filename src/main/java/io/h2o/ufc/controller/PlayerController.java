package io.h2o.ufc.controller;

import io.h2o.ufc.model.Player;
import io.h2o.ufc.repository.MatchRepository;
import io.h2o.ufc.repository.PointsTableRepository;
import io.h2o.ufc.service.PlayerService;
import io.h2o.ufc.service.PointsTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PointsTableService pointsTableService;
    @GetMapping("/player")
    public String getAllPlayers(Model model) {
//        System.out.println(playerService.findAll());


//        List<Player> playerList = playerService.findAll();
        List<Player> playerList = pointsTableService.getPlayerData();
        model.addAttribute("playerList", playerList);
        return "player";
    }
}
