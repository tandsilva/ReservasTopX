package com.reservastopx.config;

import com.reservastopx.config.JwtAuthenticationFilter; // ajuste o pacote do seu filtro
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    // rotas públicas (SEM token)
    private static final String[] PUBLIC = {
            "/auth/**",
            // NÃO coloque "/users/**" aqui para não abrir /users/password!
            "/reservas/**",
            "/actuator/**",
            "/error",
            "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**"
    };

    // injete seu filtro JWT (recomendado)
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // compatível com JWT e padrão de mercado
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC).permitAll()

                        // 🔒 Trava SOMENTE o endpoint de senha:
                        .requestMatchers(HttpMethod.POST, "/users/password").authenticated()
                        // (se também tiver PUT/GET para senha, habilite as linhas abaixo)
                        // .requestMatchers(HttpMethod.PUT, "/users/password").authenticated()
                        // .requestMatchers(HttpMethod.GET, "/users/password").authenticated()

                        // opcional: se tiver outras rotas realmente sensíveis, coloque aqui.
                        // .requestMatchers("/secure/**").authenticated()

                        // todo o resto aberto
                        .anyRequest().permitAll()
                )
                // o filtro JWT deve rodar antes do UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // AuthenticationManager (necessário no Spring Security 6 p/ injetar no AuthController)
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    // CORS básico (ajuste as origens do seu front)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
