package com.example.pruebacursosformacion.controladores;

import com.example.pruebacursosformacion.Excepciones.ResourceNotFoundException;
import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.servicios.CursoDAO;
import com.example.pruebacursosformacion.servicios.EmpleadoDAO;
import com.example.pruebacursosformacion.solicitudes.EmpleadoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoDAO empleadoDAO;
    @Autowired
    private CursoDAO cursoDAO;

    @GetMapping("/todos")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<EmpleadoResponse>> obtenerTodosLosEmpleados() {
        List<Empleado> empleados = empleadoDAO.obtenerTodosLosEmpleados();
        if (empleados != null && !empleados.isEmpty()) {
            List<EmpleadoResponse> empleadosDTO = empleados.stream()
                    .map(this::convertirAEmpleadoDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(empleadosDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<EmpleadoResponse> obtenerEmpleadoPorId(@PathVariable long id) {
        Empleado empleado = empleadoDAO.obtenerEmpleadoPorId(id);
        if (empleado != null) {
            EmpleadoResponse empleadoDTO = convertirAEmpleadoDTO(empleado);
            return ResponseEntity.ok(empleadoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crearEmpleado(@RequestBody Empleado empleado) {
        empleadoDAO.insertarActualizaEmlpleado(empleado);
        EmpleadoResponse empleadoDTO = convertirAEmpleadoDTO(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoDTO);
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<EmpleadoResponse> actualizarEmpleado(@PathVariable long id, @RequestBody Empleado empleado) {
        Empleado empleadoExistente = empleadoDAO.obtenerEmpleadoPorId(id);
        if (empleadoExistente != null) {
            empleado.setIdEmpleado(id);
            empleadoDAO.insertarActualizaEmlpleado(empleado);
            Empleado empleadoActualizado = empleadoDAO.obtenerEmpleadoPorId(id);
            EmpleadoResponse empleadoDTO = convertirAEmpleadoDTO(empleadoActualizado);
            return ResponseEntity.ok(empleadoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable long id) {
        Empleado empleado = empleadoDAO.obtenerEmpleadoPorId(id);
        if (empleado != null) {
            empleadoDAO.eliminarEmpleado(empleado);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/curso/{idCurso}")
    //@PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<EmpleadoResponse>> obtenerEmpleadosPorCurso(@PathVariable long idCurso) {
        try {
            Curso curso = cursoDAO.obtenerCursoPorId(idCurso);
            List<Empleado> empleados = empleadoDAO.obtenerEmpleadosPorCurso(curso);
            if (empleados != null && !empleados.isEmpty()) {
                List<EmpleadoResponse> empleadosDTO = empleados.stream()
                        .map(this::convertirAEmpleadoDTO)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(empleadosDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private EmpleadoResponse convertirAEmpleadoDTO(Empleado empleado) {
        return EmpleadoResponse.builder()
                .idEmpleado(empleado.getIdEmpleado())
                .nombreEmpleado(empleado.getNombreEmpleado())
                .apellidosEmpleado(empleado.getApellidosEmpleado())
                .emailEmpleado(empleado.getEmailEmpleado())
                .rol(empleado.getRol())
                .build();
    }

}
