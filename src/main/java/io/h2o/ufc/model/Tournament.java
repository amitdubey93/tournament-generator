package io.h2o.ufc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Name must have a value")
    @Size(max = 30, message = "Name max length should be under 30 char")
    private String tournamentName;

    private int winner;
    private boolean allMatchesCompleted;
    private Date tournamentDate;
    private int playerCount;
    private int gameType;


    @OneToMany(mappedBy="tournament",cascade=CascadeType.ALL)
    @ToString.Exclude
    private Collection<TournamentMatch> tournamentMatchList = new ArrayList<>();

    @OneToMany(mappedBy="tournament",cascade=CascadeType.ALL)
    @ToString.Exclude
    private Collection<PointsTable> pointsTable = new ArrayList<>();

    @Transient
    private String winnerName;

    @Transient
    @Size(min = 3, message = "Minimum 3 Players Needed to create a Tournament")
    private List<Player> playerList;

}

