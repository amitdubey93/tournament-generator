package io.h2o.ufc.service;

import io.h2o.ufc.model.DailyFreePlayMatchCount;
import io.h2o.ufc.model.FreePlayMatch;
import io.h2o.ufc.model.Player;
import io.h2o.ufc.repository.FreePlayMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FreePlayMatchService {
    @Autowired
    private FreePlayMatchRepository freePlayMatchRepository;

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

    public Player getPlayerData(int playerId) {
        return freePlayMatchRepository.getPlayerData(playerId);
    }


    public List<DailyFreePlayMatchCount> getDailyFreePlayMatchCounts() {
        return freePlayMatchRepository.getDailyFreePlayMatchCounts();
    }
}
