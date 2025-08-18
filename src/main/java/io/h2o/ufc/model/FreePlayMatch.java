package io.h2o.ufc.model;

import io.h2o.ufc.dto.DailyFreePlayMatchCount;
import io.h2o.ufc.dto.PVPStatsByGameTypeDTO;
import io.h2o.ufc.dto.PlayerActivity;
import io.h2o.ufc.dto.PlayerStatsByGameTypeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "free_play_matches")

@NamedNativeQuery(
        name = "getDailyFreePlayMatchCounts",
//        query = " SELECT count(free_play_id) as matchCount, date_format(match_time,'%Y-%m-%d') as matchDate FROM free_play_matches  where match_time not like '%2024-06-01%' GROUP BY date_format(match_time,'%Y-%m-%d') ORDER BY match_time",
        query = " SELECT count(free_play_id) as matchCount, date_format(match_time,'%Y-%m-%d') as matchDate FROM free_play_matches GROUP BY date_format(match_time,'%Y-%m-%d') ORDER BY matchDate",
        resultSetMapping = "result"
)
@SqlResultSetMapping(
        name = "result",
        classes = @ConstructorResult(
                targetClass = DailyFreePlayMatchCount.class,
                columns = {
                        @ColumnResult(name = "matchCount", type = Integer.class),
                        @ColumnResult(name = "matchDate", type = String.class)
                }
        )
)

@NamedNativeQuery(
        name = "getPlayerWiseMatchPlayedPercent",
        query = "select ftbl.player_id as playerId, pl.player_name as playerName, round((final_match_count * 50/(select count(free_play_id) from free_play_matches where winner!=0)) , 2) as matchPlayedPercent " +
                "from ( select sum(tbl.match_count) as final_match_count, player_id from (select rand(), count(free_play_id) as match_count, player_one_id as player_id from free_play_matches group by player_one_id union select rand(), count(free_play_id) as match_count, player_two_id as player_id from free_play_matches group by player_two_id ) as tbl group by player_id order by player_id) as ftbl " +
                "join player as pl on pl.player_id = ftbl.player_id",
        resultSetMapping = "result1"
)
@SqlResultSetMapping(
        name = "result1",
        classes = @ConstructorResult(
                targetClass = PlayerActivity.class,
                columns = {
                        @ColumnResult(name = "playerId", type = Integer.class),
                        @ColumnResult(name = "playerName", type = String.class),
                        @ColumnResult(name = "matchPlayedPercent", type = Float.class)
                }
        )
)

