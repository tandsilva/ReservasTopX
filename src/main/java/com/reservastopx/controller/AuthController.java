package com.reservastopx.controller;

import com.reservastopx.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    public record AuthRequest(String username, String password) {}
    public record AuthResponse(String token) {}

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        // validação simples — troque pela sua lógica real
        if (!"admin".equals(req.username()) || !"123".equals(req.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        // cria um UserDetails fake apenas pra gerar o token
        var userDetails = new User(req.username(), req.password(), Collections.emptyList());

        String token = jwtUtil.generateToken(userDetails);
        return new AuthResponse(token);
    }
}
