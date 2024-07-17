package io.h2o.ufc.model;

import io.h2o.ufc.dto.DailyFreePlayMatchCount;
import io.h2o.ufc.dto.PlayerActivity;
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
        query = " SELECT count(free_play_id) as matchCount, date_format(match_time,'%Y-%m-%d') as matchDate FROM free_play_matches  where match_time not like '%2024-06-01%' GROUP BY date_format(match_time,'%Y-%m-%d') ORDER BY match_time",
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

