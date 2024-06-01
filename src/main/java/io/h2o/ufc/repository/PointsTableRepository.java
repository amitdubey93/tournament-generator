package io.h2o.ufc.repository;

import io.h2o.ufc.model.Player;
import io.h2o.ufc.model.PointsTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

//    select sum(score) as score, sum(won) as won, sum(match_played) as matches_played, round(( sum(won)/sum(match_played) * 100 ),2) as win_percent, player_id from points_table group by player_id;

    //@Query(value = "select sum(score) as score, sum(won) as won, sum(match_played) as matches_played, round(( sum(won)/sum(match_played) * 100 ),2) as win_percent, player_id from points_table group by player_id", nativeQuery = true)
//    @Query("select playerId, sum(score) as score, sum(won) as won, sum(matchPlayed) as matchPlayed, round(( sum(won)/sum(matchPlayed) * 100 ),2) as winPercent, sum(lost) as lost, tournament.tournamentId from PointsTable group by playerId")
    @Query("select new io.h2o.ufc.model.Player(pt.playerId, cast(sum(pt.matchPlayed) as integer), cast(sum(pt.score) as integer), cast(sum(pt.won) as integer), cast(round(( sum(pt.won)/sum(pt.matchPlayed) * 100 ),2) as integer )) from PointsTable as pt group by playerId")
    public List<Player> getPlayerData();
}
