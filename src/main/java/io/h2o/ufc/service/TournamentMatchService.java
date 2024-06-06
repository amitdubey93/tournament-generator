package io.h2o.ufc.service;

import io.h2o.ufc.model.TournamentMatch;
import io.h2o.ufc.repository.TournamentMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentMatchService {
    @Autowired
    private TournamentMatchRepository tournamentMatchRepository;

    public List<TournamentMatch> findAll() {
        return tournamentMatchRepository.findAll();
    }

    public int updateMatchScore(int playerOneScore, int playerTwoScore, int winner, int matchId) {
        return tournamentMatchRepository.updateMatchScore(playerOneScore, playerTwoScore, winner, matchId);
    }
}