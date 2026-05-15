package com.taskmanager.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.backend.model.User;
import com.taskmanager.backend.repository.UserRepository;
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        return userRepository.save(user);
    }
    @PostMapping("/login")
public User login(@RequestBody User loginUser) {

    return userRepository.findAll()
            .stream()
            .filter(user ->
                    user.getEmail().equals(loginUser.getEmail()) &&
                    user.getPassword().equals(loginUser.getPassword()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));
}
@GetMapping("/test")
public String test() {
    return "Backend Working";
}
}