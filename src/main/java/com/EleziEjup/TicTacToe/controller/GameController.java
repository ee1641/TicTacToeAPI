package com.EleziEjup.TicTacToe.controller;

import com.EleziEjup.TicTacToe.data.entity.Game;
import com.EleziEjup.TicTacToe.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    public Game createGame(Principal principal) {
        return gameService.createGame(principal.getName());
    }

    @GetMapping
    public Page<Game> getGames(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10")int size) {
        return gameService.getGames(page, size);
    }

    @PostMapping("/{id}/join")
    public Game joinGame(@PathVariable int id, Principal principal) {
        return gameService.joinGame(id, principal.getName());
    }
}