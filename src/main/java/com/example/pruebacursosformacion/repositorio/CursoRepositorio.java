package com.example.pruebacursosformacion.repositorio;

import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.entidades.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CursoRepositorio extends JpaRepository<Curso, Long> {
    Optional<Curso> findByNombreCurso(String nombreCurso);
    // clase introducida en Java 8 que se utiliza para representar un valor opcional que puede estar presente o ausente.
    // En el contexto de Spring Data JPA, se utiliza principalmente para representar el resultado de una operación de
    // búsqueda que puede o no devolver un valor.

    List<Curso> findByTipoCurso(Tipo tipo);

    @Query("SELECT c FROM Curso c JOIN FETCH c.empleados ec WHERE ec.empleado = :empleado")
    List<Curso> findCursosByEmpleado(@Param("empleado") Empleado empleado);

    @Query("SELECT COUNT(ec) FROM EmpleadoCursoAsociacion ec WHERE ec.curso = :curso")
    long contarEmpleadosQueRealizaronCurso(@Param("curso") Curso curso);

    List<Curso> findByProveedor(String proveedor);
    List<Curso> findByNombreCursoAndProveedor(String nombreCurso, String proveedor);

}
