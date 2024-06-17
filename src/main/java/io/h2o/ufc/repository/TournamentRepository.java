package io.h2o.ufc.repository;

import io.h2o.ufc.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {

    @Modifying
    @Query("UPDATE Tournament SET winner = :winnerId, allMatchesCompleted = true where tournamentId = :tourId")
    public int updateFinalWinner(int winnerId, int tourId);
}