@NamedNativeQuery(
        name = "getFreePlayStats",
        query = "SELECT player_stat.player_id as playerId, player_name as PlayerName, match_played as matchPlayed, score, opp_score as oppScore, coalesce(wins,0) as totalWins, (match_played - coalesce(wins,0)) as totalLost, round(score/match_played, 2) as avgScore, round(opp_score/match_played, 2) as oppAvgScore,  round(((score-opp_score)*10)/match_played, 2) as scoreMargin, coalesce(round((wins/match_played)*100, 2),0) as winPercent from \n" +
                "(select sum(match_played) as match_played, sum(score) as score, sum(opp_score) as opp_score, player_id as player_id from \n" +
                "(select rand(), count(free_play_id) as match_played, sum(player_one_score) as score, sum(player_two_score) as opp_score, player_one_id as player_id from free_play_matches GROUP BY player_one_id\n" +
                "UNION\n" +
                "select rand(), count(free_play_id) as match_played, sum(player_two_score) as score, sum(player_one_score) as opp_score, player_two_id as player_id from free_play_matches GROUP BY player_two_id) \n" +
                "as tbl\n" +
                "GROUP BY player_id\n" +
                "ORDER BY player_id) player_stat\n" +
                "left join (select count(winner) as wins, winner as player_id from free_play_matches GROUP BY winner) player_win_stat\n" +
                "on player_win_stat.player_id = player_stat.player_id\n" +
                "left join player\n" +
                "on player.player_id = player_stat.player_id",
        resultSetMapping = "result2"
)
@NamedNativeQuery(
        name = "getTournamentStats",
        query = "SELECT player_stat.player_id as playerId, player_name as PlayerName, match_played as matchPlayed, score, opp_score as oppScore, coalesce(wins,0) as totalWins, (match_played - coalesce(wins,0)) as totalLost, round(score/match_played, 2) as avgScore, round(opp_score/match_played, 2) as oppAvgScore,  round(((score-opp_score)*10)/match_played, 2) as scoreMargin, coalesce(round((wins/match_played)*100, 2),0) as winPercent from \n" +
                "(select sum(match_played) as match_played, sum(score) as score, sum(opp_score) as opp_score, player_id as player_id from \n" +
                "(select rand(), count(match_id) as match_played, sum(player_one_score) as score, sum(player_two_score) as opp_score, player_one_id as player_id from tournament_matches GROUP BY player_one_id\n" +
                "UNION\n" +
                "select rand(), count(match_id) as match_played, sum(player_two_score) as score, sum(player_one_score) as opp_score, player_two_id as player_id from tournament_matches GROUP BY player_two_id) \n" +
                "as tbl\n" +
                "GROUP BY player_id\n" +
                "ORDER BY player_id) player_stat\n" +
                "left join (select count(winner) as wins, winner as player_id from tournament_matches GROUP BY winner) player_win_stat\n" +
                "on player_win_stat.player_id = player_stat.player_id\n" +
                "left join player\n" +
                "on player.player_id = player_stat.player_id",
        resultSetMapping = "result2"
)
@SqlResultSetMapping(
        name = "result2",
        classes = @ConstructorResult(
                targetClass = PlayerStatsByGameTypeDTO.class,
                columns = {
                        @ColumnResult(name = "playerId", type = Integer.class),
                        @ColumnResult(name = "playerName", type = String.class),
//                        @ColumnResult(name = "gameType", type = Integer.class),
                        @ColumnResult(name = "matchPlayed", type = Integer.class),
                        @ColumnResult(name = "score", type = Integer.class),
                        @ColumnResult(name = "totalWins", type = Integer.class),
                        @ColumnResult(name = "totalLost", type = Integer.class),
                        @ColumnResult(name = "oppScore", type = Integer.class),
                        @ColumnResult(name = "avgScore", type = Float.class),
                        @ColumnResult(name = "oppAvgScore", type = Float.class),
                        @ColumnResult(name = "scoreMargin", type = Float.class),
                        @ColumnResult(name = "winPercent", type = Float.class)
                }
        )
)

@NamedNativeQuery(
        name = "getPlayerStatsByGameType",
        query = "select player_stat.game_type as gameType, match_played as matchPlayed, score, opp_score as oppScore, ifnull(wins,0) as totalWins, (match_played-ifnull(wins,0)) as totalLost, round((ifnull(wins,0)/match_played)*100, 2) as winPercent, \n" +
                "round(score/match_played, 2) as avgScore, round(opp_score/match_played, 2) as oppAvgScore, round(((score-opp_score)*10)/match_played, 2) as scoreMargin from \n" +
                "(SELECT game_type, sum(match_played) as match_played, sum(score) as score, sum(opp_score) as opp_score  from\n" +
                "(SELECT game_type, count(free_play_id) as match_played, sum(player_one_score) as score, sum(player_two_score) as opp_score FROM free_play_matches WHERE player_one_id = :playerId GROUP BY game_type\n" +
                " UNION \n" +
                " SELECT game_type, count(free_play_id) as match_played, sum(player_two_score) as score, sum(player_one_score) as opp_score FROM free_play_matches WHERE player_two_id = :playerId GROUP BY game_type) uniontbl\n" +
                "group by game_type) player_stat\n" +
                "left join (select game_type, count(winner) as wins, winner as player_id from free_play_matches where winner = :playerId GROUP BY game_type) player_win_stat\n" +
                "on player_win_stat.game_type = player_stat.game_type",
        resultSetMapping = "result3"
)
@SqlResultSetMapping(
        name = "result3",
        classes = @ConstructorResult(
                targetClass = PlayerStatsByGameTypeDTO.class,
                columns = {
//                        @ColumnResult(name = "playerId", type = Integer.class),
//                        @ColumnResult(name = "playerName", type = String.class),
                        @ColumnResult(name = "gameType", type = Integer.class),
                        @ColumnResult(name = "matchPlayed", type = Integer.class),
                        @ColumnResult(name = "score", type = Integer.class),
                        @ColumnResult(name = "totalWins", type = Integer.class),
                        @ColumnResult(name = "totalLost", type = Integer.class),
                        @ColumnResult(name = "oppScore", type = Integer.class),
                        @ColumnResult(name = "avgScore", type = Float.class),
                        @ColumnResult(name = "oppAvgScore", type = Float.class),
                        @ColumnResult(name = "scoreMargin", type = Float.class),
                        @ColumnResult(name = "winPercent", type = Float.class)
                }
        )
)

