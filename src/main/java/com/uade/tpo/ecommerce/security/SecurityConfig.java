package com.uade.tpo.ecommerce.security;

// ## CONFIGURACIÓN DE SEGURIDAD — Spring Security
// ##
// ## Define las reglas globales de seguridad de la aplicación:
// ##   - Qué rutas son públicas y cuáles requieren autenticación
// ##   - Política de sesiones (STATELESS con JWT)
// ##   - Configuración CORS para el frontend React
// ##   - Respuesta 401 cuando no autenticado (no 302 redirect)
// ##   - Encriptación de contraseñas con BCrypt
// ##   - Orden de filtros: JwtAuthenticationFilter antes del filtro estándar de Spring

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    // ## Encriptador BCrypt: genera un hash seguro con salt aleatorio
    // ## Se usa para guardar contraseñas y verificarlas en el login
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ## Manager que Spring usa internamente para validar email+password en login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ## Configuración CORS para permitir requests del frontend React
    // ## IMPORTANTE: allowedOriginPatterns("*") con allowCredentials(true) permite cookies
    // ## (allowedOrigins("*") NO funciona con credenciales — es una restricción del estándar CORS)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));
        config.setAllowCredentials(true); // ## necesario para que el browser envíe cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // ## Cadena principal de filtros de seguridad
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        try {
            http
                // ## Aplicar configuración CORS definida arriba
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ## CSRF deshabilitado: no aplica con JWT stateless (no hay form login)
                .csrf(csrf -> csrf.disable())

                // ## STATELESS: Spring no crea ni usa sesiones HTTP
                // ## Cada request se autentica independientemente via cookie JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ## Reglas de autorización por ruta
                .authorizeHttpRequests(auth -> auth
                    // ## Rutas públicas — accesibles sin cookie JWT
                    .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/logout").permitAll()
                    .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/productos/**").permitAll()
                    .requestMatchers(org.springframework.http.HttpMethod.GET, "/categoria/**").permitAll()
                    // ## Todo lo demás requiere autenticación válida
                    .anyRequest().authenticated()
                )

                // ## Cuando una request no autenticada llega a ruta protegida:
                // ## devolver 401 JSON (no redirigir a /login como hace Spring por defecto)
                // ## Esto es crucial para una REST API — los clientes esperan 401, no HTML
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                )

                // ## Insertar JwtAuthenticationFilter ANTES del filtro estándar de Spring
                // ## para que el JWT sea procesado primero en cada request
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        } catch (Exception e) {
            throw new RuntimeException("Error al configurar la cadena de filtros: " + e.getMessage());
        }
    }
}
