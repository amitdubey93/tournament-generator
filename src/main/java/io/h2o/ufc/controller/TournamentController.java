package io.h2o.ufc.controller;

import io.h2o.ufc.ScheduleGenerator;
import io.h2o.ufc.model.Match;
import io.h2o.ufc.model.Player;
import io.h2o.ufc.model.PointsTable;
import io.h2o.ufc.model.Tournament;
import io.h2o.ufc.service.MatchService;
import io.h2o.ufc.service.PlayerService;
import io.h2o.ufc.service.TournamentService;
//import jakarta.validation.Valid;
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



    @GetMapping("/tournament")
    public String tournaments(Tournament tournament, Model model) {
        model.addAttribute("tournamentList", tournamentService.findAll());
        model.addAttribute("matchList", null);
        return "tournament";
    }

    @GetMapping("/tournament/{id}")
    public String tournamentInfo(@PathVariable("id") int id, Model model) {
        Map<String, String> images = playerService.findAll().stream().collect(Collectors
                .toMap(player -> player.getPlayer_name(),  player -> player.getImage_path()));

        Tournament tournament = tournamentService.findById(id);
        Collection<Match> matchList = tournament.getMatchList();
        matchList.stream().forEach(match ->
        {
            match.setPlayerOneImagePath("../"+images.get(match.getPlayerOne()));
            match.setPlayerTwoImagePath("../"+images.get(match.getPlayerTwo()));
        });

        model.addAttribute("tournamentName", tournament.getTournamentName());
        model.addAttribute("duration", tournament.getDuration());
        model.addAttribute("playerCount", tournament.getPlayerCount());

        model.addAttribute("matchList", matchList);
        model.addAttribute("pointsTable", tournament.getPointsTable().stream().sorted(
                (pt1, pt2) -> Integer.compare(pt2.getScore(), pt1.getScore()))
        );

        return "match";
    }

    @PostMapping("/tournament")
    public String createTournament(@Valid Tournament tournament, BindingResult result, Model model) {

        //System.err.println(tournament);
        ScheduleGenerator generator = new ScheduleGenerator();
        List<String> players = playerService.findAll().stream().map(Player::getPlayer_name).toList();
        //List<String> players = List.of("Amit", "Ananya", "Suryansh", "Upendra", "Shivam", "Biswajeet");
        //System.err.println(players);
        List<Match> schedule = generator.generateSchedule(tournament, players);
        List<PointsTable> pointsTable = playerService.findAll().stream().map(player -> {
            return new PointsTable().builder()
                    .playerName(player.getPlayer_name())
                    .tournament(tournament)
                    .matchesPlayed(0)
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

}
