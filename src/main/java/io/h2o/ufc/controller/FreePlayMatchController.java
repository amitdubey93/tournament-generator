package io.h2o.ufc.controller;

import io.h2o.ufc.model.FreePlayMatch;
import io.h2o.ufc.model.Player;
import io.h2o.ufc.service.FreePlayMatchService;
import io.h2o.ufc.service.PlayerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class FreePlayMatchController {

    @Autowired
    private FreePlayMatchService freePlayMatchService;

    @Autowired
    private PlayerService playerService;

    @GetMapping("/freePlay")
    public String getFreePlayMatchList(FreePlayMatch freePlayMatch, Model model) {
        Map<Integer, Player> playerMap = playerService.findAll().stream().collect(Collectors
                .toMap(Player::getPlayerId, player -> player));

        Collection<FreePlayMatch> freePlayList = freePlayMatchService.findAll();
        freePlayList.stream().forEach(match ->
        {
            match.setPlayerOneImagePath("../" + playerMap.get(match.getPlayerOneId()).getImagePath());
            match.setPlayerTwoImagePath("../" + playerMap.get(match.getPlayerTwoId()).getImagePath());
            match.setPlayerOneName(playerMap.get(match.getPlayerOneId()).getPlayerName());
            match.setPlayerTwoName(playerMap.get(match.getPlayerTwoId()).getPlayerName());
            String winner = match.getWinner() == 0 ? "Match Pending" : playerMap.get(match.getWinner()).getPlayerName();
            match.setWinnerName(winner);
            //System.err.println(playerMap.get(match.getPlayerOne()).getPlayerName());
            //System.err.println(match);
        });
        System.err.println("freePlayList " + freePlayList);
//        model.addAttribute("freePlayList", freePlayMatchService.findAll());
//        model.addAttribute("freePlayList", freePlayMatchService.findAllOrderByFreePlayId());
        model.addAttribute("freePlayList", freePlayList);
        model.addAttribute("playerList", playerService.findAll());
        return "freeplay";
    }


    @PostMapping("/freePlay")
    public String createFreePlayMatch(@ModelAttribute("freePlayMatch") FreePlayMatch freePlayMatch, BindingResult bindingResult, Model model) {

        freePlayMatchService.save(freePlayMatch);
        return "redirect:freePlay";
    }

    @PutMapping("/freePlay")
    @Transactional
    public String updateMatchScore(FreePlayMatch match, BindingResult result, Model model) {
//        System.err.println(match);

        int freePlayId = match.getFreePlayId();
//        int tourId = match.getTourId();
        int playerOneId = match.getPlayerOneId();
        int playerTwoId = match.getPlayerTwoId();
        int playerOneScore = match.getPlayerOneScore();
        int playerTwoScore = match.getPlayerTwoScore();

        int winnerId = playerOneScore > playerTwoScore ? playerOneId : playerTwoId;
        int loserId = playerOneScore < playerTwoScore ? playerOneId : playerTwoId;
        int winnerScore = Math.max(playerOneScore, playerTwoScore);
        int loserScore = Math.min(playerOneScore, playerTwoScore);

//        System.err.println("winnerId:: "+winnerId);
//        System.err.println("loserId:: "+loserId);
//        System.err.println("winnerScore:: "+winnerScore);
//        System.err.println("loserScore:: "+loserScore);

        int x1 = freePlayMatchService.updateFreePlayMatchScore(playerOneScore, playerTwoScore, winnerId, freePlayId);


        System.err.println("X!:   " + x1);
        return "redirect:freePlay";
    }
}
