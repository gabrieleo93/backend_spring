package com.example.pruebacursosformacion.controladores;

import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.servicios.CursoDAO;
import com.example.pruebacursosformacion.servicios.EmpleadoCursoAsociacionServicio;
import com.example.pruebacursosformacion.servicios.EmpleadoDAO;
import com.example.pruebacursosformacion.solicitudes.AsociacionRequest;
import com.example.pruebacursosformacion.solicitudes.AsociacionSinFinRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/asociacion")
public class AsociacionController {

    @Autowired
    private EmpleadoCursoAsociacionServicio asociacionServicio;

   /* @Autowired
    private EmpleadoDAO empleadoDAO;

    @Autowired
    private CursoDAO cursoDAO;*/

   @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/asociar")
    public ResponseEntity<String> asociarCurso(@RequestBody AsociacionRequest request) {
        try {
            asociacionServicio.asociarCurso(
                request.getIdEmpleado(),
                request.getCurso(),
                request.getCalificacion(),
                request.getTitulo(),
                request.isEstado(),
                request.getFechaInicio(),
                request.getFechaFin()
            );
            return ResponseEntity.ok("Asociación creada exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/asociarSinFin")
    public ResponseEntity<String> asociarCursoSinFin(@RequestBody AsociacionSinFinRequest request) {
        try {
            asociacionServicio.asociarCurso(
                request.getEmpleado(),
                request.getCurso(),
                request.getCalificacion(),
                request.getTitulo(),
                request.isEstado(),
                request.getFechaInicio()
            );
            return ResponseEntity.ok("Asociación creada exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }

}
