package com.EleziEjup.TicTacToe.services;

import com.EleziEjup.TicTacToe.data.entity.User;
import com.EleziEjup.TicTacToe.data.repository.GameRepository;
import com.EleziEjup.TicTacToe.data.repository.UserRepository;
import com.EleziEjup.TicTacToe.dto.UserDto;
import com.EleziEjup.TicTacToe.exception.BadRequestException;
import com.EleziEjup.TicTacToe.exception.ConflictException;
import com.EleziEjup.TicTacToe.exception.NotFoundException;
import com.EleziEjup.TicTacToe.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GameRepository gameRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.gameRepository = gameRepository;
    }

    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ConflictException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new NotFoundException("Username not found");
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new BadRequestException("Wrong password");
        }

        return jwtService.generateToken(username);
    }

    public UserDto getProfile(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                ()  -> new NotFoundException("Username not found")
        );

        int gamesPlayed = gameRepository.countByPlayerXOrPlayerO(user,user);
        int wins = gameRepository.countByWinner(user);

        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setGamesPlayed(gamesPlayed);

        int winrate;
        if (gamesPlayed != 0){
            winrate = (wins * 100) /  gamesPlayed;
        }else {
            winrate = 0;
        }

        userDto.setWinRate(winrate);
        return userDto;
    }

}
