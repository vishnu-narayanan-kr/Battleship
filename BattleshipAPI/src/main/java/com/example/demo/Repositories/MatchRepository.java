package com.example.demo.Repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Match;

public interface MatchRepository extends JpaRepository<Match, Integer> {
	Optional<Match> findByIsActiveTrueAndP1OrIsActiveTrueAndP2(String username, String username2);
}
