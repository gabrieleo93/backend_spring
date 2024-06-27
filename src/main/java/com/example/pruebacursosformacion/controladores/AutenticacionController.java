package com.example.pruebacursosformacion.controladores;

import com.example.pruebacursosformacion.configuracion.JwtUtil;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.servicios.EmpleadoDAO;
import com.example.pruebacursosformacion.solicitudes.AutenticacionRequest;
import com.example.pruebacursosformacion.solicitudes.AutenticacionResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AutenticacionController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AutenticacionController.class);
    @Autowired
    private EmpleadoDAO empleadoDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Empleado empleado) {
        boolean isSaved = empleadoDAO.insertarActualizaEmlpleado(empleado);
        if (isSaved) {
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar usuario");
        }
    }

   /* @PostMapping("/login")
    public ResponseEntity<String> login() {
        // Spring Security maneja la autenticación, por lo que no necesitas código aquí
        return ResponseEntity.ok("Login exitoso");
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AutenticacionRequest authenticationRequest) throws Exception {
        // Atenticación usuario
        logger.info("Iniciando autenticación para: " + authenticationRequest.getUsername()+" con contraseña: "+authenticationRequest.getPassword());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
                    //new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), passwordEncoder.encode(authenticationRequest.getPassword()))
            );
            logger.info("Autenticación exitosa para: " + authenticationRequest.getUsername());

            final UserDetails userDetails = empleadoDAO.loadUserByUsername(authenticationRequest.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);
            logger.info("Token JWT generado para: " + authenticationRequest.getUsername());

            Empleado e = empleadoDAO.obtenerEmpleadoPorEmail(authenticationRequest.getUsername());
            // Devuelve la respuesta con el token JWT
            return ResponseEntity.ok(new AutenticacionResponse(jwt,e.getIdEmpleado(),e.getNombreEmpleado(), e.getRol()));

        } catch (BadCredentialsException e) {
            // Maneja la excepción de credenciales inválidas
            logger.error("Credenciales inválidas para el usuario: " + authenticationRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception e) {
            // Maneja otras excepciones
            logger.error("Error durante la autenticación para el usuario: " + authenticationRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error durante la autenticación");
        }

    }

}
