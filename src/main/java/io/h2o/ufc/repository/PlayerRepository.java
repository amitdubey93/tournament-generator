package io.h2o.ufc.repository;

import io.h2o.ufc.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

}
