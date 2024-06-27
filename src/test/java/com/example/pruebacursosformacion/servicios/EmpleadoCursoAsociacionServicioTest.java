package com.example.pruebacursosformacion.servicios;

import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.entidades.EmpleadoCursoAsociacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional
public class EmpleadoCursoAsociacionServicioTest {

    @Autowired
    private EmpleadoCursoAsociacionServicio empleadoCursoAsociacionServicio;
    @Autowired
    private EmpleadoDAO empleadoDAO;
    @Autowired
    private CursoDAO cursoDAO;


    @Test
    public void testInsertarActualizarEmpleadoCurso() {
        // Crear una instancia de EmpleadoCurso
        Empleado empleado = new Empleado();
        empleado.setNombreEmpleado("Empleado Prueba");

        Curso curso = new Curso();
        curso.setNombreCurso("Curso Prueba");

        EmpleadoCursoAsociacion empleadoCursoAsociacion = EmpleadoCursoAsociacion.builder()
                .empleado(empleado)
                .curso(curso)
                .calificacion(4)
                .titulo("Título Curso Prueba")
                .estado(true)
                .build();

        // Insertar o actualizar el empleadoCurso
        boolean resultado = empleadoCursoAsociacionServicio.insertarActualizarEmpleadoCurso(empleadoCursoAsociacion);

        // Verificar que se haya guardado correctamente
        Assertions.assertTrue(resultado);
        Assertions.assertNotNull(empleadoCursoAsociacion.getId());
    }

    @Test
    public void testObtenerEmpleadoCursoPorId() {
        // Crear un nuevo empleado y guardarlo en la base de datos
        Empleado empleado = new Empleado();
        empleado.setNombreEmpleado("Empleado Prueba");
        empleado.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado);

        // Crear un nuevo curso y guardarlo en la base de datos
        Curso curso = new Curso();
        curso.setNombreCurso("Curso Prueba");
        cursoDAO.insertarActualizarCurso(curso);

        // Asociar el curso al empleado usando el servicio
        empleadoCursoAsociacionServicio.asociarCurso(empleado.getIdEmpleado(), curso, 5, "Título Curso Prueba", true, new Date(), new Date());

        // Obtener el EmpleadoCurso recién creado
        List<EmpleadoCursoAsociacion> todosLosEmpleadoCursoAsociacion = empleadoCursoAsociacionServicio.obtenerTodosLosEmpleadoCurso();
        EmpleadoCursoAsociacion empleadoCursoAsociacionCreado = todosLosEmpleadoCursoAsociacion.get(todosLosEmpleadoCursoAsociacion.size() - 1); // Obtiene el último elemento agregado

        // Obtener el ID de la asociación creada
        long idEmpleadoCursoCreado = empleadoCursoAsociacionCreado.getId();

        // Obtener la asociación por su ID utilizando el método del servicio
        EmpleadoCursoAsociacion empleadoCursoAsociacionObtenido = empleadoCursoAsociacionServicio.obtenerEmpleadoCursoPorId(idEmpleadoCursoCreado);

        // Verificar que la asociación obtenida no sea nula
        Assertions.assertNotNull(empleadoCursoAsociacionObtenido);

