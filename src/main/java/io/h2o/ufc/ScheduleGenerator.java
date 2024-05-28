package io.h2o.ufc;

import io.h2o.ufc.model.Match;
import io.h2o.ufc.model.Tournament;

import java.util.ArrayList;
import java.util.List;

public class ScheduleGenerator {

    public List<Match> generateSchedule(Tournament tournament, List<String> players) {
        List<Match> schedule = new ArrayList<>();
        int numPlayers = players.size();
        int matchNo = 1;
        int totalRounds = (numPlayers - 1) * 2; // Each player plays twice against each opponent

        for (int round = 1; round <= totalRounds; round++) {
            for (int i = 0; i < numPlayers / 2; i++) {
                int playerOneIndex = (round + i) % (numPlayers - 1);
                int playerTwoIndex = (numPlayers - 1 - i + round) % (numPlayers - 1);

                if (i == 0) {
                    playerTwoIndex = numPlayers - 1;
                }

                String playerOne = players.get(playerOneIndex);
                String playerTwo = players.get(playerTwoIndex);

                // Home match in the first half of the tournament
                if (round <= numPlayers - 1) {
                    schedule.add(Match.builder()
                            .tournament(tournament)
                            .matchNo(matchNo++)
                            .roundNo(round)
                            .playerOne(playerOne)
                            .playerTwo(playerTwo)
                            .playerOneScore(0)
                            .playerTwoScore(0)
                            .winner("Game Pending")
                            .build());
                }
                // Away match in the second half of the tournament
                else {
                    schedule.add(Match.builder()
                            .tournament(tournament)
                            .matchNo(matchNo++)
                            .roundNo(round)
                            .playerOne(playerTwo)
                            .playerTwo(playerOne)
                            .playerOneScore(0)
                            .playerTwoScore(0)
                            .winner("Game Pending")
                            .build());
                }
            }
        }

        return schedule;
    }

    public static void main(String[] args) {
        ScheduleGenerator generator = new ScheduleGenerator();
        List<String> players = List.of("Alice", "Bob", "Charlie", "David", "Eve", "Frank");
        List<Match> schedule = generator.generateSchedule(new Tournament(), players);
        schedule.stream().forEach(System.out::println);
        for (Match match : schedule) {
            System.out.println("Match No: " + match.getMatchNo() +
                    ", Round No: " + match.getRoundNo() +
                    ", " + match.getPlayerOne() + " vs " + match.getPlayerTwo());
        }
    }
}

//List<String> players = List.of("Alice", "Bob", "Charlie", "David", "Eve", "Frank");