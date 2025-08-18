package io.h2o.ufc.dto;

import io.h2o.ufc.model.FreePlayMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PVPStats {

//    private Player playerOne;
//    private Player playerTwo;
//    private String winningPlayerName;
//    private float scoreMargin;


    private int playerId; //playerTwoId basically for iterating
    private String playerName;

    private List<PVPStatsByGameTypeDTO> pvpStatsByGameTypeList;

    private List<FreePlayMatch> freePlayMatchList;

}
