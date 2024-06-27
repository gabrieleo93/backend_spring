package com.example.pruebacursosformacion.configuracion;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
@Component
public class JwtKeyProvider {
    // Método para generar una clave segura
    /*public SecretKey generateKey() {

        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }*/

    private final SecretKey secretKey;

    public JwtKeyProvider() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

}
