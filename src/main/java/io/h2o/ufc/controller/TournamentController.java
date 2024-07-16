package io.h2o.ufc.controller;

import io.h2o.ufc.ScheduleGenerator;
import io.h2o.ufc.Utility;
import io.h2o.ufc.model.Player;
import io.h2o.ufc.model.PointsTable;
import io.h2o.ufc.model.Tournament;
import io.h2o.ufc.model.TournamentMatch;
import io.h2o.ufc.service.PlayerService;
import io.h2o.ufc.service.PointsTableService;
import io.h2o.ufc.service.TournamentMatchService;
import io.h2o.ufc.service.TournamentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TournamentMatchService tournamentMatchService;

    @Autowired
    private PointsTableService pointsTableService;



    @GetMapping("/tournament")
    public String tournaments(Tournament tournament, Model model) {

        List<Tournament> tournamentList = tournamentService.findAll();
        Map<Integer, Player> playerMap = playerService.findAll().stream().collect(Collectors
                .toMap(Player::getPlayerId, player -> player));
        tournamentList.stream().forEach(tournament1 -> {
//            boolean completedFlag = tournament1.getTournamentMatchList().stream()
//                    .filter(tournamentMatch -> tournamentMatch.getWinner() == 0).count() == 0;
//            tournament1.setAllMatchesCompleted(completedFlag);
//            tournament1.setPlayerCount(tournament1.getPointsTable().size());

            //change this approach || getting all players from db. only in need of one players name
            if (tournament1.getWinner() != 0) {
                tournament1.setWinnerName(playerMap.get(tournament1.getWinner()).getPlayerName());
            }
        });

        model.addAttribute("tournamentList", tournamentList);
        //model.addAttribute("matchList", null);
        model.addAttribute("playerList", playerService.getPlayerList());
        return "tournament";
    }

    @GetMapping("/tournament/{id}")
    public String tournamentInfo(@PathVariable("id") int id, Model model) {
//        Map<String, String> images = playerService.findAll().stream().collect(Collectors
//                .toMap(player -> player.getPlayerName(),  player -> player.getImagePath()));
        Map<Integer, Player> playerMap = playerService.findAll().stream().collect(Collectors
                .toMap(Player::getPlayerId, player -> player));

        //System.err.println("playerMap:: "+playerMap);
        Tournament tournament = tournamentService.findById(id);
        Collection<TournamentMatch> tournamentMatchList = tournament.getTournamentMatchList();


        //Collection<TournamentMatch> tournamentFinalsMatchList = tournamentMatchList.stream().filter(tournamentMatch -> tournamentMatch.getRoundNo() != 2001).toList();
        tournamentMatchList.stream().forEach(match ->
       {
           match.setPlayerOneImagePath(Utility.UPLOAD_DIRECTORY + playerMap.get(match.getPlayerOneId()).getImagePath());
           match.setPlayerTwoImagePath(Utility.UPLOAD_DIRECTORY + playerMap.get(match.getPlayerTwoId()).getImagePath());
           match.setPlayerOneName(playerMap.get(match.getPlayerOneId()).getPlayerName());
           match.setPlayerTwoName(playerMap.get(match.getPlayerTwoId()).getPlayerName());
           String winner = match.getWinner() == 0 ? "Match Pending" : playerMap.get(match.getWinner()).getPlayerName();
           match.setWinnerName(winner);
           //System.err.println(playerMap.get(match.getPlayerOne()).getPlayerName());
           //System.err.println(match);
       });

        Collection<PointsTable> pointsTable = tournament.getPointsTable().stream().sorted(
                (pt1, pt2) -> Integer.compare(pt2.getScore(), pt1.getScore())).toList();

        pointsTable.stream().forEach(pt -> pt.setPlayerName(playerMap.get(pt.getPlayerId()).getPlayerName()));
//        System.err.println(matchList);
//        System.err.println(pointsTable);

        model.addAttribute("tournamentId", tournament.getTournamentId());
        model.addAttribute("tournamentName", tournament.getTournamentName());
        model.addAttribute("tournamentDate", tournament.getTournamentDate());
//        model.addAttribute("duration", tournament.getDuration());
        model.addAttribute("playerCount", tournament.getPlayerCount());
        model.addAttribute("playerParticipated", tournament.getPointsTable().size());
        model.addAttribute("totalMatches", tournamentMatchList.size());
        model.addAttribute("completedMatches", tournamentMatchList.stream().filter(match -> match.getWinner() != 0).count());

//        Collection<TournamentMatch> tournamentLeagueMatchList = tournamentMatchList.stream()
//                .filter(tournamentMatch -> tournamentMatch.getRoundNo() != 1001
//                        && tournamentMatch.getRoundNo() != 1002
//                        && tournamentMatch.getRoundNo() != 2001)
//                .toList();
        Collection<TournamentMatch> tournamentLeagueMatchList = tournamentMatchList.stream()
                .filter(tournamentMatch -> tournamentMatch.getRoundNo() < 1000).toList();
        model.addAttribute("tournamentLeagueMatchList", tournamentLeagueMatchList);

//        Collection<TournamentMatch> tournamentFinalsMatchList = tournamentMatchList.stream()
//                .filter(tournamentMatch -> tournamentMatch.getRoundNo() == 1001
//                        || tournamentMatch.getRoundNo() == 1002
//                        || tournamentMatch.getRoundNo() == 2001)
//                .toList();
        Collection<TournamentMatch> tournamentFinalsMatchList = tournamentMatchList.stream()
                .filter(tournamentMatch -> tournamentMatch.getRoundNo() > 1000).toList();
        model.addAttribute("tournamentFinalsMatchList", tournamentFinalsMatchList);

        model.addAttribute("pointsTable", pointsTable);

        model.addAttribute("tournamentMatch", new TournamentMatch());
//        log.info("tournamentLeagueMatchList::  " + tournamentLeagueMatchList);
//        log.info("tournamentFinalsMatchList::  " + tournamentFinalsMatchList);
        return "tournament_info";
    }

    @PostMapping("/tournament")
    public String createTournament(@Valid @ModelAttribute("tournament") Tournament tournament, BindingResult bindingResult, Model model) {
        System.err.println(bindingResult.hasErrors());
        //System.err.println(bindingResult);
        log.info("bindingResult:: "+bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:tournament";
        }

        System.err.println(tournament.getPlayerList());
        ScheduleGenerator generator = new ScheduleGenerator();
        List<Integer> players = tournament.getPlayerList().stream().map(Player::getPlayerId).toList();
//        List<Integer> players = playerService.findAll().stream().map(Player::getPlayerId).toList();
        //List<String> players = List.of("Amit", "Ananya", "Suryansh", "Upendra", "Shivam", "Biswajeet");
        //System.err.println(players);
        log.info("players:: "+players);
        List<TournamentMatch> schedule = generator.generateSchedule(tournament, players);
        List<PointsTable> pointsTable = tournament.getPlayerList().stream().map(player -> {
            return new PointsTable().builder()
                    .playerId((int) player.getPlayerId())
                    .tournament(tournament)
                    .matchPlayed(0)
                    .won(0)
                    .lost(0)
                    .score(0)
                    .build();
        }).toList();

        tournament.setTournamentMatchList(schedule);
        tournament.setPointsTable(pointsTable);
        tournament.setTournamentDate(new Date());
        tournament.setPlayerCount(pointsTable.size());
        tournamentService.save(tournament);
        model.addAttribute("tournamentList", tournamentService.findAll());
        //model.addAttribute("matchList", null);
        log.info("Create Tournament Request");
        return "redirect:tournament";
    }


}
