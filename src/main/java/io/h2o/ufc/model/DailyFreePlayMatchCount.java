package io.h2o.ufc.model;

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
