package com.example.pruebacursosformacion.repositorio;

import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmpleadoRepositorio extends JpaRepository<Empleado, Long > {
    public Empleado findEmpleadoByNombreEmpleado(String nombreEmpleado);
    public Empleado findEmpleadoByEmailEmpleado(String email);
    @Query("SELECT DISTINCT e FROM Empleado e JOIN FETCH e.cursos ec WHERE ec.curso = :curso")
    List<Empleado> findByCurso(@Param("curso") Curso curso);
}
