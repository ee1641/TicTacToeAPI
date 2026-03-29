package com.EleziEjup.TicTacToe.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private int gamesPlayed;
    private int winRate;
}
