package io.h2o.ufc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "points_table")
public class PointsTable {

    @Id
    @SequenceGenerator(name = "pt_sequence", sequenceName = "pt_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pt_sequence")
    private int ptId;

    @ManyToOne
    @JoinColumn(name="tournament_id")
    private Tournament tournament;

    //private int tournamentId;
    private int playerId;

    @Transient
    private String playerName;

    private int matchPlayed;
    private int won;
    private int lost;
    private int score;



}

