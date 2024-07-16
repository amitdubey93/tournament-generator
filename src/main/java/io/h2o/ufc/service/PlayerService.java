package io.h2o.ufc.service;

import io.h2o.ufc.Utility;
import io.h2o.ufc.model.Player;
import io.h2o.ufc.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> findAll(){
        return playerRepository.findAll();
    }

    public List<Player> getPlayerList() {

        List<Player> playerList = playerRepository.findAll().stream().filter(player -> player.getPlayerId() < 2000).toList();
        playerList.stream().forEach(
                player -> {
                    player.setImagePath(Utility.UPLOAD_DIRECTORY + player.getImagePath());
                }
        );
        return playerList;
    }

//    public int updatePlayerWinStats(int score, int playerId){
//        return playerRepository.updatePlayerWinStats(score, playerId);
//    }

//    public int updatePlayerLossStats(int score, int playerId){
//        return playerRepository.updatePlayerLossStats(score, playerId);
//    }

    public Player findByPlayerId(int playerId) {
        return playerRepository.findByPlayerId(playerId);
    }

    public Player save(Player player) {
        return playerRepository.save(player);
    }

//    public String findPlayerNameByPlayerId(int playerId){
//        return playerRepository.findPlayerNameByPlayerId(playerId);
//    }
}