        // Verificar que la asociación obtenida tenga el mismo ID que el buscado
        Assertions.assertEquals(idEmpleadoCursoCreado, empleadoCursoAsociacionObtenido.getId());
    }

    @Test
    public void testAsociarCurso() {
        // Crear un nuevo empleado y curso
        Empleado empleado = new Empleado();
        empleado.setNombreEmpleado("Empleado Prueba");
        empleado.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado);

        Curso curso = new Curso();
        curso.setNombreCurso("Curso Prueba");
        cursoDAO.insertarActualizarCurso(curso);

        // Asociar el curso al empleado usando el servicio
        empleadoCursoAsociacionServicio.asociarCurso(empleado.getIdEmpleado(), curso, 5, "Título Curso Prueba", true, new Date(), new Date());

        // Verificar que la asociación se haya guardado correctamente en la base de datos
        // Obtener el EmpleadoCurso recién creado
        List<EmpleadoCursoAsociacion> todosLosEmpleadoCursoAsociacion = empleadoCursoAsociacionServicio.obtenerTodosLosEmpleadoCurso();
        EmpleadoCursoAsociacion empleadoCursoAsociacion = todosLosEmpleadoCursoAsociacion.get(todosLosEmpleadoCursoAsociacion.size() - 1);

        Assertions.assertNotNull(empleadoCursoAsociacion);
        Assertions.assertEquals(empleado.getIdEmpleado(), empleadoCursoAsociacion.getEmpleado().getIdEmpleado());
        Assertions.assertEquals(curso.getIdCurso(), empleadoCursoAsociacion.getCurso().getIdCurso());
        Assertions.assertEquals(5, empleadoCursoAsociacion.getCalificacion());
        Assertions.assertEquals("Título Curso Prueba", empleadoCursoAsociacion.getTitulo());
        Assertions.assertTrue(empleadoCursoAsociacion.isEstado());
    }

    @Test
    public void testAsociarCursoSinEstadoYFechaFin() {
        // Crear un nuevo empleado y curso
        Empleado empleado = new Empleado();
        empleado.setNombreEmpleado("Empleado Prueba Sin Estado y Fecha Fin");
        empleado.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado);

        Curso curso = new Curso();
        curso.setNombreCurso("Curso Prueba Sin Estado y Fecha Fin");
        cursoDAO.insertarActualizarCurso(curso);

        // Asociar el curso al empleado usando el nuevo método del servicio
        Date fechaInicio = new Date();
        empleadoCursoAsociacionServicio.asociarCurso(empleado, curso, 4, "Título Curso Prueba Sin Estado y Fecha Fin",false, fechaInicio);

        // Verificar que la asociación se haya guardado correctamente en la base de datos
        List<EmpleadoCursoAsociacion> todosLosEmpleadoCursoAsociacion = empleadoCursoAsociacionServicio.obtenerTodosLosEmpleadoCurso();
        EmpleadoCursoAsociacion empleadoCursoAsociacion = todosLosEmpleadoCursoAsociacion.get(todosLosEmpleadoCursoAsociacion.size() - 1);

        Assertions.assertNotNull(empleadoCursoAsociacion);
        Assertions.assertEquals(empleado, empleadoCursoAsociacion.getEmpleado());
        Assertions.assertEquals(curso, empleadoCursoAsociacion.getCurso());
        Assertions.assertEquals(4, empleadoCursoAsociacion.getCalificacion());
        Assertions.assertEquals("Título Curso Prueba Sin Estado y Fecha Fin", empleadoCursoAsociacion.getTitulo());
        Assertions.assertEquals(fechaInicio, empleadoCursoAsociacion.getFechaInicio());
        // Verificar valores predeterminados
        Assertions.assertFalse(empleadoCursoAsociacion.isEstado()); // Si el estado predeterminado es false
        Assertions.assertNull(empleadoCursoAsociacion.getFechaFin()); // Si la fecha fin predeterminada es null
    }

    @Test
    public void testObtenerTodosLosEmpleadoCurso() {
        // Crear algunos datos de prueba
        Empleado empleado = new Empleado();
        empleado.setNombreEmpleado("Empleado Prueba");
        empleado.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado);

        Curso curso = new Curso();
        curso.setNombreCurso("Curso Prueba");
        cursoDAO.insertarActualizarCurso(curso);

        // Asociar el curso al empleado
        empleadoCursoAsociacionServicio.asociarCurso(empleado.getIdEmpleado(), curso, 5, "Título Curso Prueba", true, new Date(), new Date());

        // Obtener todos los EmpleadoCurso utilizando el servicio
        List<EmpleadoCursoAsociacion> empleadoCursoAsociacionList = empleadoCursoAsociacionServicio.obtenerTodosLosEmpleadoCurso();

        // Verificar que la lista no sea nula y contenga al menos un elemento
        Assertions.assertNotNull(empleadoCursoAsociacionList);
        Assertions.assertFalse(empleadoCursoAsociacionList.isEmpty());

        // Verificar que la lista contenga el EmpleadoCurso creado
        boolean encontrado = false;
        for (EmpleadoCursoAsociacion empleadoCursoAsociacion : empleadoCursoAsociacionList) {
            if (empleadoCursoAsociacion.getEmpleado().equals(empleado) && empleadoCursoAsociacion.getCurso().equals(curso)) {
                encontrado = true;
                break;
            }
        }
        Assertions.assertTrue(encontrado, "No se encontró el EmpleadoCurso asociado al empleado y curso");
    }


    @Test
    public void testObtenerCalificacionesPorCurso() {
        // Crear un curso y algunos empleados
        Curso curso = new Curso();
        curso.setNombreCurso("Curso de Prueba");
        cursoDAO.insertarActualizarCurso(curso);

        Empleado empleado1 = new Empleado();
        empleado1.setNombreEmpleado("Empleado 1");
        empleado1.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombreEmpleado("Empleado 2");
        empleado2.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado2);

        // Asociar calificaciones al curso para los empleados
        empleadoCursoAsociacionServicio.asociarCurso(empleado1.getIdEmpleado(), curso, 4, "Título Calificación 1", true, new Date(), new Date());
        empleadoCursoAsociacionServicio.asociarCurso(empleado2.getIdEmpleado(), curso, 3, "Título Calificación 2", true, new Date(), new Date());

        // Obtener las calificaciones por curso
        List<EmpleadoCursoAsociacion> calificacionesPorCurso = empleadoCursoAsociacionServicio.obtenerCalificacionesPorCurso(curso);

        // Verificar que la lista no sea nula y contenga las calificaciones asociadas al curso
        Assertions.assertNotNull(calificacionesPorCurso);
        Assertions.assertEquals(2, calificacionesPorCurso.size());

        // Verificar que las calificaciones pertenecen al curso
        for (EmpleadoCursoAsociacion empleadoCursoAsociacion : calificacionesPorCurso) {
            Assertions.assertEquals(curso, empleadoCursoAsociacion.getCurso());
        }

    }

    @Test
    public void testCalcularCalificacionFinalCurso() {
        // Crear un curso de prueba
        Curso curso = new Curso();
        curso.setNombreCurso("Curso de Prueba");
        cursoDAO.insertarActualizarCurso(curso);

        // Crear algunas calificaciones de ejemplo
        Empleado empleado1 = new Empleado();
        empleado1.setNombreEmpleado("Empleado 1");
        empleado1.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado1);
        empleadoCursoAsociacionServicio.asociarCurso(empleado1.getIdEmpleado(), curso, 5, "Título Calificación 1", true, new Date(), new Date());

        Empleado empleado2 = new Empleado();
        empleado2.setNombreEmpleado("Empleado 2");
        empleado2.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado2);
        empleadoCursoAsociacionServicio.asociarCurso(empleado2.getIdEmpleado(), curso, 3, "Título Calificación 2", true, new Date(), new Date());

        // Calcular la calificación final del curso
        double calificacionFinal = empleadoCursoAsociacionServicio.calcularCalificacionFinalCurso(curso);

        // Verificar que el promedio se calcule correctamente
        Assertions.assertEquals(4.0, calificacionFinal);
    }

    @Test
    public void testActualizarCalificacionFinal() {
        // Crear un curso de prueba
        Curso curso = new Curso();
        curso.setNombreCurso("Curso de Prueba");
        cursoDAO.insertarActualizarCurso(curso);

        // Crear algunas calificaciones de ejemplo
        Empleado empleado1 = new Empleado();
        empleado1.setNombreEmpleado("Empleado 1");
        empleado1.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado1);
        empleadoCursoAsociacionServicio.asociarCurso(empleado1.getIdEmpleado(), curso, 5, "Título Calificación 1", true, new Date(), new Date());

        Empleado empleado2 = new Empleado();
        empleado2.setNombreEmpleado("Empleado 2");
        empleado2.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado2);
        empleadoCursoAsociacionServicio.asociarCurso(empleado2.getIdEmpleado(), curso, 3, "Título Calificación 2", true, new Date(), new Date());

        // Actualizar la calificación final del curso
        cursoDAO.insertarActualizarCurso(curso);

        // Obtener el curso actualizado
        Curso cursoActualizado = cursoDAO.obtenerCursoPorId(curso.getIdCurso());

        // Verificar que la calificación final se haya actualizado correctamente
        Assertions.assertNotNull(cursoActualizado);
        Assertions.assertEquals(4.0, cursoActualizado.getClasificacionFinal());
    }


}
