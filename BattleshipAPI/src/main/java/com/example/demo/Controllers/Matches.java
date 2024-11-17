package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Models.Match;
import com.example.demo.Repositories.MatchRepository;

@RestController
@RequestMapping("matches")
public class Matches {
    @Autowired
    private MatchRepository matchRepository;
    
    @GetMapping("/getMatch")
    public ResponseEntity<Match> getMatchDetails(@RequestParam int mid) {
    	try {
            Match match = matchRepository.findById(mid).get();
            
        	return new ResponseEntity<>(match, HttpStatus.OK);
    	} catch(Exception ex) {
    		return ResponseEntity
    				.status(HttpStatus.NOT_FOUND)
    				.header("error", "Couldn't retrieve match data. More Details: " + ex.getMessage())
    				.body(null);
    	}
    }
    
    @GetMapping("/getActiveMatchByPlayer")
    public ResponseEntity<Match> getActiveMatchByPlayer(@RequestParam String username) {
    	try {
            Match match = matchRepository.findByIsActiveTrueAndP1OrP2(username, username).get();
            
        	return new ResponseEntity<>(match, HttpStatus.OK);
    	} catch(Exception ex) {
    		return ResponseEntity
    				.status(HttpStatus.NOT_FOUND)
    				.header("error", "Couldn't retrieve match data. More Details: " + ex.getMessage())
    				.body(null);
    	}
    }
    

    @GetMapping("/getAllMatches")
    public List<Match> getMatches() {
        return matchRepository.findAll();
    }
}
