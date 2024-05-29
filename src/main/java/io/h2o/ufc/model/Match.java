package io.h2o.ufc.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "matches")
public class Match {

    @Id
    @SequenceGenerator(name = "match_sequence", sequenceName = "match_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_sequence")
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

