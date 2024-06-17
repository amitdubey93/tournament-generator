package io.h2o.ufc.service;

import io.h2o.ufc.model.TournamentMatch;
import io.h2o.ufc.repository.TournamentMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TournamentMatchService {
    @Autowired
    private TournamentMatchRepository tournamentMatchRepository;

    public List<TournamentMatch> findAll() {
        return tournamentMatchRepository.findAll();
    }

    public int updateTournamentMatchScore(int playerOneScore, int playerTwoScore, int winner, int matchId) {
        return tournamentMatchRepository.updateTournamentMatchScore(playerOneScore, playerTwoScore, winner, matchId, new Date());
    }

    public boolean checkIfAllLeagueMatchesCompleted(int tourId) {
        return tournamentMatchRepository.checkIfAllLeagueMatchesCompleted(tourId) == 0;
    }

    public boolean updateEliminatorPlayers(int playerOneId, int playerTwoId, int tourId, int roundNo) {
        return tournamentMatchRepository.updateEliminatorPlayers(playerOneId, playerTwoId, tourId, roundNo) == 1;
    }

    public boolean updateFinalPlayerOne(int playerOneId, int tourId) {
        return tournamentMatchRepository.updateFinalPlayerOne(playerOneId, tourId) == 1;
    }

    public boolean updateFinalPlayerTwo(int playerTwoId, int tourId) {
        return tournamentMatchRepository.updateFinalPlayerTwo(playerTwoId, tourId) == 1;
    }
}
