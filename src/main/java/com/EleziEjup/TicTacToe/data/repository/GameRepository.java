package com.EleziEjup.TicTacToe.data.repository;

import com.EleziEjup.TicTacToe.data.entity.Game;
import com.EleziEjup.TicTacToe.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer>{
    int countByPlayerXOrPlayerO(User playerX, User playerY);
    int countByWinner(User winner);
}
