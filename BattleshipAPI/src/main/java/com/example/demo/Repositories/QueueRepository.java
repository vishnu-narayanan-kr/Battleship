package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Queue;

public interface QueueRepository extends JpaRepository<Queue, Integer> {

}
