package com.example.pruebacursosformacion.entidades;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Entity
@Scope("singleton")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "PRUEBA_CURSO")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia")
    @SequenceGenerator(name = "secuencia", sequenceName = "SECUENCIA_PRUEBA_CURSO", allocationSize = 1)
    private long idCurso;
    @Column(unique = true)
    private String nombreCurso;
    private String proveedor;
    /*private Date fechaInicio;
    private Date fechaFin;*/
    private String urlCurso;
    //private Enum<Tipo> tipoCurso;
    private Tipo tipoCurso;
    private double clasificacionFinal;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<EmpleadoCursoAsociacion> empleados;

    private Double precio;

}
