package com.reservastopx.controller;

import com.reservastopx.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class DevTokenController {

    private final JwtUtil jwtUtil;

    // 🔧 DEV ONLY — gere um JWT de ADMIN para teste
    @GetMapping("/dev-token-admin")
    public Map<String,String> devTokenAdmin() {
        var user = org.springframework.security.core.userdetails.User
                .withUsername("admin")
                .password("x")
                .authorities("ROLE_ADMIN")
                .build();
        return Map.of("token", jwtUtil.generateToken(user));
    }

    // 🔧 DEV ONLY — gere um JWT de USER para teste
    @GetMapping("/dev-token-user")
    public Map<String,String> devTokenUser() {
        var user = org.springframework.security.core.userdetails.User
                .withUsername("user")
                .password("x")
                .authorities("ROLE_USER")
                .build();
        return Map.of("token", jwtUtil.generateToken(user));
    }
}
