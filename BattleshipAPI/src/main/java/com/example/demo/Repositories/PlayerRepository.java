package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Player;

public interface PlayerRepository extends JpaRepository<Player, String> {

}
