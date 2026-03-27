package com.EleziEjup.TicTacToe.services;

import com.EleziEjup.TicTacToe.data.entity.User;
import com.EleziEjup.TicTacToe.data.repository.UserRepository;
import com.EleziEjup.TicTacToe.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return null;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new RuntimeException("Username not found");
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return jwtService.generateToken(username);
    }

}
