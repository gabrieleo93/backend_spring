package com.example.pruebacursosformacion.controladores;

import com.example.pruebacursosformacion.configuracion.JwtUtil;
import com.example.pruebacursosformacion.configuracion.SecurityConfig;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.servicios.EmpleadoDAO;
import com.example.pruebacursosformacion.solicitudes.AutenticacionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AutenticacionController.class)
@Import(SecurityConfig.class)
/*-- Sin mockito login --*/
/*@SpringBootTest
@AutoConfigureMockMvc*/
public class AutenticacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoDAO empleadoDAO;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;


          /*---- VERSION 2  TEST REGISTRO -----*/
    @Test

    public void testRegister() throws Exception {
        // Crear un objeto Empleado para simular el registro
        Empleado empleado = new Empleado();
        empleado.setNombreEmpleado("Empleado Registro Prueba Test");
        empleado.setEmailEmpleado("usuarioRegistro@sapiens.com");
        empleado.setPassword("password");
        empleado.setRol("ADMIN");

        // Convertir el objeto Empleado a JSON
        String empleadoJson = objectMapper.writeValueAsString(empleado);

        // Configurar el mock para insertar el empleado
        when(empleadoDAO.insertarActualizaEmlpleado(any(Empleado.class))).thenReturn(true);


        // Configurar el mock para el encoder
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$c9S0jsmCM7F.cuOhkIqQw.W1kSMNKPpj0K49iYrkG6a0viuKejGsW");

        // Realizar la petición de registro y verificar el resultado
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(empleadoJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario registrado exitosamente"));

        // Verificar que el método insertarActualizaEmpleado se llamó una vez con el empleado correcto
        verify(empleadoDAO, times(1)).insertarActualizaEmlpleado(any(Empleado.class));
    }

    /*-- TEST LOGIN SIN MOCKITO --*/

   /* @Test
    public void testLoginSM() throws Exception {
        // Crear un objeto AutenticacionRequest para simular la solicitud de inicio de sesión
        AutenticacionRequest authenticationRequest = new AutenticacionRequest();
        authenticationRequest.setUsername("usuarioRegistro@sapiens.com");
        authenticationRequest.setPassword("password");

        // Simular la carga del usuario
        UserDetails userDetails = User.builder()
                .username(authenticationRequest.getUsername())
                .password("password")
                .roles("ADMIN")
                .build();

        when(empleadoDAO.loadUserByUsername(authenticationRequest.getUsername())).thenReturn(userDetails);

        // Simular la autenticación exitosa
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        // Simular la generación del token JWT
        when(jwtUtil.generateToken(userDetails)).thenReturn("fake-jwt-token");

        // Convertir el objeto AutenticacionRequest a JSON
        String authRequestJson = objectMapper.writeValueAsString(authenticationRequest);

        // Realizar la petición de inicio de sesión y verificar el resultado
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("fake-jwt-token"));

        // Verificar que el método authenticate se llamó una vez
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));

        // Verificar que el método loadUserByUsername se llamó una vez con el nombre de usuario correcto
        verify(empleadoDAO, times(1)).loadUserByUsername(authenticationRequest.getUsername());

        // Verificar que el método generateToken se llamó una vez con el UserDetails correcto
        verify(jwtUtil, times(1)).generateToken(userDetails);
    }*/


}
