package io.h2o.ufc;

import java.util.Map;
import java.util.TreeMap;

public class Utility {

//    @Autowired
//    private Properties properties;

//    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
//    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/ufc-4.0.0.jar/BOOT-INF/classes/static/images";

    public static String UPLOAD_DIRECTORY = "/uploads"; //to serve from uploads folder which is added in configuration
//    public static String UPLOAD_DIRECTORY =""; //to serve from static folder

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
