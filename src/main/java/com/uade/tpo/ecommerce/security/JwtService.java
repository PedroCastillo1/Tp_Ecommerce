package com.uade.tpo.ecommerce.security;

// ## SERVICIO — JWT (JSON Web Token)
// ##
// ## Genera y valida los tokens JWT para autenticar usuarios.
// ## El token se firma con HMAC-SHA256 usando una clave secreta.
// ##
// ## Estructura del JWT generado:
// ##   Header:    { alg: "HS256", typ: "JWT" }
// ##   Payload:   { sub: email, userId: id, iat: fechaCreación, exp: fechaExpiración }
// ##   Signature: HMAC-SHA256(base64(header) + "." + base64(payload), secretKey)
// ##
// ## Expiración: 24 horas (igual que la cookie que contiene el token)

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // ## Clave secreta en Base64 para firmar el token
    // ## PRODUCCIÓN: mover a application.properties o variable de entorno (no hardcoded)
    private static final String SECRET_KEY = "NDI5NDk2NzI5NmgyNDI0MjQwNDI5NDk2NzI5NmgyNDI0MjQw";

    // ## Extrae el email del usuario (campo "subject") del JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ## Extrae cualquier claim del JWT usando una función resolver
    // ## Ejemplo: extractClaim(token, Claims::getExpiration)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ## Genera un JWT sin claims adicionales (solo email y expiración)
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // ## Genera un JWT con claims adicionales (como userId)
    // ## El subject del token es el email del usuario (usado para cargar el user en cada request)
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // ## email del usuario
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // ## 24 horas
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ## Valida el token: verifica que el email coincida con el usuario y que no esté expirado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // ## Compara la fecha de expiración del token con la fecha actual
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ## Parsea el JWT y retorna todos sus claims (el payload decodificado)
    // ## Lanza RuntimeException si la firma es inválida o el token está expirado
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Token inválido o expirado");
        }
    }

    // ## Decodifica la clave Base64 y la convierte en un objeto Key para HMAC-SHA256
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
