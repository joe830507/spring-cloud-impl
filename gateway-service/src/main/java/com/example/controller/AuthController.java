package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.JwtUtil;

import reactor.core.publisher.Mono;

record LoginRequest(String username, String password) {
}

record AuthResponse(String token) {
}

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody LoginRequest request) {
        return Mono.just(
                "admin".equals(request.username()) && "password".equals(request.password())
                        ? ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(request.username())))
                        : ResponseEntity.badRequest().body("Invalid credentials"));
    }
}