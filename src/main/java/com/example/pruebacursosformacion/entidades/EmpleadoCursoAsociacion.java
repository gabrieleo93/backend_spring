package com.example.pruebacursosformacion.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "EMPLEADO_CURSO_CALIFICACION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoCursoAsociacion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia")
    @SequenceGenerator(name = "secuencia", sequenceName = "SECUENCIA_EMPLEADO_CURSO_CALIFICACION", allocationSize = 1)
    private long id;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;
    @Min(value = 1, message = "{calificacion.minima}")
    @Max(value = 5, message = "{calificacion.maxima}")
    private int calificacion;

    private String titulo;

    private boolean estado;

    private Date fechaInicio;
    private Date fechaFin;
}
