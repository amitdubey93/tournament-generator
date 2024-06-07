package io.h2o.ufc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Size(min=2, max=30)
    private String tournamentName;

//    @Transient
//    private Object playerIds;

    @Transient
    private List<Player> playerList;

    @NotNull
    private String duration;

    private Date tournamentDate;

    //    @NotNull
//    @Min(4)
    @Transient
    private int playerCount;


    @Transient
    private boolean allMatchCompleted;

    @OneToMany(mappedBy="tournament",cascade=CascadeType.ALL)
    @ToString.Exclude
    private Collection<TournamentMatch> tournamentMatchList = new ArrayList<>();

    @OneToMany(mappedBy="tournament",cascade=CascadeType.ALL)
    @ToString.Exclude
    private Collection<PointsTable> pointsTable = new ArrayList<>();


}

