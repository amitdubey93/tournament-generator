package io.h2o.ufc.repository;

import io.h2o.ufc.model.TournamentMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TournamentMatchRepository extends JpaRepository<TournamentMatch, Integer> {

    //UPDATE matches SET player_one_score = 2, player_two_score = 5, winner = 1 WHERE match_id = 3;
    @Modifying
    @Query("UPDATE TournamentMatch SET playerOneScore = :playerOneScore, playerTwoScore = :playerTwoScore, winner = :winner, matchTime = :matchTime WHERE matchId= :matchId")
    public int updateTournamentMatchScore(int playerOneScore, int playerTwoScore, int winner, int matchId, Date matchTime);


    //    @Query("SELECT COUNT(winner) from TournamentMatch where winner = 0 and Tournament.tournamentId = :tourId ")
    @Query(value = "SELECT count(winner) FROM tournament_matches WHERE winner = 0 and tournament_id = :tourId and round_no not in (1001,1002,1003,2001)", nativeQuery = true)
    public int checkIfAllLeagueMatchesCompleted(int tourId);

    @Modifying
    @Query(value = "UPDATE tournament_matches SET player_one_id = :playerOneId, player_two_id = :playerTwoId where round_no = :roundNo and tournament_id = :tourId", nativeQuery = true)
    public int updateEliminatorPlayers(int playerOneId, int playerTwoId, int tourId, int roundNo);

    @Modifying
    @Query(value = "UPDATE tournament_matches SET player_one_id = :playerOneId where round_no = 2001 and tournament_id = :tourId", nativeQuery = true)
    public int updateFinalPlayerOne(int playerOneId, int tourId);

    @Modifying
    @Query(value = "UPDATE tournament_matches SET player_two_id = :playerTwoId where round_no = 2001 and tournament_id = :tourId", nativeQuery = true)
    public int updateFinalPlayerTwo(int playerTwoId, int tourId);


}
