package com.example.leave.controller;

import com.example.leave.dto.AuthDTO;
import com.example.leave.model.User;
import com.example.leave.security.JwtUtils;
import com.example.leave.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthDTO.LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            if (!userOpt.isPresent()) {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            User user = userOpt.get();
            String token = jwtUtils.generateToken(user.getUsername(), user.getRole().name());
            AuthDTO.JwtResponse response = new AuthDTO.JwtResponse(
                    token,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody AuthDTO.RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "Username already taken");
            return ResponseEntity.badRequest().body(error);
        }
        if (userService.existsByEmail(request.getEmail())) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "Email already in use");
            return ResponseEntity.badRequest().body(error);
        }
        User.Role role = User.Role.STUDENT;
        if (request.getRole() != null && !request.getRole().trim().isEmpty()) {
            try {
                role = User.Role.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "Invalid role. Use: STUDENT, ADMIN, TEACHER");
                return ResponseEntity.badRequest().body(error);
            }
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        User saved = userService.save(user);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("message", "User registered successfully");
        response.put("userId", saved.getId());
        response.put("role", saved.getRole().name());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Object> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "Not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        Optional<User> userOpt = userService.findByUsername(authentication.getName());
        if (!userOpt.isPresent()) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        User user = userOpt.get();
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());
        return ResponseEntity.ok(response);
    }
}