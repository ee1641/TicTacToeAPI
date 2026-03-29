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

    public Page<Game> getGames(int page, int size,String username,String status, String before, String after) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateTime").descending());
        GameStatus gameStatus = null;
        if(status != null) {
            gameStatus = GameStatus.valueOf(status);
        }
        LocalDateTime beforeDate = null;
        if (before != null) {
            beforeDate = LocalDateTime.parse(before);
        }

        LocalDateTime afterDate = null;
        if (after != null) {
            afterDate = LocalDateTime.parse(after);
        }
        return gameRepository.filterGames(username,gameStatus,beforeDate,afterDate,pageable);
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

    public String getBoardState(int gameId){
        Game game = gameRepository.findById(gameId).orElseThrow();
        return game.getBoard();
    }

    public Game getGame(int gameId){
        return gameRepository.findById(gameId).orElseThrow();
    }

    private boolean hasWinner(String board, char symbol){
        int[][] combinations = {{0,1,2},{3,4,5},{6,7,8},{0,4,8},{2,4,6},{0,3,6},{1,4,7},{2,5,8}};

        for(int[] combination : combinations){
            if(board.charAt(combination[0]) == symbol && board.charAt(combination[1]) == symbol && board.charAt(combination[2]) == symbol){
                return true;
            }
        }
        return false;
    }

    public Game move(int gameId, String username, int position){
        Game game = gameRepository.findById(gameId).orElseThrow();
        char symbol;

        if(!game.getStatus().equals(GameStatus.IN_PROGRESS)){
            throw new RuntimeException("Game is not in progress");
        }

        if(!game.getCurrentTurn().getUsername().equals(username)){
            throw new RuntimeException("It is not your turn");
        }

        if (position < 0 || position > 8) {
            throw new RuntimeException("Choose a valid position");
        }

        if (game.getBoard().charAt(position) != '.') {
            throw new RuntimeException("Position taken");
        }

        if(game.getPlayerX().getUsername().equals(username)){
            symbol = 'X';
        }else{
            symbol = 'O';
        }

        StringBuilder board = new  StringBuilder(game.getBoard());
        board.setCharAt(position,symbol);
        game.setBoard(board.toString());

        if(hasWinner(game.getBoard(),symbol)){
            User winner = userRepository.findByUsername(username).orElseThrow();
            game.setWinner(winner);
            game.setStatus(GameStatus.FINISHED);
        } else if (!game.getBoard().contains(".")) {
            game.setStatus(GameStatus.FINISHED);
        }else{
            if(game.getPlayerX().getUsername().equals(username)){
                game.setCurrentTurn(game.getPlayerO());
            }else{
                game.setCurrentTurn(game.getPlayerX());
            }
        }

        return gameRepository.save(game);
    }
}