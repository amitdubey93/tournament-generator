package io.h2o.ufc.controller;

import io.h2o.ufc.model.TournamentMatch;
import io.h2o.ufc.service.PlayerService;
import io.h2o.ufc.service.PointsTableService;
import io.h2o.ufc.service.TournamentMatchService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class TournamentMatchController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TournamentMatchService tournamentMatchService;

    @Autowired
    private PointsTableService pointsTableService;

    @PutMapping("/tournament/match")
    @Transactional
    public String updateMatchScore(@Valid TournamentMatch tournamentMatch, BindingResult result, Model model) {
//        System.err.println(match);

        long matchId = tournamentMatch.getMatchId();
        int tourId = tournamentMatch.getTourId();
        int playerOneId = tournamentMatch.getPlayerOneId();
        int playerTwoId = tournamentMatch.getPlayerTwoId();
        int playerOneScore = tournamentMatch.getPlayerOneScore();
        int playerTwoScore = tournamentMatch.getPlayerTwoScore();

        int winnerId = playerOneScore > playerTwoScore ? playerOneId : playerTwoId;
        int loserId = playerOneScore < playerTwoScore ? playerOneId : playerTwoId;
        int winnerScore = Math.max(playerOneScore, playerTwoScore);
        int loserScore = Math.min(playerOneScore, playerTwoScore);

//        System.err.println("winnerId:: "+winnerId);
//        System.err.println("loserId:: "+loserId);
//        System.err.println("winnerScore:: "+winnerScore);
//        System.err.println("loserScore:: "+loserScore);

        int x1 = tournamentMatchService.updateMatchScore(playerOneScore, playerTwoScore, winnerId, (int) matchId);
        int x2 = pointsTableService.updatePointsTablePlayerWinStats(winnerScore, winnerId, tourId);
        int x3 = pointsTableService.updatePointsTablePlayerLossStats(loserScore, loserId, tourId);
        int x4 = playerService.updatePlayerWinStats(winnerScore, winnerId);
        int x5 = playerService.updatePlayerLossStats(loserScore, loserId);

//        System.err.println("X!:   "+x1+x2+x3+x4+x5);
        return "redirect:/tournament/" + tourId;
    }


}
