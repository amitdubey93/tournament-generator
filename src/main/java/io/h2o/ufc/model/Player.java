package io.h2o.ufc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    @Transient
    private float avgScore;

    public Player(int playerId, String playerName, int matchPlayed, int score, int totalWins, float winPercent, float avgScore) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.matchPlayed = matchPlayed;
        this.score = score;
        this.totalWins = totalWins;
        this.winPercent = winPercent;
        this.avgScore = avgScore;
    }

    public Player(long matchPlayed, long totalWins, long score, long winPercent) {
        this.matchPlayed = (int) matchPlayed;
        this.totalWins = (int) totalWins;
        this.score = (int) score;
        this.winPercent = (float) winPercent;
    }
}
