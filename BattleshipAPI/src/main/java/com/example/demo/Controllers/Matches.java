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
    	Match match;
    	String message;
    	HttpStatus statusCode = HttpStatus.OK;
    	
    	try {
            match = matchRepository.findById(mid).get();
            message = "Match details found";
    	} catch(Exception ex) {
    		match = null;
    		statusCode = HttpStatus.NOT_FOUND;
    		message = "Couldn't retrieve match data. More Details: " + ex.getMessage();
    	}
    	
    	return ResponseEntity
				.status(statusCode)
				.header("message", message)
				.body(match);
    }
    
    @GetMapping("/getActiveMatchByPlayer")
    public ResponseEntity<Match> getActiveMatchByPlayer(@RequestParam String username) {
    	Match match;
    	String message;
    	HttpStatus statusCode = HttpStatus.OK;
    	
    	try {
            match = matchRepository.findByIsActiveTrueAndP1OrIsActiveTrueAndP2(username, username).get();
            message = "Match details found";
    	} catch(Exception ex) {
    		match = null;
    		statusCode = HttpStatus.NOT_FOUND;
    		message = "Couldn't retrieve match data. More Details: " + ex.getMessage();
    	}
    	
    	return ResponseEntity
				.status(statusCode)
				.header("message", message)
				.body(match);
    }
    

    @GetMapping("/getAllMatches")
    public List<Match> getMatches() {
        return matchRepository.findAll();
    }
}
