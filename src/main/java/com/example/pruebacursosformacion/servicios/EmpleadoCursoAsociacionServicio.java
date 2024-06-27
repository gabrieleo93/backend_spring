package com.example.pruebacursosformacion.servicios;

import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.entidades.EmpleadoCursoAsociacion;
import com.example.pruebacursosformacion.repositorio.EmpleadoCursoAsociacionRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class EmpleadoCursoAsociacionServicio {
    private static final Logger logger = LoggerFactory.getLogger(CursoDAO.class);
    @Autowired
    private MessageSource messageSource;
    @Autowired
    EmpleadoCursoAsociacionRepositorio empleadoCursoAsociacionRepositorio;
    @Autowired
    CursoDAO cursoDAO;
    @Autowired
    EmpleadoDAO empleadoDAO;

    public boolean insertarActualizarEmpleadoCurso(EmpleadoCursoAsociacion empleadoCursoAsociacion) {
        try {
            empleadoCursoAsociacionRepositorio.save(empleadoCursoAsociacion);
            logger.info(messageSource.getMessage("basedatos.empleado_curso.guardar", new Object[]{
                    empleadoCursoAsociacion.getEmpleado().getIdEmpleado(), empleadoCursoAsociacion.getCurso().getIdCurso()},
                    LocaleContextHolder.getLocale()));
            return true;
        } catch (Exception e) {
            logger.error(messageSource.getMessage("basedatos.empleado_curso.error.guardar", new Object[]{
                    empleadoCursoAsociacion.getEmpleado().getIdEmpleado(), empleadoCursoAsociacion.getCurso().getIdCurso()},
                    LocaleContextHolder.getLocale()), e);
            return false;
        }
    }

    public EmpleadoCursoAsociacion obtenerEmpleadoCursoPorId(long id){
        logger.trace(messageSource.getMessage("basedatos.empleado_curso.obtenerPorId", new Object[]{id}, LocaleContextHolder.getLocale()));
        return empleadoCursoAsociacionRepositorio.getReferenceById(id);
    }

    public void asociarCurso(long idEmpleado, Curso curso, int calificacion, String titulo, boolean estado, Date fInicio, Date fFin) {

        // Verifica si el curso y el empleado existen en la base de datos
        //Curso cursoGuardado = cursoDAO.obtenerCursoPorNombre(curso.getNombreCurso());
        Curso cursoGuardado = cursoDAO.obtenerCursoPorId(curso.getIdCurso());
        if (cursoGuardado == null) {
            logger.warn("Intento de hacer una asociación con un curso que no existe");
            throw new IllegalArgumentException(messageSource.getMessage("error.curso.noExiste", new Object[]{curso.getNombreCurso()}, Locale.getDefault()));
            // Locale.getDefault() idioma predeterminado de la aplicación.
        }

        //Empleado empleadoGuardado = empleadoDAO.obtenerEmpleadoPorNombre(empleado.getNombreEmpleado());
        Empleado empleadoGuardado = empleadoDAO.obtenerEmpleadoPorId(idEmpleado);
        if (empleadoGuardado == null) {
            logger.warn("Intento de hacer una asociación con un empleado que no existe");
            throw new IllegalArgumentException(messageSource.getMessage("error.empleado.noExiste", new Object[]{empleadoGuardado.getNombreEmpleado()}, Locale.getDefault()));
        }

        // Validar calificación utilizando las anotaciones @Min y @Max
        if (calificacion < 1 || calificacion > 5) {
            logger.warn("Intento de hacer una asociación con una calificación no valida");
            throw new IllegalArgumentException(messageSource.getMessage("calificacion.invalida", null, Locale.getDefault()));
        }

        // Crear una nueva instancia de EmpleadoCurso
        EmpleadoCursoAsociacion empleadoCursoAsociacion = EmpleadoCursoAsociacion.builder()
                .empleado(empleadoGuardado)
                .curso(cursoGuardado)
                .calificacion(calificacion)
                .titulo(titulo)
                .estado(estado)
                .fechaInicio(fInicio)
                .fechaFin(fFin)
                .build();

        // Guardar la asociación en la base de datos
        insertarActualizarEmpleadoCurso(empleadoCursoAsociacion);

        // Actualizar la calificación final del curso
        actualizarCalificacionFinal(cursoGuardado);


    }

    public void asociarCurso(Empleado empleado, Curso curso, int calificacion, String titulo,boolean estado, Date fInicio) {

        // Verifica si el curso y el empleado existen en la base de datos
        Curso cursoGuardado = cursoDAO.obtenerCursoPorId(curso.getIdCurso());
        if (cursoGuardado == null) {
            logger.warn("Intento de hacer una asociación con un curso que no existe");
            throw new IllegalArgumentException(messageSource.getMessage("error.curso.noExiste", new Object[]{curso.getNombreCurso()}, Locale.getDefault()));
            // Locale.getDefault() idioma predeterminado de la aplicación.
        }

        Empleado empleadoGuardado = empleadoDAO.obtenerEmpleadoPorId(empleado.getIdEmpleado());
        if (empleadoGuardado == null) {
            logger.warn("Intento de hacer una asociación con un empleado que no existe");
            throw new IllegalArgumentException(messageSource.getMessage("error.empleado.noExiste", new Object[]{empleado.getNombreEmpleado()}, Locale.getDefault()));
        }

        // Validar calificación utilizando las anotaciones @Min y @Max
        if (calificacion < 1 || calificacion > 5) {
            logger.warn("Intento de hacer una asociación con una calificación no valida");
            throw new IllegalArgumentException(messageSource.getMessage("calificacion.invalida", null, Locale.getDefault()));
        }

        // Crear una nueva instancia de EmpleadoCurso
        EmpleadoCursoAsociacion empleadoCursoAsociacion = EmpleadoCursoAsociacion.builder()
                .empleado(empleado)
                .curso(curso)
                .calificacion(calificacion)
                .titulo(titulo)
                .estado(estado)
                .fechaInicio(fInicio)
                .build();

        // Guardar la asociación en la base de datos
        insertarActualizarEmpleadoCurso(empleadoCursoAsociacion);

        // Actualizar la calificación final del curso
        actualizarCalificacionFinal(cursoGuardado);
    }

    public List<EmpleadoCursoAsociacion> obtenerTodosLosEmpleadoCurso(){
        logger.trace(messageSource.getMessage("basedatos.empleado_curso.obtenerTodos", null, LocaleContextHolder.getLocale()));
        return empleadoCursoAsociacionRepositorio.findAll();
    }

    public  List<EmpleadoCursoAsociacion> obtenerCalificacionesPorCurso(Curso curso){
        logger.trace(messageSource.getMessage("basedatos.empleado_curso.obtenerPorCurso", new Object[]{curso.getIdCurso()}, LocaleContextHolder.getLocale()));
        return empleadoCursoAsociacionRepositorio.findByCurso(curso);
    }

    public double calcularCalificacionFinalCurso(Curso curso){
        List<EmpleadoCursoAsociacion> calificaciones = obtenerCalificacionesPorCurso(curso);
        int totalCalificaciones = 0;

        // Sumar todas las calificaciones
        for (EmpleadoCursoAsociacion calificacion : calificaciones){
            totalCalificaciones += calificacion.getCalificacion();
        }

        // Calcular el promedio de las calificaciones
        double promedioCalificaciones =  totalCalificaciones / calificaciones.size();
        logger.trace(messageSource.getMessage("basedatos.curso.calcularCalificacionFinal", new Object[]{curso.getIdCurso()}, LocaleContextHolder.getLocale()));
        return promedioCalificaciones;
    }

    public void actualizarCalificacionFinal(Curso curso) {
        double calificacion = calcularCalificacionFinalCurso(curso);
        curso.setClasificacionFinal(calificacion);
        cursoDAO.insertarActualizarCurso(curso);
        logger.info(messageSource.getMessage("basedatos.curso.actualizarCalificacionFinal", new Object[]{curso.getIdCurso()}, LocaleContextHolder.getLocale()));
    }

}
