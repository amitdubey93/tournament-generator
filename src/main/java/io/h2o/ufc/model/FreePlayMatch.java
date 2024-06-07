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
@Table(name = "free_play_matches")
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

