package com.reservastopx.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret; // garanta entropia >= 256 bits para HS256

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private Key getKey() {
        // gere/defina um segredo com >=32 bytes (256 bits) para HS256
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /** Gera token com subject=username e claim roles=[...]. */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roles);

        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiry = Date.from(now.plusMillis(expirationMs));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) // ✅ subject correto
                .setIssuedAt(issuedAt)
                .setExpiration(expiry)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Extrai o username do subject. */
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /** Extrai as roles do token. */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Object val = parseClaims(token).get("roles");
        if (val instanceof List<?> list) {
            return (List<String>) list; // confia-se que foi gravado como List<String>
        }
        return List.of();
    }

    /** Valida assinatura e expiração. */
    public boolean isTokenValid(String token) {
        try {
            Claims c = parseClaims(token);
            Date exp = c.getExpiration();
            return exp != null && exp.after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** Mantive por compatibilidade — agora usa o parser correto. */
    public String validateAndGetSubject(String token) {
        try {
            return parseClaims(token).getSubject();
        } catch (JwtException e) {
            throw new JwtException("Token inválido ou expirado", e);
        }
    }

    /** Compatível com versões existentes do seu código. */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = getUsernameFromToken(token);
            return username != null
                    && username.equals(userDetails.getUsername())
                    && isTokenValid(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /* -------------------- PRIVADOS -------------------- */

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
