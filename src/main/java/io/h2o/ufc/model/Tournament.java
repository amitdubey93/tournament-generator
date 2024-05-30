package io.h2o.ufc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;


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
    @NotNull
    private String duration;
    @NotNull
    @Min(4)
    private int playerCount;

    @OneToMany(mappedBy="tournament",cascade=CascadeType.ALL)
    @ToString.Exclude
    private Collection<Match> matchList = new ArrayList<>();

    @OneToMany(mappedBy="tournament",cascade=CascadeType.ALL)
    @ToString.Exclude
    private Collection<PointsTable> pointsTable = new ArrayList<>();


}

