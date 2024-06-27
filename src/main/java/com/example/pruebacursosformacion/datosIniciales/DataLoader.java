package com.example.pruebacursosformacion.datosIniciales;

import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.entidades.EmpleadoCursoAsociacion;
import com.example.pruebacursosformacion.servicios.CursoDAO;
import com.example.pruebacursosformacion.servicios.EmpleadoCursoAsociacionServicio;
import com.example.pruebacursosformacion.servicios.EmpleadoDAO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
public class DataLoader {

    private final EmpleadoDAO empleadoDAO;

    private  final CursoDAO cursoDAO;
    private final EmpleadoCursoAsociacionServicio empleadoCursoAsociacionServicio;

    public DataLoader(EmpleadoDAO empleadoDAO, CursoDAO cursoDAO, EmpleadoCursoAsociacionServicio empleadoCursoAsociacionServicio) {
        this.empleadoDAO = empleadoDAO;
        this.cursoDAO = cursoDAO;
        this.empleadoCursoAsociacionServicio = empleadoCursoAsociacionServicio;
    }

    @PostConstruct
    public void initData(){

        Empleado empleado1 = Empleado.builder()
                .nombreEmpleado("Juan")
                .apellidosEmpleado("Pérez")
                .emailEmpleado("juan@example.com")
                .password("password123")
                .rol("ADMIN")
                .build();

        Empleado empleado2 = Empleado.builder()
                .nombreEmpleado("María")
                .apellidosEmpleado("Gómez")
                .emailEmpleado("maria@example.com")
                .password("password456")
                .rol("ADMIN")
                .build();

        Empleado empleado = new Empleado();
        empleado.setNombreEmpleado("Empleado Registro Prueba");
        empleado.setEmailEmpleado("usuarioRegistro@sapiens.com");
        empleado.setPassword("1234");
        empleado.setRol("ADMIN");

        empleadoDAO.insertarActualizaEmlpleado(empleado);

        empleadoDAO.insertarActualizaEmlpleado(empleado1);
        empleadoDAO.insertarActualizaEmlpleado(empleado2);

        Curso curso1 = Curso.builder()
                .nombreCurso("Curso Angular")
                .proveedor("OpenWebinars")
                .precio(20.0)
                .build();

        Curso curso2 = Curso.builder()
                .nombreCurso("Curso SpringBoot")
                .proveedor("OpenWebinars")
                .precio(150.00)
                .build();

        cursoDAO.insertarActualizarCurso(curso1);
        cursoDAO.insertarActualizarCurso(curso2);

        // Asignar cursos a empleados


        empleadoCursoAsociacionServicio.asociarCurso(empleado1,curso1,4,"titulop.pdf",false,new Date());
        empleadoCursoAsociacionServicio.asociarCurso(empleado1,curso2,5,"titulop.pdf",false,new Date());
        empleadoCursoAsociacionServicio.asociarCurso(empleado2,curso2,5,"titulop.pdf",false,new Date());

        /*EmpleadoCursoAsociacion empleadoCursoAsociacion1 = EmpleadoCursoAsociacion.builder()
                .empleado(empleado1)
                .curso(curso1)
                .calificacion(5)
                .titulo("titulo.pfd")
                .build();

        EmpleadoCursoAsociacion empleadoCursoAsociacion2 = EmpleadoCursoAsociacion.builder()
                .empleado(empleado1)
                .curso(curso2)
                .calificacion(4)
                .titulo("titulo2.pfd")
                .build();

        EmpleadoCursoAsociacion empleadoCursoAsociacion3 = EmpleadoCursoAsociacion.builder()
                .empleado(empleado2)
                .curso(curso1)
                .calificacion(3)
                .titulo("titulo3.pfd")
                .build();

        empleadoCursoAsociacionServicio.insertarActualizarEmpleadoCurso(empleadoCursoAsociacion1);
        empleadoCursoAsociacionServicio.insertarActualizarEmpleadoCurso(empleadoCursoAsociacion2);
        empleadoCursoAsociacionServicio.insertarActualizarEmpleadoCurso(empleadoCursoAsociacion3);*/

    }

}
