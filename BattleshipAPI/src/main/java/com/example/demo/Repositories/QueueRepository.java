package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.MatchQueue;

public interface QueueRepository extends JpaRepository<MatchQueue, String> {

	List<MatchQueue> findByUsernameNot(String username);

}
