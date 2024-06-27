package com.example.pruebacursosformacion.configuracion;

import com.example.pruebacursosformacion.servicios.EmpleadoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    @Lazy
    private EmpleadoDAO empleadoDAO;

   @Autowired
   private JwtUtil jwtUtil;


    @Bean
    //@Profile("!test")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Crear instancia de JwtRequestFilter
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(empleadoDAO, jwtUtil);


        http
                .csrf(csrf -> csrf.disable()) // Deshabilita la protección CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/register", "/auth/login").permitAll() // Permite el acceso sin autenticación a las rutas de registro y login
                        .requestMatchers("/todos", "/insertar", "/actualizar/{id}", "/por-empleado", "/ordenados-por-calificacion", "/por-tipo", "/asociar", "/por-empleado").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/eliminar/{id}", "/eliminar", "/{id}/porcentaje-empleados", "/porcentaje-empleados", "/por-proveedor", "/por-nombre-proveedor", "/curso/{idCurso}").hasAnyRole("ADMIN")
                        .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configura la política de sesión como STATELESS (donde no se mantiene ninguna sesión en el servidor.)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Añade un filtro JWT antes del filtro de autenticación de usuario y contraseña

        return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(empleadoDAO).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

/*------------*/


}
