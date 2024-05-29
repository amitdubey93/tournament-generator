package io.h2o.ufc.repository;

import io.h2o.ufc.model.PointsTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsTableRepository extends JpaRepository<PointsTable, Integer> {


    //update points_table set score=score+10, won=won+1 , matches_played = matches_played + 1 where player_id=1 and tournament_id=1;
    //update points_table set score=score+2, lost=lost+1 , matches_played = matches_played + 1 where player_id=2 and tournament_id=1;
    @Modifying
    @Query("UPDATE PointsTable SET score = score + :score, won = won + 1, matchPlayed = matchPlayed + 1 WHERE playerId= :playerId AND tournament.tournamentId = :tournamentId")
    public int updatePointsTablePlayerWinStats(int score, int playerId, int tournamentId);

    @Modifying
    @Query("UPDATE PointsTable SET score = score + :score, lost = lost + 1, matchPlayed = matchPlayed + 1 WHERE playerId= :playerId AND tournament.tournamentId = :tournamentId")
    public int updatePointsTablePlayerLossStats(int score, int playerId, int tournamentId);
}
