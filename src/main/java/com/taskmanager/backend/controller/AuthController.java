package com.taskmanager.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.taskmanager.backend.config.JwtUtil;
import com.taskmanager.backend.model.User;
import com.taskmanager.backend.model.Role;
import com.taskmanager.backend.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @PostMapping("/signup")
public ResponseEntity<?> signup(@RequestBody User user) {

    User existingUser = userRepository.findByEmail(user.getEmail());

    if (existingUser != null) {
        return ResponseEntity
                .badRequest()
                .body("Email already exists");
    }
    user.setPassword(
        passwordEncoder.encode(user.getPassword()));

user.setRole(Role.MEMBER);

    return ResponseEntity.ok(userRepository.save(user));
}
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User loginUser) {

    User user = userRepository.findByEmail(loginUser.getEmail());

    if(user == null) {
        return ResponseEntity.badRequest()
                .body("Email not found");
    }
    

if(!passwordEncoder.matches(
        loginUser.getPassword(),
        user.getPassword())) {

    return ResponseEntity.badRequest()
            .body("Wrong password");
}


    String token = JwtUtil.generateToken(user.getEmail());

    return ResponseEntity.ok(token);
}
@GetMapping("/test")
public String test() {
    return "Backend Working";
}

}