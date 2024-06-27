package com.example.pruebacursosformacion.repositorio;

import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.EmpleadoCursoAsociacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpleadoCursoAsociacionRepositorio extends JpaRepository <EmpleadoCursoAsociacion, Long> {
    List<EmpleadoCursoAsociacion> findByCurso(Curso curso);
}
