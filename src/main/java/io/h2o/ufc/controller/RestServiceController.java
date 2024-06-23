package io.h2o.ufc.controller;

import io.h2o.ufc.model.Player;
import io.h2o.ufc.model.PointsTable;
import io.h2o.ufc.model.TournamentMatch;
import io.h2o.ufc.service.PlayerService;
import io.h2o.ufc.service.PointsTableService;
import io.h2o.ufc.service.TournamentMatchService;
import io.h2o.ufc.service.TournamentService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins="http://localhost:8080")
@Slf4j
public class RestServiceController {

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


    @PutMapping("/tournament/finalMatch")
    @Transactional
    public ResponseEntity<?> updateFinalMatchScore(@RequestBody TournamentMatch tournamentMatch) {


        log.info("Request on rest controller");
        System.err.println("Request on rest controller tournamentMatch::  " + tournamentMatch);

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

        System.err.println("tourId:: " + tourId);
        System.err.println("winnerId:: " + winnerId);
        System.err.println("loserId:: " + loserId);
        System.err.println("winnerScore:: " + winnerScore);
        System.err.println("loserScore:: " + loserScore);

        int x1 = tournamentMatchService.updateTournamentMatchScore(playerOneScore, playerTwoScore, winnerId, (int) matchId);
        int x2 = pointsTableService.updatePointsTablePlayerWinStats(winnerScore, winnerId, tourId);
        int x3 = pointsTableService.updatePointsTablePlayerLossStats(loserScore, loserId, tourId);
        int x4 = playerService.updatePlayerWinStats(winnerScore, winnerId);
        int x5 = playerService.updatePlayerLossStats(loserScore, loserId);
        System.err.println("X!:   " + x1 + x2 + x3 + x4 + x5);

        boolean leagueMatchesCompletedFlag = tournamentMatchService.checkIfAllLeagueMatchesCompleted(tourId);
        List<PointsTable> pointsTable = pointsTableService.findAllByTournamentId(tourId);

        if (tournamentMatch.getRoundNo() == FINAL) {
            boolean flag5 = tournamentService.updateFinalWinner(winnerId, tourId);
            System.err.println("finals winner declared:: " + flag5);
        }

        Player player = playerService.findByPlayerId(winnerId);
        //log.error("Update Tournament Final Match Score Request");
        //log.error("pointsTable::  "+pointsTable);
        //log.info("player::  "+player);
        //System.err.println("pointsTable:  "+pointsTable);
        log.info("Update Tournament Final Match Score Request");
        log.info("leagueMatchesCompletedFlag::   " + leagueMatchesCompletedFlag);


        tournamentMatch.setWinner(winnerId);
        tournamentMatch.setWinnerName(player.getPlayerName());
        return ResponseEntity.status(HttpStatus.CREATED).body(tournamentMatch);
    }

    @GetMapping("/tournament/pointsTable/{tourId}")
    public ResponseEntity<?> getPointsTable(@PathVariable("tourId") int tourId) {
        List<PointsTable> pointsTables = pointsTableService.findAllByTournamentId(tourId);
        pointsTables.stream().forEach(pointsTable -> {
            pointsTable.setPlayerName(playerService.findByPlayerId(pointsTable.getPlayerId()).getPlayerName());
        });
        System.err.println(pointsTables);
        log.info("pointsTables::  " + pointsTables);
        return ResponseEntity.status(HttpStatus.OK).body(pointsTables);
    }

}
