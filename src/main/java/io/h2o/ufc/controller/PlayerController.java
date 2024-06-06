package io.h2o.ufc.controller;

import io.h2o.ufc.model.Player;
import io.h2o.ufc.service.FreePlayMatchService;
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

    @Autowired
    private FreePlayMatchService freePlayMatchService;

    @GetMapping("/player")
    public String getAllPlayers(Model model) {
//        System.out.println(playerService.findAll());


        List<Player> playerFreePlayList = playerService.findAll();
        playerFreePlayList.stream().forEach(player -> {
            Player player1 = freePlayMatchService.getPlayerData(player.getPlayerId());
            player.setScore(player1.getScore());
            player.setMatchPlayed(player1.getMatchPlayed());
            player.setTotalWins(player1.getTotalWins());
            player.setWinPercent(player1.getWinPercent());
        });
        List<Player> playerTournamentList = pointsTableService.getPlayerData();
        //List<Player> playerFreePlayList = new ArrayList<>();
//        Player player = freePlayMatchService.getPlayerData(playerId);
        model.addAttribute("playerTournamentList", playerTournamentList);
        model.addAttribute("playerFreePlayList", playerFreePlayList);
        return "player";
    }
}
