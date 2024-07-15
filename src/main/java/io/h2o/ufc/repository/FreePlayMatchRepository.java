package io.h2o.ufc.repository;

import io.h2o.ufc.dto.DailyFreePlayMatchCount;
import io.h2o.ufc.model.FreePlayMatch;
import io.h2o.ufc.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FreePlayMatchRepository extends JpaRepository<FreePlayMatch, Integer> {

    @Modifying
    @Query("UPDATE FreePlayMatch SET playerOneScore = :playerOneScore, playerTwoScore = :playerTwoScore, winner = :winner, matchTime = :matchTime WHERE freePlayId= :freePlayId")
    public int updateFreePlayMatchScore(int playerOneScore, int playerTwoScore, int winner, int freePlayId, Date matchTime);

    // Full query
//    @Query("FROM FreePlayMatch ORDER BY freePlayId desc, winner")
//    List<FreePlayMatch> findAllOrdered();

    // Shorthand relying on method naming
//    List<FreePlayMatch> findAllOrderByFreePlayId();

    @Query("select new io.h2o.ufc.model.Player(" +
            "(select count(m.freePlayId)), " +
            "(select count(winner) from FreePlayMatch where winner = :playerId), " +
            "ifnull((select sum(playerOneScore) from FreePlayMatch where playerOneId = :playerId),0) + ifnull((select sum(playerTwoScore) from FreePlayMatch where playerTwoId = :playerId),0), " +
            "ifnull((select sum(playerTwoScore) from FreePlayMatch where playerOneId = :playerId),0) + ifnull((select sum(playerOneScore) from FreePlayMatch where playerTwoId = :playerId),0) " +
//            "ifnull((((select count(winner) from FreePlayMatch where winner = :playerId)/(select count(m.freePlayId))) * 100),0)" +
            ") " +
            "from FreePlayMatch m where (m.playerOneId = :playerId or m.playerTwoId = :playerId) and m.winner != 0")
    public Player getPlayerFreePlayData(int playerId);

    //    @Query("select new io.h2o.ufc.dto.DailyFreePlayMatchCount( count(freePlayId), date_format(matchTime,'%Y-%m-%d')) from FreePlatMatch group by date_format(matchTime,'%Y-%m-%d') order by matchTime")
    @Query(name = "getDailyFreePlayMatchCounts", nativeQuery = true)
    public List<DailyFreePlayMatchCount> getDailyFreePlayMatchCounts();

    @Query("SELECT DISTINCT(playerOneId) from FreePlayMatch where (playerOneId = :playerId or playerTwoId = :playerId) union SELECT DISTINCT(playerTwoId) from FreePlayMatch where (playerOneId = :playerId or playerTwoId = :playerId)")
    public List<Integer> getPlayerOneVsPlayerList(int playerId);

    @Query("select new io.h2o.ufc.model.Player(" +
            "(select count(m.freePlayId)), " +
            "(select count(winner) from FreePlayMatch m1 where ((m1.playerOneId = :playerOneId and m1.playerTwoId = :playerTwoId) or (m1.playerOneId = :playerTwoId and m1.playerTwoId = :playerOneId)) and m1.winner = :playerOneId), " +
            "ifnull((select sum(playerOneScore) from FreePlayMatch where playerOneId = :playerOneId and playerTwoId = :playerTwoId),0) + ifnull((select sum(playerTwoScore) from FreePlayMatch where playerOneId = :playerTwoId and playerTwoId = :playerOneId),0)" +
//            "ifnull((((select count(winner) from FreePlayMatch where ((playerOneId = :playerOneId and playerTwoId = :playerTwoId) or (playerOneId = :playerTwoId and playerTwoId = :playerOneId)) and winner = :playerOneId )/(select count(m.freePlayId))) * 100),0)" +
            //beware for ',' comma error
            ") " +
            "from FreePlayMatch m where ((m.playerOneId = :playerOneId and m.playerTwoId = :playerTwoId) or (m.playerOneId = :playerTwoId and m.playerTwoId = :playerOneId)) and m.winner != 0")
    public Player getPlayerVsPlayerFreePlayData(int playerOneId, int playerTwoId);


    @Query("from FreePlayMatch where ((playerOneId = :playerOneId and playerTwoId = :playerTwoId) or (playerOneId = :playerTwoId and playerTwoId = :playerOneId))")
    public List<FreePlayMatch> getPlayerVsPlayerFreePlayMatchList(int playerOneId, int playerTwoId);

}
