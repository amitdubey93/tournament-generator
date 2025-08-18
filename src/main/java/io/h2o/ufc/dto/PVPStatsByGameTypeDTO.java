package io.h2o.ufc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PVPStatsByGameTypeDTO {

    private int gameType;
    private String gameTypeName;
    private int matchPlayed;

    private int playerOneId;
    private int playerTwoId;

    private String playerOneName;
    private String playerTwoName;

    private String playerOneImagePath;
    private String playerTwoImagePath;

    private int playerOneScore;
    private int playerTwoScore;

    private int playerOneWins;
    private int playerTwoWins;

    private float playerOneWinPercent;
    private float playerTwoWinPercent;

    private float playerOneAvgScore;
    private float playerTwoAvgScore;

    private String winningPlayerName;
    private float scoreMargin;

    //private List<FreePlayMatch> pvpByGameTypeFreePlayMatchList;

}
