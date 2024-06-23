package io.h2o.ufc.repository;

import io.h2o.ufc.model.DailyFreePlayMatchCount;
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
            "ifnull((select sum(playerOneScore) from FreePlayMatch where playerOneId = :playerId),0) + ifnull((select sum(playerTwoScore) from FreePlayMatch where playerTwoId = :playerId),0)," +
            "ifnull((((select count(winner) from FreePlayMatch where winner = :playerId)/(select count(m.freePlayId))) * 100),0)" +
            ") " +
            "from FreePlayMatch m where (m.playerOneId = :playerId or m.playerTwoId = :playerId) and m.winner != 0")
    public Player getPlayerData(int playerId);

    //    @Query("select new io.h2o.ufc.model.DailyFreePlayMatchCount( count(freePlayId), date_format(matchTime,'%Y-%m-%d')) from FreePlatMatch group by date_format(matchTime,'%Y-%m-%d') order by matchTime")
    @Query(name = "getDailyFreePlayMatchCounts", nativeQuery = true)
    public List<DailyFreePlayMatchCount> getDailyFreePlayMatchCounts();
}
