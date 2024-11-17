package com.example.demo.Controllers;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Utility;
import com.example.demo.Models.MatchQueue;
import com.example.demo.Repositories.QueueRepository;

@RestController
@RequestMapping("queue")
public class Queue {
	@Autowired
	private QueueRepository queueRepository;
	
	@PostMapping(value = "/enterQueue", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MatchQueue> enterQueue(@RequestBody MatchQueue queue) {
		String message;
		HttpStatus statusCode = HttpStatus.OK;
		String username = queue.getUsername();
		String grid = queue.getGrid();
		
		// try matchmaking first
		
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
		
		return ResponseEntity
				.status(statusCode)
				.header("message", message)
				.body(queue);
	}
}
