package com.example.demo.Controllers;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Models.Player;
import com.example.demo.Repositories.PlayerRepository;

@RestController
@RequestMapping("players")
public class Players {
	@Autowired
	private PlayerRepository playerRepository;
	
	@GetMapping("/getPlayerDetails")
	public ResponseEntity<Player> getPlayerDetails(@RequestParam String username) {
		Player player;
		String message;
		
		String pattern = "yyyy-MM-dd HH:mm:ssZ";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String date = simpleDateFormat.format(new Date());
		
		try {
			player = playerRepository.findById(username).get();
			message = "Exisiting player found";
			player.setLastSeen(date);
			playerRepository.save(player);
		} catch (NoSuchElementException ex) {
			player = new Player(username, date, "english");
			message = "Added as a new player";
			playerRepository.save(player);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", ex.getMessage()).body(null);			
		}
		
		return ResponseEntity.status(HttpStatus.OK).header("succes", message).body(player);
	}
}
