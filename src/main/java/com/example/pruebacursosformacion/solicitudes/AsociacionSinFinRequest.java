package com.example.pruebacursosformacion.solicitudes;

import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AsociacionSinFinRequest {
    private Empleado empleado;
    private Curso curso;
    private int calificacion;
    private String titulo;
    private boolean estado;
    private Date fechaInicio;
}
