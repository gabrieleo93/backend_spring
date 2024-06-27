package com.example.pruebacursosformacion.configuracion;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final JwtKeyProvider jwtKeyProvider;

    @Autowired
    public JwtUtil(JwtKeyProvider jwtKeyProvider) {
        this.jwtKeyProvider = jwtKeyProvider;
    }


    // Método para extraer el nombre de usuario del token JWT
    public String extractUsername(String token) {
        /* Utiliza el método extractClaim para obtener el sujeto (nombre de usuario) del token */
        return extractClaim(token, Claims::getSubject);
    }

    // Método para extraer la fecha de expiración del token JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Método genérico para extraer cualquier claim (reclamación) del token JWT
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        /* Utiliza el método extractAllClaims para obtener todas las reclamaciones del token y
        luego aplica la función de resolución para obtener la reclamación específica. */
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Método privado para extraer todas las reclamaciones del token JWT (necesite hacerlo publico para las pruebas)
    public Claims extractAllClaims(String token) {
       // return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
       // return Jwts.parser().setSigningKey(jwtKeyProvider.generateKey()).parseClaimsJws(token).getBody();
        return Jwts.parserBuilder().setSigningKey(jwtKeyProvider.getSecretKey()).build().parseClaimsJws(token).getBody();
    }

    // Método para verificar si el token JWT ha expirado
    private Boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    // Método para generar un nuevo token JWT a partir de los detalles del usuario
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    // Método privado para crear el token JWT con reclamaciones, sujeto y firma
    private String createToken(Map<String, Object> claims, String subject) {

        /*return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(SignatureAlgorithm.HS256,  jwtKeyProvider.generateKey()).compact();*/

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(jwtKeyProvider.getSecretKey())
                .compact();


    }

    // Método para validar el token JWT comparando el nombre de usuario y verificando la expiración
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
