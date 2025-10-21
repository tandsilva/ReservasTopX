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
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // Não filtra /auth/** (login, refresh, etc.)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path != null && path.startsWith("/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        // Se já tem auth no contexto, segue o fluxo
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(req, res);
            return;
        }

        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            // Sem token → segue; endpoints públicos continuam acessíveis,
            // e os protegidos vão retornar 401/403 pelo SecurityConfig.
            chain.doFilter(req, res);
            return;
        }

        String token = auth.substring(7);

        try {
            // Valida assinatura/expiração
            if (!jwtUtil.isTokenValid(token)) {
                chain.doFilter(req, res); // token inválido → deixa o Security decidir
                return;
            }

            // Extrai subject (username) e roles
            String username = jwtUtil.getUsernameFromToken(token);
            var roles = jwtUtil.getRolesFromToken(token); // ex.: ["ROLE_ADMIN","ROLE_USER"]

            if (username != null) {
                var authorities = roles == null ? java.util.List.<SimpleGrantedAuthority>of()
                        : roles.stream()
                        .filter(Objects::nonNull)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                var authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            // Qualquer problema no parsing: não autentica; deixa o fluxo seguir.
            // Endpoints que exigem auth responderão 401/403.
        }

        chain.doFilter(req, res);
    }
}
