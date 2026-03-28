package com.EleziEjup.TicTacToe.data.entity;

import com.EleziEjup.TicTacToe.data.enums.GameStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "playerx_id", nullable = false)
    private User playerX;

    @ManyToOne
    @JoinColumn(name="playero_id")
    private User playerO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @Column(nullable = false)
    private String board = ".........";

    @ManyToOne
    @JoinColumn(name="current_id")
    private User currentTurn;

    @ManyToOne
    @JoinColumn(name="winner_id")
    private User winner;

    @Column(nullable = false)
    private LocalDateTime dateTime;

}