//@NamedStoredProcedureQuery(
//        name = "getPVPStatsByGameType1",
//        procedureName = "new_procedure",
//        parameters = {
//                @StoredProcedureParameter(mode = ParameterMode.IN, name ="id1", type = Integer.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN, name ="id2", type = Integer.class)
//        },
//        resultSetMappings = "result4")
//@NamedNativeQuery(
//        name = "getPVPStatsByGameType",
//        query = "select player_one_id as playerOneId, player_two_id as playerTwoId, player_stat.game_type as gameType, match_played as matchPlayed, player_one_score as playerOneScore, player_two_score as playerTwoScore,\n" +
//                "player_one_wins as playerOneWins, (player_two_wins) as playerTwoWins,\n" +
//                "round(((player_one_wins/match_played)*100),2) as playerOneWinPercent, round((((player_two_wins)/match_played)*100),2) as playerTwoWinPercent, \n" +
//                "round((player_one_score/match_played),2) as playerOneAvgScore, round((player_two_score/match_played),2) as playerTwoAvgScore\n" +
//                "from \n" +
//                "(select player_one_id, player_two_id, game_type, sum(match_played) as match_played, sum(player_one_score) as player_one_score, sum(player_two_score) as player_two_score from \n" +
//                "(SELECT :playerOneId as player_one_id, :playerTwoId as player_two_id, game_type, count(free_play_id) as match_played, sum(player_one_score) as player_one_score, sum(player_two_score) as player_two_score\n" +
//                "FROM free_play_matches WHERE player_one_id= :playerOneId and player_two_id= :playerTwoId GROUP BY game_type\n" +
//                "UNION\n" +
//                "SELECT :playerTwoId as player_one_id, :playerOneId as player_two_id, game_type, count(free_play_id) as match_played, sum(player_two_score) as player_one_score, sum(player_one_score) as player_two_score\n" +
//                "FROM free_play_matches WHERE player_one_id= :playerTwoId and player_two_id= :playerOneId GROUP BY game_type) uniontbl\n" +
//                "GROUP BY game_type) player_stat\n" +
//                "join\n" +
//                "(\n" +
//                "SELECT game_type, sum(player_one_wins) as player_one_wins, sum(player_two_wins) as player_two_wins from\n" +
//                "(SELECT game_type, count(winner) as player_one_wins, 0 as player_two_wins  from free_play_matches where ((player_one_id= :playerOneId and player_two_id= :playerTwoId) or (player_two_id= :playerOneId and player_one_id= :playerTwoId)) and winner= :playerOneId GROUP BY game_type\n" +
//                "UNION\n" +
//                "SELECT game_type, 0 as player_one_wins, count(winner) as player_two_wins from free_play_matches where ((player_one_id= :playerOneId and player_two_id= :playerTwoId) or (player_two_id= :playerOneId and player_one_id= :playerTwoId)) and winner= :playerTwoId GROUP BY game_type) uniontbl\n" +
//                "GROUP BY game_type) player_win_stat\n" +
//                "on\n" +
//                "player_stat.game_type= player_win_stat.game_type",
//        resultSetMapping = "result4"
//)

