package io.h2o.ufc.service;

import io.h2o.ufc.model.Match;
import io.h2o.ufc.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    public List<Match> findAll(){
        return matchRepository.findAll();
    }

}
