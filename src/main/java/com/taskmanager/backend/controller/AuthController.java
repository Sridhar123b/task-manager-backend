package com.taskmanager.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.backend.config.JwtUtil;
import com.taskmanager.backend.model.Role;
import com.taskmanager.backend.model.User;
import com.taskmanager.backend.repository.UserRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    // SIGNUP
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {

        System.out.println(user.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());

        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            return ResponseEntity
                    .badRequest()
                    .body("Email already exists");
        }

        // Encode password
        user.setPassword(
                passwordEncoder.encode(user.getPassword()));

        // ADMIN EMAIL
        if (user.getEmail().equals("bottasridhar159@gmail.com")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.MEMBER);
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {

        User user = userRepository.findByEmail(loginUser.getEmail());

        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Email not found");
        }

        // Password check
        if (!passwordEncoder.matches(
                loginUser.getPassword().trim(),
                user.getPassword().trim())) {

            return ResponseEntity
                    .badRequest()
                    .body("Wrong password");
        }

        // Generate JWT token
        String token = JwtUtil.generateToken(user.getEmail());

        // Response object
        Map<String, Object> response = new HashMap<>();

        response.put("token", token);
        response.put("email", user.getEmail());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }

    // FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody User userRequest) {

        User user = userRepository.findByEmail(
                userRequest.getEmail());

        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Email not found");
        }

        // Encode new password
        user.setPassword(
                passwordEncoder.encode(
                        userRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(
                "Password updated successfully");
    }

    // TEST API
    @GetMapping("/test")
    public String test() {
        return "Backend Working";
    }
}