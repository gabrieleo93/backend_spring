package com.example.pruebacursosformacion.controladores;

import com.example.pruebacursosformacion.Excepciones.ResourceNotFoundException;
import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.entidades.Tipo;
import com.example.pruebacursosformacion.servicios.CursoDAO;
import com.example.pruebacursosformacion.servicios.EmpleadoDAO;
import com.example.pruebacursosformacion.solicitudes.CursoResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoDAO cursoDAO;

    @Autowired
    private EmpleadoDAO empleadoDAO;

    @PostMapping("/insertar")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public boolean insertarCurso(@RequestBody Curso curso) {
        return cursoDAO.insertarActualizarCurso(curso);
    }

    @PutMapping("/actualizar/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public boolean actualizarCurso(@PathVariable long id, @RequestBody Curso curso) {
        curso.setIdCurso(id); // Asegúrate de que el curso tenga el ID correcto
        return cursoDAO.insertarActualizarCurso(curso);
    }

    @GetMapping("/todos")
    public List<CursoResponse> obtenerTodosLosCursos() {
        List<Curso> cursos = cursoDAO.obtenerTodosLosCursos();
        return cursos.stream().map(this::convertirACursoDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> obtenerCursoPorId(@PathVariable long id) {
        try {
            Curso curso = cursoDAO.obtenerCursoPorId(id);
            CursoResponse cursoDTO = convertirACursoDTO(curso);
            return ResponseEntity.ok(cursoDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN')")
    public boolean eliminarCursoPorId(@PathVariable long id) {
        return cursoDAO.eliminarCursoPorId(id);
    }

    @DeleteMapping("/eliminar")
    //@PreAuthorize("hasAnyRole('ADMIN')")
    public boolean eliminarCurso(@RequestBody Curso curso) {
        return cursoDAO.eliminarCurso(curso);
    }

    //@GetMapping("/por-empleado") // Da error 400 ya que hay un requestBody para el empleado
   /* @PostMapping("/por-empleado")
   // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CursoResponse>> obtenerCursosPorEmpleado(@RequestBody Empleado empleado) {
        List<Curso> cursos = cursoDAO.obtenerCursosPorEmpleado(empleado);
        if (cursos != null && !cursos.isEmpty()) {
            List<CursoResponse> cursosDTO = cursos.stream()
                    .map(this::convertirACursoDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cursosDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
    @PostMapping("/por-empleado/{idEmpleado}")
    public ResponseEntity<List<CursoResponse>> obtenerCursosPorEmpleado(@PathVariable long idEmpleado) {
        Empleado empleado = empleadoDAO.obtenerEmpleadoPorId(idEmpleado);
        if (empleado != null) {
            List<Curso> cursos = cursoDAO.obtenerCursosPorEmpleado(empleado);
            if (cursos != null && !cursos.isEmpty()) {
                List<CursoResponse> cursosDTO = cursos.stream()
                        .map(this::convertirACursoDTO)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(cursosDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenados-por-calificacion")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CursoResponse>> obtenerCursosOrdenadosPorCalificacionFinal() {
        List<Curso> cursos = cursoDAO.obtenerCursosOrdenadosPorCalificacionFinal();
        if (cursos != null && !cursos.isEmpty()) {
            List<CursoResponse> cursosDTO = cursos.stream()
                    .map(this::convertirACursoDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cursosDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{id}/porcentaje-empleados")
    //@PreAuthorize("hasAnyRole('ADMIN')")
    public double obtenerPorcentajeEmpleadosQueRealizanCurso(@PathVariable long id) {
        Curso curso = cursoDAO.obtenerCursoPorId(id);
        return cursoDAO.obtenerPorcentajeEmpleadosQueRealizanCurso(curso);
    }

    @PostMapping("/porcentaje-empleados")
   // @PreAuthorize("hasAnyRole('ADMIN')")
    public double obtenerPorcentajeEmpleadosQueRealizanCurso(@RequestBody Curso curso) {
        return cursoDAO.obtenerPorcentajeEmpleadosQueRealizanCurso(curso);
    }

    @PostMapping("/por-tipo")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CursoResponse>> obtenerCursosPorTipo(@RequestBody Tipo tipo) {
        List<Curso> cursos = cursoDAO.obtenerCursosPorTipo(tipo);
        if (cursos != null && !cursos.isEmpty()) {
            List<CursoResponse> cursosDTO = cursos.stream()
                    .map(this::convertirACursoDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cursosDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/por-proveedor")
    //@PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<CursoResponse>> obtenerCursosPorProveedor(@RequestParam String proveedor) {
        List<Curso> cursos = cursoDAO.obtenerCursosPorProveedor(proveedor);
        if (cursos != null && !cursos.isEmpty()) {
            List<CursoResponse> cursosDTO = cursos.stream()
                    .map(this::convertirACursoDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cursosDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/por-nombre-proveedor")
    //@PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<CursoResponse>> obtenerCursosPorNombreYProveedor(@RequestParam String nombre, @RequestParam String proveedor) {
        List<Curso> cursos = cursoDAO.obtenerCursosPorNombreYProveedor(nombre, proveedor);
        if (cursos != null && !cursos.isEmpty()) {
            List<CursoResponse> cursosDTO = cursos.stream()
                    .map(this::convertirACursoDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cursosDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private CursoResponse convertirACursoDTO(Curso curso) {
        return CursoResponse.builder()
                .idCurso(curso.getIdCurso())
                .nombreCurso(curso.getNombreCurso())
                .proveedor(curso.getProveedor())
                .urlCurso(curso.getUrlCurso())
                .tipoCurso(curso.getTipoCurso())
                .clasificacionFinal(curso.getClasificacionFinal())
                .precio(curso.getPrecio())
                .build();
    }

}
