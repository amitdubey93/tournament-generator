package io.h2o.ufc.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "player")

public class Player {

    @Id
    private int playerId;
    private String playerName;
    private String playerNickname;
    private String imagePath;
    private int matchPlayed;
    private int score;
    private int totalWins;
    private float winPercent;

}
