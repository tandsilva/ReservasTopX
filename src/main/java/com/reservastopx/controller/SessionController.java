package com.reservastopx.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class SessionController {

    @GetMapping("/whoami")
    public ResponseEntity<Map<String,Object>> whoami(Authentication auth) {
        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)   // ex.: ROLE_ADMIN
                .findFirst().orElse("ROLE_USER")
                .replaceFirst("^ROLE_", "");           // ADMIN/USER

        return ResponseEntity.ok(Map.of(
                "username", auth.getName(),
                "role", role
        ));
    }
}
