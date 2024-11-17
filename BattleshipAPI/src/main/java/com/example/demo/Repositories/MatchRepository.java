package com.example.demo.Repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Match;

public interface MatchRepository extends JpaRepository<Match, Integer> {
	Optional<Match> findByIsActiveTrueAndP1OrP2(String p1, String p2);
}
