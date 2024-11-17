package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.MatchQueue;

public interface QueueRepository extends JpaRepository<MatchQueue, String> {

}
