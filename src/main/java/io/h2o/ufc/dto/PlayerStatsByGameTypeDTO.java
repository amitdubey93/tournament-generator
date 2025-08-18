package io.h2o.ufc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerStatsByGameTypeDTO {

    private int gameType;
    private String gameTypeName;

    private int playerId;
    private String playerName;
    private String playerNickName;
    private String imagePath;

    private int matchPlayed;
    private int score;
    private int oppScore;
    private int totalWins;
    private int totalLost;
    private float winPercent;
    private float avgScore;
    private float oppAvgScore;
    private float scoreMargin;
}
