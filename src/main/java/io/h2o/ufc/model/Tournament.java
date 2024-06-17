package io.h2o.ufc.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @SequenceGenerator(name = "tournament_sequence", sequenceName = "tournament_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournament_sequence")
    private int tournamentId;

    //    @NotNull
//    @Size(min=2, max=30)
    private String tournamentName;
    private int winner;
    private boolean allMatchesCompleted;
    private Date tournamentDate;
    private int playerCount;


    @OneToMany(mappedBy="tournament",cascade=CascadeType.ALL)
    @ToString.Exclude
    private Collection<TournamentMatch> tournamentMatchList = new ArrayList<>();

    @OneToMany(mappedBy="tournament",cascade=CascadeType.ALL)
    @ToString.Exclude
    private Collection<PointsTable> pointsTable = new ArrayList<>();

    @Transient
    private String winnerName;

    @Transient
    private List<Player> playerList;

}

