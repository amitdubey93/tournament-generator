package io.h2o.ufc.controller;

import io.h2o.ufc.ScheduleGenerator;
import io.h2o.ufc.model.Match;
import io.h2o.ufc.model.Player;
import io.h2o.ufc.model.PointsTable;
import io.h2o.ufc.model.Tournament;
import io.h2o.ufc.service.MatchService;
import io.h2o.ufc.service.PlayerService;
import io.h2o.ufc.service.PointsTableService;
import io.h2o.ufc.service.TournamentService;
//import jakarta.validation.Valid;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private PointsTableService pointsTableService;



    @GetMapping("/tournament")
    public String tournaments(Tournament tournament, Model model) {
        model.addAttribute("tournamentList", tournamentService.findAll());
        model.addAttribute("matchList", null);
        return "tournament";
    }

    @GetMapping("/tournament/{id}")
    public String tournamentInfo(@PathVariable("id") int id, Model model) {
//        Map<String, String> images = playerService.findAll().stream().collect(Collectors
//                .toMap(player -> player.getPlayerName(),  player -> player.getImagePath()));
        Map<Integer, Player> playerMap = playerService.findAll().stream().collect(Collectors
                .toMap(Player::getPlayerId, player -> player));

        System.err.println("playerMap:: "+playerMap);
        Tournament tournament = tournamentService.findById(id);
        Collection<Match> matchList = tournament.getMatchList();


       matchList.stream().forEach(match ->
       {
           match.setPlayerOneImagePath("../"+playerMap.get(match.getPlayerOneId()).getImagePath());
           match.setPlayerTwoImagePath("../"+playerMap.get(match.getPlayerTwoId()).getImagePath());
           match.setPlayerOneName(playerMap.get(match.getPlayerOneId()).getPlayerName());
           match.setPlayerTwoName(playerMap.get(match.getPlayerTwoId()).getPlayerName());
           String winner = match.getWinner() == 0 ? "Match Pending" : playerMap.get(match.getWinner()).getPlayerName();
           match.setWinnerName(winner);
           //System.err.println(playerMap.get(match.getPlayerOne()).getPlayerName());
           System.err.println(match);
       });

        Collection<PointsTable> pointsTable = tournament.getPointsTable().stream().sorted(
                (pt1, pt2) -> Integer.compare(pt2.getScore(), pt1.getScore())).toList();

        pointsTable.stream().forEach(pt -> pt.setPlayerName(playerMap.get(pt.getPlayerId()).getPlayerName()));
        System.err.println(matchList);
        System.err.println(pointsTable);

        model.addAttribute("tournamentId", tournament.getTournamentId());
        model.addAttribute("tournamentName", tournament.getTournamentName());
        model.addAttribute("duration", tournament.getDuration());
        model.addAttribute("playerCount", tournament.getPlayerCount());
        model.addAttribute("totalMatches", matchList.size());
        model.addAttribute("playedMatches", matchList.stream().filter(match -> match.getWinner() != 0).count());

        model.addAttribute("matchList", matchList);
        model.addAttribute("pointsTable", pointsTable);

        model.addAttribute("match", new Match());
        return "match";
    }

    @PostMapping("/tournament")
    public String createTournament(@Valid @ModelAttribute("tournament") Tournament tournament, BindingResult bindingResult, Model model) {
        System.err.println(bindingResult.hasErrors());
        System.err.println(bindingResult);
        if (bindingResult.hasErrors()) {
            return "";
        }
        //System.err.println(tournament);
        ScheduleGenerator generator = new ScheduleGenerator();
        List<Integer> players = playerService.findAll().stream().map(Player::getPlayerId).toList();
        //List<String> players = List.of("Amit", "Ananya", "Suryansh", "Upendra", "Shivam", "Biswajeet");
        //System.err.println(players);
        List<Match> schedule = generator.generateSchedule(tournament, players);
        List<PointsTable> pointsTable = playerService.findAll().stream().map(player -> {
            return new PointsTable().builder()
                    .playerId(player.getPlayerId())
                    .tournament(tournament)
                    .matchPlayed(0)
                    .won(0)
                    .lost(0)
                    .score(0)
                    .build();
        }).toList();

        tournament.setMatchList(schedule);
        tournament.setPointsTable(pointsTable);
        tournamentService.save(tournament);
        model.addAttribute("tournamentList", tournamentService.findAll());
        //model.addAttribute("matchList", null);
        return "redirect:tournament";
    }

    @PostMapping("/match/update")
    @Transactional
    public String updateMatchScore(@Valid Match match, BindingResult result, Model model){
        System.err.println(match);

        int matchId = match.getMatchId();
        int tourId = match.getTourId();
        int playerOneId = match.getPlayerOneId();
        int playerTwoId = match.getPlayerTwoId();
        int playerOneScore = match.getPlayerOneScore();
        int playerTwoScore = match.getPlayerTwoScore();

        int winnerId = playerOneScore > playerTwoScore ? playerOneId : playerTwoId;
        int loserId = playerOneScore < playerTwoScore ? playerOneId : playerTwoId;
        int winnerScore = Math.max(playerOneScore, playerTwoScore);
        int loserScore = Math.min(playerOneScore, playerTwoScore);

        System.err.println("winnerId:: "+winnerId);
        System.err.println("loserId:: "+loserId);
        System.err.println("winnerScore:: "+winnerScore);
        System.err.println("loserScore:: "+loserScore);

        int x1 = matchService.updateMatchScore(playerOneScore, playerTwoScore, winnerId, matchId);
        int x2 = pointsTableService.updatePointsTablePlayerWinStats(winnerScore, winnerId, tourId);
        int x3 = pointsTableService.updatePointsTablePlayerLossStats(loserScore, loserId, tourId);
        int x4 = playerService.updatePlayerWinStats(winnerScore, winnerId);
        int x5 = playerService.updatePlayerLossStats(loserScore, loserId);

        System.err.println("X!:   "+x1+x2+x3+x4+x5);
        return "redirect:/tournament/"+tourId;
    }
}
