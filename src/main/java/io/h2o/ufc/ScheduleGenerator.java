package io.h2o.ufc;

import io.h2o.ufc.model.Tournament;
import io.h2o.ufc.model.TournamentMatch;

import java.util.ArrayList;
import java.util.List;

public class ScheduleGenerator {

    public List<TournamentMatch> generateSchedule(Tournament tournament, List<Integer> players) {
        List<TournamentMatch> schedule = new ArrayList<>();
        int numPlayers = players.size();
        int matchNo = 1;
        int totalRounds = numPlayers - 1;

        for (int round = 1; round <= totalRounds; round++) {
            for (int i = 0; i < numPlayers; i++) {
                int playerOne = players.get(i);
                int playerTwo = players.get((i + round) % numPlayers);

                schedule.add(TournamentMatch.builder()
                        .tournament(tournament)
                        .matchNo(matchNo++)
                        .roundNo(round)
                        .playerOneId(playerOne)
                        .playerTwoId(playerTwo)
                        .build());

                // Add reverse fixture for home and away matches
//                schedule.add(Match.builder()
//                        .tournament(tournament)
//                        .matchNo(matchNo++)
//                        .roundNo(round + totalRounds)
//                        .playerOne(playerTwo)
//                        .playerTwo(playerOne)
//                        .build());
            }
        }

        if (numPlayers <= 3) {
            //only create finals
            schedule.add(TournamentMatch.builder()
                    .tournament(tournament)
                    .playerOneId(2001)
                    .playerTwoId(2002)
                    .matchNo(matchNo++)
                    .roundNo(2001)
                    .build());
        } else {
            //create two semifinals and a final
            schedule.add(TournamentMatch.builder()
                    .tournament(tournament)
                    .playerOneId(2001)
                    .playerTwoId(2002)
                    .matchNo(matchNo++)
                    .roundNo(1001)
                    .build());
            schedule.add(TournamentMatch.builder()
                    .tournament(tournament)
                    .playerOneId(2001)
                    .playerTwoId(2002)
                    .matchNo(matchNo++)
                    .roundNo(1002)
                    .build());
//            schedule.add(TournamentMatch.builder()
//                    .tournament(tournament)
//                    .playerOneId(2001)
//                    .playerTwoId(2002)
//                    .matchNo(matchNo++)
//                    .roundNo(1003)
//                    .build());
            schedule.add(TournamentMatch.builder()
                    .tournament(tournament)
                    .playerOneId(2001)
                    .playerTwoId(2002)
                    .matchNo(matchNo++)
                    .roundNo(2001)
                    .build());

        }


        return schedule;
    }
//    public List<Match> generateSchedule(Tournament tournament, List<String> players) {
//        List<Match> schedule = new ArrayList<>();
//        int numPlayers = players.size();
//        int matchNo = 1;
//        int totalRounds = (numPlayers - 1) * 2; // Each player plays twice against each opponent
//
//        for (int round = 1; round <= totalRounds; round++) {
//            for (int i = 0; i < numPlayers / 2; i++) {
//                int playerOneIndex = (round + i) % (numPlayers - 1);
//                int playerTwoIndex = (numPlayers - 1 - i + round) % (numPlayers - 1);
//
//                if (i == 0) {
//                    playerTwoIndex = numPlayers - 1;
//                }
//
//                String playerOne = players.get(playerOneIndex);
//                String playerTwo = players.get(playerTwoIndex);
//
//                // Home match in the first half of the tournament
//                if (round <= numPlayers - 1) {
//                    schedule.add(Match.builder()
//                            .tournament(tournament)
//                            .matchNo(matchNo++)
//                            .roundNo(round)
//                            .playerOne(playerOne)
//                            .playerTwo(playerTwo)
//                            .playerOneScore(0)
//                            .playerTwoScore(0)
//                            .winner("Game Pending")
//                            .build());
//                }
//                // Away match in the second half of the tournament
//                else {
//                    schedule.add(Match.builder()
//                            .tournament(tournament)
//                            .matchNo(matchNo++)
//                            .roundNo(round)
//                            .playerOne(playerTwo)
//                            .playerTwo(playerOne)
//                            .playerOneScore(0)
//                            .playerTwoScore(0)
//                            .winner("Game Pending")
//                            .build());
//                }
//            }
//        }
//
//        return schedule;
//    }

//    public static void main(String[] args) {
//        ScheduleGenerator generator = new ScheduleGenerator();
//        List<String> players = List.of("Alice", "Bob", "Charlie", "David", "Eve", "Frank");
//        List<Match> schedule = generator.generateSchedule(new Tournament(), players);
//        schedule.stream().forEach(System.out::println);
//        for (Match match : schedule) {
//            System.out.println("Match No: " + match.getMatchNo() +
//                    ", Round No: " + match.getRoundNo() +
//                    ", " + match.getPlayerOne() + " vs " + match.getPlayerTwo());
//        }
//    }
}

//List<String> players = List.of("Alice", "Bob", "Charlie", "David", "Eve", "Frank");