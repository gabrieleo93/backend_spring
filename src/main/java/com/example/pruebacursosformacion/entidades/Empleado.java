package com.example.pruebacursosformacion.entidades;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("singleton")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "EMPLEADO_PRUEBA")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia")
    @SequenceGenerator(name = "secuencia", sequenceName = "SECUENCIA_PRUEBA_EMPLEADO", allocationSize = 1)
    private long idEmpleado;

    private String nombreEmpleado;

    private String apellidosEmpleado;
    @Column(unique = true)
    private String emailEmpleado;
    private String password;
    private String rol;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
    private List<EmpleadoCursoAsociacion> cursos;



}
