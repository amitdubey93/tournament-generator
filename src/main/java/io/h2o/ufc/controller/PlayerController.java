package io.h2o.ufc.controller;

import io.h2o.ufc.Utility;
import io.h2o.ufc.dto.PVPStats;
import io.h2o.ufc.model.Player;
import io.h2o.ufc.service.FreePlayMatchService;
import io.h2o.ufc.service.PlayerService;
import io.h2o.ufc.service.PointsTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PlayerController {

    //    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    //public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/images";
    @Autowired
    private PlayerService playerService;

    @Autowired
    private PointsTableService pointsTableService;

    @Autowired
    private FreePlayMatchService freePlayMatchService;

//    @Autowired
//    private FileStorageService fileStorageService;
//    @Autowired
//    ServletContext servletContext;
//    private ServletWebServerApplicationContext servletContext;


    @GetMapping("/player_stats")
    public String getAllPlayers(Model model) {
//        System.out.println(playerService.findAll());

        DecimalFormat df = new DecimalFormat("#.##");
        df.setMaximumFractionDigits(2);

        List<Player> playerFreePlayList = playerService.findAll().stream()
                .filter(player -> player.getPlayerId() != 2001 && player.getPlayerId() != 2002).toList();

        playerFreePlayList.stream().forEach(player -> {

            Player player1 = freePlayMatchService.getPlayerFreePlayData(player.getPlayerId());

            player.setScore(player1.getScore());
            player.setOppScore(player1.getOppScore());
            player.setMatchPlayed(player1.getMatchPlayed());
            player.setTotalWins(player1.getTotalWins());

            float winPercent = ((float) player1.getTotalWins() / (player1.getMatchPlayed() == 0 ? 1 : player1.getMatchPlayed())) * 100;
            float avgScore = (float) player1.getScore() / (player1.getMatchPlayed() == 0 ? 1 : player1.getMatchPlayed());
            float oppAvgScore = (float) player1.getOppScore() / (player1.getMatchPlayed() == 0 ? 1 : player1.getMatchPlayed());
            float scoreMargin = (avgScore - oppAvgScore) * 10;
            player.setWinPercent(Float.parseFloat(df.format(winPercent)));
            player.setAvgScore(Float.parseFloat(df.format(avgScore)));

            player.setOppAvgScore(Float.parseFloat(df.format(oppAvgScore)));
            player.setScoreMargin(Float.parseFloat(df.format(scoreMargin)));

        });

        List<Player> playerTournamentList = pointsTableService.getPlayerData().stream()
                .filter(player -> player.getPlayerId() != 2001 && player.getPlayerId() != 2002).toList();

        playerTournamentList.stream().forEach(player -> {
            float winPercent = ((float) player.getTotalWins() / (player.getMatchPlayed() == 0 ? 1 : player.getMatchPlayed())) * 100;
            float avgScore = (float) player.getScore() / (player.getMatchPlayed() == 0 ? 1 : player.getMatchPlayed());
            player.setWinPercent(Float.parseFloat(df.format(winPercent)));
            player.setAvgScore(Float.parseFloat(df.format(avgScore)));
        });
        //List<Player> playerFreePlayList = new ArrayList<>();
//        Player player = freePlayMatchService.getPlayerData(playerId);
        model.addAttribute("playerTournamentList", playerTournamentList);
        model.addAttribute("playerFreePlayList", playerFreePlayList);
        return "player_stats";
    }

    @GetMapping("/player")
    public String getPlayerPage(Player player, Model model) {
        List<Player> playerList = playerService.getPlayerList();
        model.addAttribute("playerList", playerList);
        return "player";
    }

    @PostMapping("/player")
    public String createPlayer(@Valid @ModelAttribute("player") Player player, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) throws IOException {

        Player player1 = playerService.save(player);
        System.err.println("saving player   :: " + player1);

        MultipartFile multipartFile = player.getPlayerImage();

//        String fileName = fileStorageService.storeFile(multipartFile);


        StringBuilder fileNames = new StringBuilder();
//        Path fileNameAndPath = Paths.get(Utility.UPLOAD_DIRECTORY, multipartFile.getOriginalFilename());
        Path fileNameAndPath = Paths.get(Utility.UPLOAD_DIRECTORY, "/images/" + player1.getPlayerId() + ".jpg");
        fileNames.append(player1.getPlayerId() + ".jpg");
//        fileNames.append(multipartFile.getOriginalFilename());
        Files.write(fileNameAndPath, multipartFile.getBytes());
        System.err.println("UPLOAD_DIRECTORY>> " + Utility.UPLOAD_DIRECTORY);
        System.err.println(fileNames);
//        redirectAttributes.addAttribute("msg", "Uploaded images: " + fileName.toString());
        redirectAttributes.addAttribute("msg", "Images Uploaded!! ");
        player1.setImagePath("/images/" + player1.getPlayerId() + ".jpg");
        playerService.save(player1);
        return "redirect:player";
    }

    @GetMapping("/player/{playerId}")
    public String getAllPlayers(@PathVariable("playerId") int playerId, Model model) {

        DecimalFormat df = new DecimalFormat("#.##");
        df.setMaximumFractionDigits(2);

        Map<Integer, Player> playerMap = playerService.findAll().stream().collect(Collectors
                .toMap(Player::getPlayerId, player -> player));


        Player player = freePlayMatchService.getPlayerFreePlayData(playerId);
        player.setPlayerName(playerMap.get(playerId).getPlayerName());
        player.setImagePath(Utility.UPLOAD_DIRECTORY + playerMap.get(playerId).getImagePath());

        float avgScore = (float) player.getScore() / (player.getMatchPlayed() == 0 ? 1 : player.getMatchPlayed());
        float oppAvgScore = (float) player.getOppScore() / (player.getMatchPlayed() == 0 ? 1 : player.getMatchPlayed());
        float winPercent = ((float) player.getTotalWins() / (player.getMatchPlayed() == 0 ? 1 : player.getMatchPlayed())) * 100;
        float scoreMargin = (avgScore - oppAvgScore) * 10;

        player.setAvgScore(Float.parseFloat(df.format(avgScore)));
        player.setWinPercent(Float.parseFloat(df.format(winPercent)));

        player.setOppAvgScore(Float.parseFloat(df.format(oppAvgScore)));
        player.setScoreMargin(Float.parseFloat(df.format(scoreMargin)));

        System.err.println(player);

        List<PVPStats> pvpStatsList = freePlayMatchService.getPlayerCompleteStat(playerId);
        System.err.println(pvpStatsList);

        model.addAttribute("playerStat", player);
        model.addAttribute("playerVsPlayerStatList", pvpStatsList);
        return "player_info";
    }
}
