package com.reservastopx.config; // se preferir, mova para com.reservastopx.security

import com.reservastopx.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // Não filtra auth, swagger e OPTIONS (preflight)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = Optional.ofNullable(request.getServletPath()).orElse("");
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
        return path.startsWith("/auth/")
                || path.startsWith("/v3/api-docs/")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/error")
                || path.startsWith("/actuator");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        // Se já tem auth no contexto, segue
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(req, res);
            return;
        }

        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = auth.substring(7);

        try {
            if (!jwtUtil.isTokenValid(token)) {
                chain.doFilter(req, res);
                return;
            }

            String username = jwtUtil.getUsernameFromToken(token);
            if (username == null || username.isBlank()) {
                chain.doFilter(req, res);
                return;
            }

            // Tenta obter lista de roles do token
            List<String> roles = jwtUtil.getRolesFromToken(token); // ex.: ["ADMIN"] ou ["ROLE_ADMIN"]
            if (roles == null || roles.isEmpty()) {
                // fallback: alguns projetos guardam "role" singular
                String singleRole = safeGetSingleRole(jwtUtil, token); // tenta "ADMIN"/"ROLE_ADMIN"
                roles = singleRole == null ? List.of() : List.of(singleRole);
            }

            // Normaliza para sempre "ROLE_..."
            var authorities = roles.stream()
                    .filter(Objects::nonNull)
                    .map(this::ensureRolePrefix)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception ignored) {
            // qualquer problema → não autentica; SecurityConfig decide (401/403)
        }

        chain.doFilter(req, res);
    }

    private String ensureRolePrefix(String role) {
        String r = role.trim();
        return r.startsWith("ROLE_") ? r : "ROLE_" + r;
    }

    // Fallback seguro para pegar uma role singular se seu JwtUtil der suporte
    private String safeGetSingleRole(JwtUtil jwtUtil, String token) {
        try {
            // Se você tiver jwtUtil.getRoleFromToken(token) use aqui:
            // return jwtUtil.getRoleFromToken(token);
            return null; // mantenha null se não existir método singular
        } catch (Exception e) {
            return null;
        }
    }
}
