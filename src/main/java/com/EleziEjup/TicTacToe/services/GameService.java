package com.EleziEjup.TicTacToe.services;

import com.EleziEjup.TicTacToe.data.entity.Game;
import com.EleziEjup.TicTacToe.data.entity.User;
import com.EleziEjup.TicTacToe.data.enums.GameStatus;
import com.EleziEjup.TicTacToe.data.repository.GameRepository;
import com.EleziEjup.TicTacToe.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    public Game createGame(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();

        Game game = new Game();
        game.setPlayerX(user);
        game.setStatus(GameStatus.OPEN);
        game.setDateTime(LocalDateTime.now());
        game.setCurrentTurn(user);

        return gameRepository.save(game);
    }

    public Page<Game> getGames(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateTime").descending());
        return gameRepository.findAll(pageable);
    }

    public Game joinGame(int gameId, String username){
        Game game = gameRepository.findById(gameId).orElseThrow();
        if(!game.getStatus().equals(GameStatus.OPEN)){
            throw new RuntimeException("Game is not open");
        }

        User user = userRepository.findByUsername(username).orElseThrow();
        if(game.getPlayerX().getUsername().equals(username)){
            throw new RuntimeException("Cannot join ur own game");
        }

        game.setPlayerO(user);
        game.setStatus(GameStatus.IN_PROGRESS);

        return gameRepository.save(game);
    }
}