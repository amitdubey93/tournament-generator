package io.h2o.ufc.repository;

import io.h2o.ufc.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {


    //update player set matches_played = matches_played + 1, score=score+10 , total_wins = total_wins +1 where player_id = 1;
    //update player set matches_played = matches_played + 1, score=score+10  where player_id = 2;

    //public List<Player> findAll();


    @Modifying
    @Query("UPDATE Player SET matchPlayed = matchPlayed + 1, score = score + :score, totalWins = totalWins + 1 WHERE playerId= :playerId")
    public int updatePlayerWinStats(int score, int playerId);

    @Modifying
    @Query("UPDATE Player SET matchPlayed = matchPlayed + 1, score = score + :score WHERE playerId= :playerId")
    public int updatePlayerLossStats(int score, int playerId);
}
