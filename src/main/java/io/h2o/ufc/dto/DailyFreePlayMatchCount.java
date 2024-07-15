package io.h2o.ufc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyFreePlayMatchCount {

    private int matchCount;
    private String matchDate;
}
