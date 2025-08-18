package io.h2o.ufc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NamedNativeQuery(
        name = "getPointsTable",
        query = "SELECT player_stat.player_id as playerId, player_name as PlayerName, match_played as matchPlayed, score, opp_score as oppScore, coalesce(wins,0) as totalWins,  (match_played - coalesce(wins,0)) as totalLost, round(score/match_played, 2) as avgScore, round(opp_score/match_played, 2) as oppAvgScore,  round(((score-opp_score)*10)/match_played, 2) as scoreMargin, coalesce(round((wins/match_played)*100, 2),0) as winPercent from \n" +
                "(select sum(match_played) as match_played, sum(score) as score, sum(opp_score) as opp_score, player_id as player_id from \n" +
                "(select rand(), count(match_id) as match_played, sum(player_one_score) as score, sum(player_two_score) as opp_score, player_one_id as player_id from tournament_matches where tournament_id = :tourId and winner != 0 GROUP BY player_one_id\n" +
                "UNION\n" +
                "select rand(), count(match_id) as match_played, sum(player_two_score) as score, sum(player_one_score) as opp_score, player_two_id as player_id from tournament_matches where tournament_id = :tourId and winner != 0 GROUP BY player_two_id) \n" +
                "as tbl\n" +
                "GROUP BY player_id\n" +
                "ORDER BY player_id) player_stat\n" +
                "left join (select count(winner) as wins, winner as player_id from tournament_matches where tournament_id = :tourId GROUP BY winner) player_win_stat\n" +
                "on player_win_stat.player_id = player_stat.player_id\n" +
                "left join player\n" +
                "on player.player_id = player_stat.player_id",
        resultSetMapping = "result2"
)

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tournament_matches")
public class TournamentMatch {

    @Id
    @SequenceGenerator(name = "tournament_match_sequence", sequenceName = "tournament_match_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournament_match_sequence")
    private int matchId;

    @ManyToOne
    @JoinColumn(name="tournament_id")
    private Tournament tournament;

    //private int tournamentId;
    private int roundNo;
    private int matchNo;
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
    private int tourId;

    @Transient
    private String winnerName;


}

