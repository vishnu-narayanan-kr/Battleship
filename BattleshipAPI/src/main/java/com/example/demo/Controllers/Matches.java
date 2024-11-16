package com.example.demo.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.Match;
import com.example.demo.Repositories.MatchesRepository;

@RestController
@RequestMapping("matches")
public class Matches {
    @Autowired
    private MatchesRepository matchRepository;
    
    @GetMapping("/getMatch")
    public ResponseEntity<Optional<Match>> getMatchDetails(@RequestParam int mId) {
        Optional<Match> match = matchRepository.findById(mId);
        
        if(match.isPresent()) {
        	return new ResponseEntity<>(match, HttpStatus.OK);
        }
        
        return new ResponseEntity<>(match, HttpStatus.NOT_FOUND);
    }
    

    @GetMapping("/getAllMatches")
    public List<Match> getMatches() {
        return matchRepository.findAll();
    }
}
