package com.EleziEjup.TicTacToe.controller;

import com.EleziEjup.TicTacToe.dto.AuthDto;
import com.EleziEjup.TicTacToe.dto.UserDto;
import com.EleziEjup.TicTacToe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody AuthDto user) {
        if(user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            return "Fields must not be empty";
        }
        userService.register(user.getUsername(), user.getPassword());
        return "Resitered " +user.getUsername();
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthDto user) {
        return userService.login(user.getUsername(), user.getPassword());
    }

    @GetMapping("/users/{username}")
    public UserDto getProfile(@PathVariable String username){
        return userService.getProfile(username);
    }

}
