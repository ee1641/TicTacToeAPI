package com.EleziEjup.TicTacToe.data.repository;

import com.EleziEjup.TicTacToe.data.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer>{

}
