package io.h2o.ufc.service;

import io.h2o.ufc.model.Player;
import io.h2o.ufc.model.PointsTable;
import io.h2o.ufc.repository.PointsTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointsTableService {
    @Autowired
    private PointsTableRepository pointsTableRepository;

    public List<Player> getPlayerData(){
        return pointsTableRepository.getPlayerData();
    }
    public int updatePointsTablePlayerWinStats(int score, int playerId, int tournamentId){
        return pointsTableRepository.updatePointsTablePlayerWinStats(score, playerId, tournamentId);
    }

    public int updatePointsTablePlayerLossStats(int score, int playerId, int tournamentId){
        return pointsTableRepository.updatePointsTablePlayerLossStats(score, playerId, tournamentId);
    }

    public List<PointsTable> findAllByTournamentId(int tourId) {
        return pointsTableRepository.findAllByTournamentId(tourId);
    }
}
