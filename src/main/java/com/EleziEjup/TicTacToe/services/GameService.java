package com.EleziEjup.TicTacToe.services;

import com.EleziEjup.TicTacToe.data.entity.Game;
import com.EleziEjup.TicTacToe.data.entity.User;
import com.EleziEjup.TicTacToe.data.enums.GameStatus;
import com.EleziEjup.TicTacToe.data.repository.GameRepository;
import com.EleziEjup.TicTacToe.data.repository.UserRepository;
import com.EleziEjup.TicTacToe.exception.BadRequestException;
import com.EleziEjup.TicTacToe.exception.ConflictException;
import com.EleziEjup.TicTacToe.exception.ForbiddenException;
import com.EleziEjup.TicTacToe.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public GameService(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public Game createGame(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found")
        );

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
            try {
                gameStatus = GameStatus.valueOf(status);
            }catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid status");
            }
        }
        LocalDateTime beforeDate = null;
        if (before != null) {
            try {
                beforeDate = LocalDateTime.parse(before);
            }catch (DateTimeParseException e) {
                throw new BadRequestException("Invalid date");
            }
        }

        LocalDateTime afterDate = null;
        if (after != null) {
            try{
                afterDate = LocalDateTime.parse(after);
            }catch (DateTimeParseException e) {
                throw new BadRequestException("Invalid date");
            }
        }
        return gameRepository.filterGames(username,gameStatus,beforeDate,afterDate,pageable);
    }

    public Game joinGame(int gameId, String username){
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new NotFoundException("Game not found")
        );
        if(!game.getStatus().equals(GameStatus.OPEN)){
            throw new ConflictException("Game is not open");
        }

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found")
        );
        if(game.getPlayerX().getUsername().equals(username)){
            throw new ForbiddenException("Cannot join ur own game");
        }

        game.setPlayerO(user);
        game.setStatus(GameStatus.IN_PROGRESS);

        return gameRepository.save(game);
    }

    public String getBoardState(int gameId){
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new NotFoundException("Game not found")
        );
        return game.getBoard();
    }

    public Game getGame(int gameId){
        return gameRepository.findById(gameId).orElseThrow(
                () -> new NotFoundException("Game not found")
        );
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

    public char getSymbol(Game game, String username){
        char symbol;
        if(game.getPlayerX().getUsername().equals(username)){
            symbol = 'X';
        }else{
            symbol = 'O';
        }

        return symbol;
    }

    public Game move(int gameId, String username, int position){
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new NotFoundException("Game not found")
        );

        if(!game.getStatus().equals(GameStatus.IN_PROGRESS)){
            throw new ConflictException("Game is not in progress");
        }

        if(!game.getCurrentTurn().getUsername().equals(username)){
            throw new ForbiddenException("It is not your turn");
        }

        if (position < 0 || position > 8) {
            throw new BadRequestException("Choose a valid position");
        }

        if (game.getBoard().charAt(position) != '.') {
            throw new BadRequestException("Position taken");
        }

        StringBuilder board = new  StringBuilder(game.getBoard());
        board.setCharAt(position,getSymbol(game,username));
        game.setBoard(board.toString());

        if(hasWinner(game.getBoard(),getSymbol(game,username))){
            User winner = userRepository.findByUsername(username).orElseThrow(
                    () -> new NotFoundException("User not found")
            );
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