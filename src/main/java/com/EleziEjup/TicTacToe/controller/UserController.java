package com.EleziEjup.TicTacToe.controller;

import com.EleziEjup.TicTacToe.data.entity.User;
import com.EleziEjup.TicTacToe.dto.AuthDto;
import com.EleziEjup.TicTacToe.dto.UserDto;
import com.EleziEjup.TicTacToe.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody AuthDto user) {
        return userService.register(user.getUsername(), user.getPassword());
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody AuthDto user) {
        return userService.login(user.getUsername(), user.getPassword());
    }

    @GetMapping("/users/{username}")
    public UserDto getProfile(@PathVariable String username){
        return userService.getProfile(username);
    }

}
