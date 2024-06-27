package com.example.pruebacursosformacion.controladores;

import com.example.pruebacursosformacion.configuracion.JwtUtil;
import com.example.pruebacursosformacion.servicios.EmpleadoDAO;
import com.example.pruebacursosformacion.solicitudes.AutenticacionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LoginTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmpleadoDAO empleadoDAO;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AutenticacionController autenticacionController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
   // @WithMockUser(username = "test@sapiens.com", roles = {"USER"})
    public void testLogin() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(autenticacionController).build();

        // Crear un objeto AutenticacionRequest para simular la solicitud de inicio de sesión
        AutenticacionRequest authenticationRequest = new AutenticacionRequest();
        authenticationRequest.setUsername("usuarioRegistro@sapiens.com");
        authenticationRequest.setPassword("password");

        // Crear un UserDetails simulado
        UserDetails userDetails = User.builder()
                .username(authenticationRequest.getUsername())
                .password("password")
                .roles("ADMIN")
                .build();

        when(empleadoDAO.loadUserByUsername(authenticationRequest.getUsername())).thenReturn(userDetails);

        // Simular la autenticación exitosa
        // Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        // when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

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

        // Verificar que el método loadUserByUsername se llamó una vez con el nombre de usuario correcto
        verify(empleadoDAO, times(1)).loadUserByUsername(authenticationRequest.getUsername());

        // Verificar que el método generateToken se llamó una vez con el UserDetails correcto
        verify(jwtUtil, times(1)).generateToken(userDetails);

    }
}
