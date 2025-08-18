package io.h2o.ufc.controller;

import io.h2o.ufc.Utility;
import io.h2o.ufc.dto.PVPStats;
import io.h2o.ufc.dto.PlayerStatsByGameTypeDTO;
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


    @GetMapping("/player_stats1")
    public String getPlayerStats1(Model model) {

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

    @GetMapping("/stats")
    public String getPlayerStats(Model model) {

        List<PlayerStatsByGameTypeDTO> playerFreePlayList = freePlayMatchService.getFreePlayStats();
        List<PlayerStatsByGameTypeDTO> playerTournamentList = freePlayMatchService.getTournamentStats();

        model.addAttribute("playerFreePlayList", playerFreePlayList);
        model.addAttribute("playerTournamentList", playerTournamentList);
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
        List<Player> playerList = playerService.getPlayerList();
        model.addAttribute("playerList", playerList);

        MultipartFile multipartFile = player.getPlayerImageFile();
        System.err.println("multipartFile.getBytes().length>> " + multipartFile.getBytes().length);
//        System.err.println("multipartFile size:: "+multipartFile.getSize());
//        ObjectError playerImageSizeError = new ObjectError("playerImage","playerImage size should be less than 50 kb");
//        ObjectError playerImageEmptyError = new ObjectError("playerImage","Image Empty!!!");
        if (multipartFile.getBytes().length > 1024 * 50) {
//            bindingResult.addError(playerImageSizeError);
            bindingResult.rejectValue("playerImageFile", "file.size", "Image size should be less than 50 kb.");
//            bindingResult.getAllErrors().stream().forEach(System.out::println);
//            System.err.println("ErrorCount:: "+bindingResult.getErrorCount());
//            System.err.println("FieldErrors:: "+bindingResult.getFieldErrors());
//            System.err.println("Model:: "+bindingResult.getModel());
//            System.err.println("ObjectName:: "+bindingResult.getObjectName());
//            System.err.println("Class:: "+bindingResult.getClass());
//            return "player";
        }
        if (multipartFile.getBytes().length == 0) {
//            bindingResult.addError(playerImageEmptyError);
            bindingResult.rejectValue("playerImageFile", "file.empty", "Please select an Image.");
//            bindingResult.getAllErrors().stream().forEach(System.out::println);
//            System.err.println("ErrorCount:: "+bindingResult.getErrorCount());
//            System.err.println("FieldErrors:: "+bindingResult.getFieldErrors());
//            System.err.println("Model:: "+bindingResult.getModel());
//            System.err.println("ObjectName:: "+bindingResult.getObjectName());
//            System.err.println("Class:: "+bindingResult.getClass());
//            return "player";
        }
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().stream().forEach(System.err::println);
            return "player";
        }
//        String fileName = fileStorageService.storeFile(multipartFile);


        Player player1 = playerService.save(player);
        System.err.println("saving player   :: " + player1);

        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(Utility.UPLOAD_DIRECTORY, "/images/" + player1.getPlayerId() + ".jpg");
        fileNames.append(player1.getPlayerId() + ".jpg");
        Files.write(fileNameAndPath, multipartFile.getBytes());
        System.err.println(fileNames);
//        redirectAttributes.addAttribute("msg", "Uploaded images: " + fileName.toString());
        player1.setImagePath("/uploads/images/" + player1.getPlayerId() + ".jpg");
        playerService.save(player1);


        redirectAttributes.addFlashAttribute("msg", "Images Uploaded!! ");
        return "redirect:/player";
    }

    @GetMapping("/player/{playerId}")
    public String getAllPlayers(@PathVariable("playerId") int playerId, Model model) {

        DecimalFormat df = new DecimalFormat("#.##");
        df.setMaximumFractionDigits(2);

        Map<Integer, Player> playerMap = playerService.findAll().stream().collect(Collectors
                .toMap(Player::getPlayerId, player -> player));


        Player player = freePlayMatchService.getPlayerFreePlayData(playerId);
        player.setPlayerName(playerMap.get(playerId).getPlayerName());
        player.setImagePath(playerMap.get(playerId).getImagePath());

        float avgScore = (float) player.getScore() / (player.getMatchPlayed() == 0 ? 1 : player.getMatchPlayed());
        float oppAvgScore = (float) player.getOppScore() / (player.getMatchPlayed() == 0 ? 1 : player.getMatchPlayed());
        float winPercent = ((float) player.getTotalWins() / (player.getMatchPlayed() == 0 ? 1 : player.getMatchPlayed())) * 100;
        float scoreMargin = (avgScore - oppAvgScore) * 10;

        player.setAvgScore(Float.parseFloat(df.format(avgScore)));
        player.setWinPercent(Float.parseFloat(df.format(winPercent)));

        player.setOppAvgScore(Float.parseFloat(df.format(oppAvgScore)));
        player.setScoreMargin(Float.parseFloat(df.format(scoreMargin)));

        System.err.println(player);

        List<PlayerStatsByGameTypeDTO> playerStatsByGameType = freePlayMatchService.getPlayerStatsByGameType(playerId);
        playerStatsByGameType.stream().forEach(playerStatsDTO -> {
            playerStatsDTO.setGameTypeName(Utility.getGameType().get(playerStatsDTO.getGameType()));
        });

        List<PVPStats> pvpCompleteStat = freePlayMatchService.getPvpCompleteStat(playerId);
//        System.err.println("playerStatsByGameType::>> "+playerStatsByGameType);
//        System.err.println("pvpCompleteStat::>> "+pvpCompleteStat);

//        playerStatsByGameType.stream().forEach(System.err::println);
//        pvpCompleteStat.stream().forEach(pvpStats -> {
//            pvpStats.getPvpStatsByGameTypeList().stream().forEach(System.out::println);
//            pvpStats.getFreePlayMatchList().stream().forEach(System.err::println);
//        });

        model.addAttribute("playerStat", player);
        model.addAttribute("playerStatsByGameType", playerStatsByGameType);
        model.addAttribute("pvpCompleteStat", pvpCompleteStat);
        return "player_info_2";
    }
}