@NamedNativeQuery(
        name = "getPVPStatsByGameType",
        query = "select  :playerOneId as playerOneId,  :playerTwoId as playerTwoId, player_stat.game_type as gameType, match_played as matchPlayed, player_one_score as playerOneScore, player_two_score as playerTwoScore,\n" +
                "player_one_wins as playerOneWins, (player_two_wins) as playerTwoWins,\n" +
                "round(((player_one_wins/match_played)*100),2) as playerOneWinPercent, round((((player_two_wins)/match_played)*100),2) as playerTwoWinPercent, \n" +
                "round((player_one_score/match_played),2) as playerOneAvgScore, round((player_two_score/match_played),2) as playerTwoAvgScore\n" +
                "from \n" +
                "(\n" +
                "select game_type, sum(match_played) as match_played, sum(player_one_score) as player_one_score, sum(player_two_score) as player_two_score from \n" +
                "(SELECT game_type, count(free_play_id) as match_played, sum(player_one_score) as player_one_score, sum(player_two_score) as player_two_score\n" +
                "FROM free_play_matches WHERE player_one_id= :playerOneId and player_two_id= :playerTwoId GROUP BY game_type\n" +
                "UNION\n" +
                "SELECT game_type, count(free_play_id) as match_played, sum(player_two_score) as player_one_score, sum(player_one_score) as player_two_score\n" +
                "FROM free_play_matches WHERE player_one_id= :playerTwoId and player_two_id= :playerOneId GROUP BY game_type) uniontbl\n" +
                "GROUP BY game_type\n" +
                ") player_stat\n" +
                "join\n" +
                "(\n" +
                "SELECT game_type, sum(player_one_wins) as player_one_wins, sum(player_two_wins) as player_two_wins from\n" +
                "(\n" +
                "SELECT game_type, count(winner) as player_one_wins, 0 as player_two_wins  from free_play_matches where ((player_one_id= :playerOneId and player_two_id= :playerTwoId) or (player_two_id= :playerOneId and player_one_id= :playerTwoId)) and winner= :playerOneId GROUP BY game_type\n" +
                "UNION\n" +
                "SELECT game_type, 0 as player_one_wins, count(winner) as player_two_wins from free_play_matches where ((player_one_id= :playerOneId and player_two_id= :playerTwoId) or (player_two_id= :playerOneId and player_one_id= :playerTwoId)) and winner= :playerTwoId GROUP BY game_type\n" +
                ") uniontbl\n" +
                "GROUP BY game_type\n" +
                ") player_win_stat\n" +
                "on\n" +
                "player_stat.game_type= player_win_stat.game_type",
        resultSetMapping = "result4"
)
@SqlResultSetMapping(
        name = "result4",
        classes = @ConstructorResult(
                targetClass = PVPStatsByGameTypeDTO.class,
                columns = {
                        @ColumnResult(name = "gameType", type = Integer.class),
                        @ColumnResult(name = "matchPlayed", type = Integer.class),
                        @ColumnResult(name = "playerOneId", type = Integer.class),
                        @ColumnResult(name = "playerTwoId", type = Integer.class),
                        @ColumnResult(name = "playerOneScore", type = Integer.class),
                        @ColumnResult(name = "playerTwoScore", type = Integer.class),
                        @ColumnResult(name = "playerOneWins", type = Integer.class),
                        @ColumnResult(name = "playerTwoWins", type = Integer.class),
                        @ColumnResult(name = "playerOneWinPercent", type = Float.class),
                        @ColumnResult(name = "playerTwoWinPercent", type = Float.class),
                        @ColumnResult(name = "playerOneAvgScore", type = Float.class),
                        @ColumnResult(name = "playerTwoAvgScore", type = Float.class)
                }
        )
)
public class FreePlayMatch {

    @Id
    @SequenceGenerator(name = "free_play_match_sequence", sequenceName = "free_play_match_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "free_play_match_sequence")
    private int freePlayId;

//    @ManyToOne
//    @JoinColumn(name="tournament_id")
//    private Tournament tournament;

//    private int tournamentId;
//    private int roundNo;

    //    private int matchNo;
    private int playerOneId;
    private int playerTwoId;
    private int playerOneScore;
    private int playerTwoScore;
    private int winner;
    private int gameType;

    @Transient
    private String gameTypeName;

    private Date matchTime;

    @Transient
    private String playerOneName;
    @Transient
    private String playerTwoName;

    @Transient
    private String playerOneImagePath;
    @Transient
    private String playerTwoImagePath;

    @Transient
    private int TourId;

    @Transient
    private String winnerName;


}

