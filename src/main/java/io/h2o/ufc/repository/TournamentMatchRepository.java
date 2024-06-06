package io.h2o.ufc.repository;

import io.h2o.ufc.model.TournamentMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentMatchRepository extends JpaRepository<TournamentMatch, Integer> {

    //UPDATE matches SET player_one_score = 2, player_two_score = 5, winner = 1 WHERE match_id = 3;
    @Modifying
    @Query("UPDATE TournamentMatch SET playerOneScore = :playerOneScore, playerTwoScore = :playerTwoScore, winner = :winner WHERE matchId= :matchId")
    public int updateMatchScore(int playerOneScore, int playerTwoScore, int winner, int matchId);
}
