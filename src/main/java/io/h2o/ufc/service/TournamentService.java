package io.h2o.ufc.service;


import io.h2o.ufc.model.Tournament;
import io.h2o.ufc.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;

    public List<Tournament> findAll(){
        return tournamentRepository.findAll();
    }

    public Tournament findById(Integer id){
        return tournamentRepository.findById(id).get();
    }

    public Tournament save(Tournament tournament){
        return tournamentRepository.save(tournament);
    }


    public boolean updateFinalWinner(int winnerId, int tourId) {
        return tournamentRepository.updateFinalWinner(winnerId, tourId) == 1;
    }
}
