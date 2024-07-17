package io.h2o.ufc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerActivity {

    private int playerId;
    private String playerName;
    private float matchPlayedPercent;
}
