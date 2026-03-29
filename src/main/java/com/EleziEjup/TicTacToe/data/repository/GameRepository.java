package com.EleziEjup.TicTacToe.data.repository;

import com.EleziEjup.TicTacToe.data.entity.Game;
import com.EleziEjup.TicTacToe.data.entity.User;
import com.EleziEjup.TicTacToe.data.enums.GameStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface GameRepository extends JpaRepository<Game, Integer>{
    int countByPlayerXOrPlayerO(User playerX, User playerY);
    int countByWinner(User winner);

    @Query("SELECT g FROM Game g LEFT JOIN g.playerO po WHERE (:username IS NULL OR g.playerX.username = :username OR po.username = :username) AND (:status IS NULL OR g.status = :status) AND (:before IS NULL OR g.dateTime < :before) AND (:after IS NULL OR g.dateTime > :after)")
    Page<Game> filterGames(@Param("username") String username, @Param("status")GameStatus status, @Param("before") LocalDateTime before, @Param("after")LocalDateTime after, Pageable pageable);
}
