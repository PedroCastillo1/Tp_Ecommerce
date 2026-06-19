package com.uade.tpo.ecommerce.security;

// ## FILTRO DE SEGURIDAD — JWT desde Cookie HTTP-Only
// ##
// ## Intercepta TODAS las requests HTTP antes de que lleguen a los controllers.
// ## Lee el token JWT desde la cookie llamada "jwt" y autentica al usuario.
// ##
// ## Flujo por cada request:
// ##   1. Buscar la cookie "jwt" entre las cookies de la request
// ##   2. Si no hay cookie → continuar sin autenticar (Spring Security decidirá)
// ##   3. Extraer el email (subject) del JWT con JwtService
// ##   4. Cargar el usuario de la DB por email
// ##   5. Validar firma y expiración del token
// ##   6. Setear la autenticación en SecurityContextHolder
// ##   7. Continuar con el siguiente filtro de la cadena
// ##
// ## Extiende OncePerRequestFilter para garantizar que se ejecuta una sola vez por request

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.uade.tpo.ecommerce.service.IUserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IUserService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String userEmail;

        try {
            // ## Paso 1: buscar la cookie "jwt" entre todas las cookies de la request
            // ## (no final → permite asignación dentro del for-loop)
            String jwt = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }

            // ## Paso 2: si no hay cookie JWT, pasar al siguiente filtro sin autenticar
            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // ## Paso 3: extraer el email del token (está en el campo "subject" del JWT)
            userEmail = jwtService.extractUsername(jwt);

            // ## Pasos 4-6: autenticar solo si hay email y no hay autenticación previa
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // ## Paso 5: verificar que la firma sea válida y el token no esté expirado
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // ## Paso 6: crear el token de autenticación y cargarlo en el contexto
                    // ## A partir de aquí, @AuthenticationPrincipal funciona en los controllers
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // ## Paso 7: continuar con el siguiente filtro
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // ## Si el token es inválido o expirado, continuamos sin autenticar
            // ## (no lanzamos excepción — Spring Security bloqueará si la ruta requiere auth)
            filterChain.doFilter(request, response);
        }
    }
}
