package io.h2o.ufc.repository;

import io.h2o.ufc.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {


    //update player set matches_played = matches_played + 1, score=score+10 , total_wins = total_wins +1 where player_id = 1;
    //update player set matches_played = matches_played + 1, score=score+10  where player_id = 2;

    //public List<Player> findAll();


//    @Modifying
//    @Query("UPDATE Player SET matchPlayed = matchPlayed + 1, score = score + :score, totalWins = totalWins + 1 WHERE playerId= :playerId")
//    public int updatePlayerWinStats(int score, int playerId);
//
//    @Modifying
//    @Query("UPDATE Player SET matchPlayed = matchPlayed + 1, score = score + :score WHERE playerId= :playerId")
//    public int updatePlayerLossStats(int score, int playerId);


    public Player findByPlayerId(int id);
//    public String findPlayerNameByPlayerId(int id);

//    @Query("SELECT * FROM free_play_matches where player_one_id = :playerId  or player_two_id = :playerId")
//    public Player getPlayerMatches(int playerId);

//    @Query("SELECT * FROM free_play_matches where ((player_one_id = :playerOneId and player_two_id = :playerTwoId) or (player_one_id = :playerTwoId and player_two_id = :playerOneId))")
//    public Player getPlayerVsPlayerData(int playerOneId, int playerTwoId);
}
