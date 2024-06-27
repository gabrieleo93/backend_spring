package com.example.pruebacursosformacion.configuracion;

import com.example.pruebacursosformacion.servicios.EmpleadoDAO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.security.SignatureException;
import java.io.IOException;



@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    // INYECCION DE CONSTRUCTOR (EVITAR DEPENDENCIAS CICLICAS)
    private final EmpleadoDAO empleadoDAO;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtRequestFilter(/*@Lazy*/ EmpleadoDAO empleadoDAO, JwtUtil jwtUtil) {
        this.empleadoDAO = empleadoDAO;
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");  // Obtener el header 'Authorization' de la solicitud HTTP

        String username = null;
        String jwt = null;

        // Extracción del token JWT (Si no es nulo y comienza por "Bearer")
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            //username = jwtUtil.extractUsername(jwt);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
            } catch (SignatureException e) {
                logger.error("JWT signature does not match locally computed signature");
            }

        }

        // Verificación del Usuario Autenticado (si no es nulo y no hay autentificación previa)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.empleadoDAO.loadUserByUsername(username);

            // Validación del token
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // Creación el Objeto de autenticación
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Establece el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // Continia la cadena de filtros
        chain.doFilter(request, response);
    }

}
