package io.h2o.ufc.model;

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

