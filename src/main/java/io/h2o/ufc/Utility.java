package io.h2o.ufc;

import java.util.Map;
import java.util.TreeMap;

public class Utility {
    public static Map<Integer, String> getGameType() {
        Map<Integer, String> gameMap = new TreeMap<>();
//        gameMap.put(1,"F10");
//        gameMap.put(2,"F5");
//        gameMap.put(3,"T9");
//        gameMap.put(4,"T5");
        gameMap.put(1, "F10 - First 10 Wins");
        gameMap.put(2, "F5 - First 5 Wins");
        gameMap.put(3, "T9 - Total 9 Games");
        gameMap.put(4, "T5 - Total 5 Games");
        return gameMap;
    }
}
