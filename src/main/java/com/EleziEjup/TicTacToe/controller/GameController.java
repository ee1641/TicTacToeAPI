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
    public Page<Game> getGames(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10")int size,
                               @RequestParam(required = false) String username,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false) String before,
                               @RequestParam(required = false) String after) {
        return gameService.getGames(page, size, username, status, before, after);
    }

    @PostMapping("/{id}/join")
    public Game joinGame(@PathVariable int id, Principal principal) {
        return gameService.joinGame(id, principal.getName());
    }

    @GetMapping("/{id}/board")
    public String getBoardState(@PathVariable int id){
        return gameService.getBoardState(id);
    }

    //It made sense to have an endpoint that includes full game details and not just board state but since assignment wasnt explicit whether displaying additional data is okay or not,so i added both endpoints.
    @GetMapping("/{id}")
    public Game getGame(@PathVariable int id){
        return gameService.getGame(id);
    }

    @PostMapping("/{id}/move")
    public Game move(@PathVariable int id, @RequestParam int position, Principal principal) {
        return gameService.move(id, principal.getName(), position);
    }
}