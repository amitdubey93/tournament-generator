package io.h2o.ufc.service;

import io.h2o.ufc.dto.DailyFreePlayMatchCount;
import io.h2o.ufc.dto.PVPStats;
import io.h2o.ufc.model.FreePlayMatch;
import io.h2o.ufc.model.Player;
import io.h2o.ufc.repository.FreePlayMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FreePlayMatchService {
    @Autowired
    private FreePlayMatchRepository freePlayMatchRepository;

    @Autowired
    private PlayerService playerService;

    public List<FreePlayMatch> findAll() {
        return freePlayMatchRepository.findAll(Sort.by("freePlayId"));
    }
//    public List<FreePlayMatch> findAllOrdered(){
//        return freePlayMatchRepository.findAllOrdered();
//    }
//    public List<FreePlayMatch> findAllOrderByFreePlayId(){
//        return freePlayMatchRepository.findAllOrderByFreePlayId();
//    }

    public FreePlayMatch save(FreePlayMatch freePlayMatch) {
        return freePlayMatchRepository.save(freePlayMatch);
    }

    public int updateFreePlayMatchScore(int playerOneScore, int playerTwoScore, int winner, int freePlayId) {
        return freePlayMatchRepository.updateFreePlayMatchScore(playerOneScore, playerTwoScore, winner, freePlayId, new Date());
    }

    public Player getPlayerFreePlayData(int playerId) {
        return freePlayMatchRepository.getPlayerFreePlayData(playerId);
    }


    public List<DailyFreePlayMatchCount> getDailyFreePlayMatchCounts() {
        return freePlayMatchRepository.getDailyFreePlayMatchCounts();
    }

    public List<PVPStats> getPlayerCompleteStat(int playerOneId) {

        DecimalFormat df = new DecimalFormat("#.##");
        df.setMaximumFractionDigits(2);

        Map<Integer, Player> playerMap = playerService.findAll().stream().collect(Collectors
                .toMap(Player::getPlayerId, player -> player));

        List<PVPStats> pvpStatsList = new ArrayList<>();
        List<Integer> vsPlayerList = freePlayMatchRepository.getPlayerOneVsPlayerList(playerOneId);

        System.err.println("vsPlayerList>>> " + vsPlayerList);

        for (int playerTwoId : vsPlayerList) {
            if (playerOneId != playerTwoId) {

                Player playerOne = freePlayMatchRepository.getPlayerVsPlayerFreePlayData(playerOneId, playerTwoId);
                Player playerTwo = freePlayMatchRepository.getPlayerVsPlayerFreePlayData(playerTwoId, playerOneId);


                playerOne.setPlayerName(playerMap.get(playerOneId).getPlayerName());
                playerOne.setPlayerId(playerMap.get(playerOneId).getPlayerId());
                playerOne.setImagePath("../" + playerMap.get(playerOneId).getImagePath());

                float winPercent1 = ((float) playerOne.getTotalWins() / (playerOne.getMatchPlayed() == 0 ? 1 : playerOne.getMatchPlayed())) * 100;
                float avgScore1 = (float) playerOne.getScore() / (playerOne.getMatchPlayed() == 0 ? 1 : playerOne.getMatchPlayed());
                playerOne.setWinPercent(Float.parseFloat(df.format(winPercent1)));
                playerOne.setAvgScore(Float.parseFloat(df.format(avgScore1)));

                playerTwo.setPlayerName(playerMap.get(playerTwoId).getPlayerName());
                playerTwo.setPlayerId(playerMap.get(playerTwoId).getPlayerId());
                playerTwo.setImagePath("../" + playerMap.get(playerTwoId).getImagePath());

                float winPercent2 = ((float) playerTwo.getTotalWins() / (playerTwo.getMatchPlayed() == 0 ? 1 : playerTwo.getMatchPlayed())) * 100;
                float avgScore2 = (float) playerTwo.getScore() / (playerTwo.getMatchPlayed() == 0 ? 1 : playerTwo.getMatchPlayed());
                playerTwo.setWinPercent(Float.parseFloat(df.format(winPercent2)));
                playerTwo.setAvgScore(Float.parseFloat(df.format(avgScore2)));

                float winMargin;
                String winningPlayerName;
                if (avgScore1 > avgScore2) {
                    winMargin = (avgScore1 - avgScore2) * 10;
                    winningPlayerName = playerOne.getPlayerName();
                } else {
                    winMargin = (avgScore2 - avgScore1) * 10;
                    winningPlayerName = playerTwo.getPlayerName();
                }

                List<FreePlayMatch> freePlayList = freePlayMatchRepository.getPlayerVsPlayerFreePlayMatchList(playerOneId, playerTwoId);
                freePlayList.stream().forEach(match ->
                {
                    match.setPlayerOneImagePath("../" + playerMap.get(match.getPlayerOneId()).getImagePath());
                    match.setPlayerTwoImagePath("../" + playerMap.get(match.getPlayerTwoId()).getImagePath());
                    match.setPlayerOneName(playerMap.get(match.getPlayerOneId()).getPlayerName());
                    match.setPlayerTwoName(playerMap.get(match.getPlayerTwoId()).getPlayerName());
                    match.setWinnerName(playerMap.get(match.getWinner()).getPlayerName());
                });
                //System.err.println("freePlayList " + freePlayList);

                pvpStatsList.add(
                        new PVPStats(
                                playerOne,
                                playerTwo,
                                winningPlayerName,
                                Float.parseFloat(df.format(winMargin)),
                                freePlayList
                        ));
            }
        }
//        freePlayMatchRepository.getPlayerVsPlayerData(playerOneId, playerTwoId);
        return pvpStatsList;
    }
}
