package io.h2o.ufc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "player")
public class Player {

    @Id
    @SequenceGenerator(name = "player_sequence", sequenceName = "player_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_sequence")
    private int playerId;
    private String playerName;
    private String playerNickName;
    private String imagePath;

    @Transient
    private MultipartFile playerImage;

    @Transient
    private int matchPlayed;
    @Transient
    private int score;
    @Transient
    private int oppScore;
    @Transient
    private int totalWins;
    @Transient
    private float winPercent;
    @Transient
    private float avgScore;
    @Transient
    private float oppAvgScore;
    @Transient
    private float scoreMargin;

    public Player(int playerId, String playerName, int matchPlayed, int score, int totalWins) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.matchPlayed = matchPlayed;
        this.score = score;
        this.totalWins = totalWins;
//        this.winPercent = winPercent;
//        this.avgScore = avgScore;
    }

    public Player(long matchPlayed, long totalWins, long score) {
        this.matchPlayed = (int) matchPlayed;
        this.totalWins = (int) totalWins;
        this.score = (int) score;
//        this.winPercent = (float) winPercent;
    }

    public Player(long matchPlayed, long totalWins, long score, long oppScore) {
        this.matchPlayed = (int) matchPlayed;
        this.totalWins = (int) totalWins;
        this.score = (int) score;
        this.oppScore = (int) oppScore;
//        this.winPercent = (float) winPercent;
    }
}
