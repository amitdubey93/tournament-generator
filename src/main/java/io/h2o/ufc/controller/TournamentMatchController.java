package io.h2o.ufc.controller;

import io.h2o.ufc.model.PointsTable;
import io.h2o.ufc.model.TournamentMatch;
import io.h2o.ufc.service.PlayerService;
import io.h2o.ufc.service.PointsTableService;
import io.h2o.ufc.service.TournamentMatchService;
import io.h2o.ufc.service.TournamentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Slf4j
@Controller
public class TournamentMatchController {

    private final int SEMI_FINAL_1 = 1001;
    private final int SEMI_FINAL_2 = 1002;
    private final int FINAL = 2001;
    @Autowired
    private PlayerService playerService;

    @Autowired
    private TournamentMatchService tournamentMatchService;

    @Autowired
    private TournamentService tournamentService;

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

        int x1 = tournamentMatchService.updateTournamentMatchScore(playerOneScore, playerTwoScore, winnerId, (int) matchId);
        int x2 = pointsTableService.updatePointsTablePlayerWinStats(winnerScore, winnerId, tourId);
        int x3 = pointsTableService.updatePointsTablePlayerLossStats(loserScore, loserId, tourId);
        int x4 = playerService.updatePlayerWinStats(winnerScore, winnerId);
        int x5 = playerService.updatePlayerLossStats(loserScore, loserId);
//        System.err.println("X!:   "+x1+x2+x3+x4+x5);

        boolean leagueMatchesCompletedFlag = tournamentMatchService.checkIfAllLeagueMatchesCompleted(tourId);
        List<PointsTable> pointsTable = pointsTableService.findAllByTournamentId(tourId);

        if (pointsTable.size() <= 3) {

            boolean flag1, flag2;
            if (leagueMatchesCompletedFlag) {
                if (tournamentMatch.getRoundNo() != FINAL) {
                    flag1 = tournamentMatchService.updateEliminatorPlayers(pointsTable.get(0).getPlayerId(), pointsTable.get(1).getPlayerId(), tourId, FINAL);
                    System.err.println("finals candidates declared:: " + flag1);
                } else {
                    flag2 = tournamentService.updateFinalWinner(winnerId, tourId);
                    System.err.println("finals winner declared:: " + flag2);
                }
            }
        } else {

            boolean flag1, flag2, flag3, flag4, flag5;
            if (leagueMatchesCompletedFlag) {
                if (tournamentMatch.getRoundNo() != SEMI_FINAL_1 && tournamentMatch.getRoundNo() != SEMI_FINAL_2 && tournamentMatch.getRoundNo() != FINAL) {
                    flag1 = tournamentMatchService.updateEliminatorPlayers(pointsTable.get(0).getPlayerId(), pointsTable.get(3).getPlayerId(), tourId, SEMI_FINAL_1);
                    flag2 = tournamentMatchService.updateEliminatorPlayers(pointsTable.get(1).getPlayerId(), pointsTable.get(2).getPlayerId(), tourId, SEMI_FINAL_2);
                    System.err.println("Semifinals candidates declared::  " + flag1);
                    System.err.println("Semifinals candidates declared::  " + flag2);
                    log.info("Semifinals candidates declared");
                }

                if (tournamentMatch.getRoundNo() == SEMI_FINAL_1) {
                    flag3 = tournamentMatchService.updateFinalPlayerOne(winnerId, tourId);
                    System.err.println("finals FIRST candidate declared::  " + flag3);
                }
                if (tournamentMatch.getRoundNo() == SEMI_FINAL_2) {
                    flag4 = tournamentMatchService.updateFinalPlayerTwo(winnerId, tourId);
                    System.err.println("finals SECOND candidate declared:: " + flag4);
                }
                if (tournamentMatch.getRoundNo() == FINAL) {
                    flag5 = tournamentService.updateFinalWinner(winnerId, tourId);
                    System.err.println("finals winner declared:: " + flag5);
                }

            }
        }

        log.info("Update Tournament Match Score Request");
        log.info("leagueMatchesCompletedFlag::   " + leagueMatchesCompletedFlag);

        return "redirect:/tournament/" + tourId;
    }


}
