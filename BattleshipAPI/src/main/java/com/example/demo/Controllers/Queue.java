package com.example.demo.Controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Utility;
import com.example.demo.Models.Match;
import com.example.demo.Models.MatchQueue;
import com.example.demo.Repositories.MatchRepository;
import com.example.demo.Repositories.QueueRepository;

@RestController
@RequestMapping("queue")
public class Queue {
	@Autowired
	private QueueRepository queueRepository;
	@Autowired
	private MatchRepository matchRepository;
	
	@PostMapping(value = "/enterQueue", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MatchQueue> enterQueue(@RequestBody MatchQueue queue) {
		String message;
		HttpStatus statusCode = HttpStatus.OK;
		String username = queue.getUsername();
		String grid = queue.getGrid();
		boolean isMatched = false;
		
		// try matchmaking first
		try {
			Optional<Match> existingActiveMatch = matchRepository.findByIsActiveTrueAndP1OrP2(username, username);
			
			if(existingActiveMatch.isPresent()) {
				message = "Can't queue, you are already matched";
				statusCode = HttpStatus.SERVICE_UNAVAILABLE;
				isMatched = true;
				throw new Exception(message);
			}
			
			MatchQueue oldQueue = queueRepository.findByUsernameNot(username).getFirst();
			Match newMatch = new Match(
					0,
					username,
					oldQueue.getUsername(),
					Utility.getTimeStamp(),
					username,
					grid,
					oldQueue.getGrid(),
					true,
					null
					);
			matchRepository.save(newMatch);
			queueRepository.deleteById(oldQueue.getUsername());
			isMatched = true;
			message = "Match Found";
		} catch(Exception ex) {
			message = "Couldn't find matches. " + ex.getMessage();
		}
		
		if(!isMatched) {
			try {
				queue = queueRepository.findById(username).get();
				message = "You are already in queue, please wait";
			} catch(NoSuchElementException ex) {
				queue = new MatchQueue(username, Utility.getTimeStamp(), grid);
				queueRepository.save(queue);
				message = "You have successfully entered the queue";
			} catch(Exception ex) {
				queue = null;
				statusCode = HttpStatus.SERVICE_UNAVAILABLE;
				message = "Couldn't enter queue. " + ex.getMessage();
			}
		}
		
		return ResponseEntity
				.status(statusCode)
				.header("message", message)
				.body(queue);
	}
}
