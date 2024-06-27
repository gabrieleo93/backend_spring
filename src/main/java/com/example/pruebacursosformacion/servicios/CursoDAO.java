package com.example.pruebacursosformacion.servicios;

import com.example.pruebacursosformacion.Excepciones.ResourceNotFoundException;
import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.entidades.Tipo;
import com.example.pruebacursosformacion.repositorio.CursoRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoDAO {
    private static final Logger logger = LoggerFactory.getLogger(CursoDAO.class);
    @Autowired
    private MessageSource messageSource;
    @Autowired
    CursoRepositorio repositorioCurso;
    @Autowired
    EmpleadoDAO empleadoDAO;

    public  boolean insertarActualizarCurso(Curso curso){
        try {
            repositorioCurso.save(curso);
            logger.info(messageSource.getMessage("basedatos.curso.guardar", new Object[]{curso.getIdCurso()}, LocaleContextHolder.getLocale()));
            return true;
        } catch (Exception e) {
            logger.error(messageSource.getMessage("basedatos.curso.error.guardar", new Object[]{e.getMessage()}, LocaleContextHolder.getLocale()));
            return false;
        }
    }
    public List<Curso> obtenerTodosLosCursos(){
        logger.trace(messageSource.getMessage("basedatos.curso.obtenerTodos", null, LocaleContextHolder.getLocale()));
        return repositorioCurso.findAll();
    }
    public Curso obtenerCursoPorId(long id){

        Curso curso = repositorioCurso.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("curso.no.encontrado", new Object[]{id}, LocaleContextHolder.getLocale())));

        logger.info(messageSource.getMessage("basedatos.curso.obtenerPorId", new Object[]{curso.getIdCurso()}, LocaleContextHolder.getLocale()));

        return curso;

    }
    public Curso obtenerCursoPorNombre(String nombre){
        logger.trace(messageSource.getMessage("basedatos.curso.obtenerPorNombre", new Object[]{nombre}, LocaleContextHolder.getLocale()));
        return repositorioCurso.findByNombreCurso(nombre).orElse(null); //Se puede realizar porque es Opcional
    }
    public boolean eliminarCursoPorId(long id){
        try {
            repositorioCurso.deleteById(id);
            logger.info(messageSource.getMessage("basedatos.curso.eliminar", new Object[]{id}, LocaleContextHolder.getLocale()));
            return true;
        } catch (Exception e) {
            logger.error(messageSource.getMessage("basedatos.curso.error.eliminar", new Object[]{id, e.getMessage()}, LocaleContextHolder.getLocale()));
            return false;
        }
    }

    public boolean eliminarCurso(Curso curso){
        try{
            repositorioCurso.delete(curso);
            logger.info(messageSource.getMessage("basedatos.curso.eliminar", new Object[]{curso.getIdCurso()}, LocaleContextHolder.getLocale()));
            return true;
        }catch (Exception e){
            logger.error(messageSource.getMessage("basedatos.curso.error.eliminar", new Object[]{curso.getIdCurso(), e.getMessage()}, LocaleContextHolder.getLocale()));
            return false;
        }

    }

    //Metodo que devuelve lista de cursos realizados por un empleado en concreto
    public List<Curso> obtenerCursosPorEmpleado(Empleado empleado) {
        logger.info(messageSource.getMessage("basedatos.curso.obtenerPorEmpleado", new Object[]{empleado.getIdEmpleado()}, LocaleContextHolder.getLocale()));
        return repositorioCurso.findCursosByEmpleado(empleado);
    }

    // Metodo que devuelve una lista ordenada de mayor a menor calificacion final de los cursos
    public List<Curso> obtenerCursosOrdenadosPorCalificacionFinal() {
        List<Curso> cursos = repositorioCurso.findAll();

        // Ordenar la lista de cursos por la calificación final de mayor a menor
        List<Curso> cursosOrdenados = cursos.stream()
                .sorted((c1, c2) -> Double.compare(c2.getClasificacionFinal(), c1.getClasificacionFinal()))
                .collect(Collectors.toList());

        logger.info(messageSource.getMessage("basedatos.curso.obtenerOrdenados", null, LocaleContextHolder.getLocale()));
        return cursosOrdenados;
    }

    // Método que devuelve el porcentaje de empleados que realizan un curso concreto
    public double obtenerPorcentajeEmpleadosQueRealizanCurso(Curso curso) {
        // Obtener el número total de empleados en la base de datos
        long totalEmpleados = empleadoDAO.obtenerTodosLosEmpleados().size();

        // Obtener el número de empleados que han realizado el curso dado
        long empleadosQueRealizaronCurso = repositorioCurso.contarEmpleadosQueRealizaronCurso(curso);


        // Calcular el porcentaje de empleados que realizaron el curso
        double porcentaje = (double) empleadosQueRealizaronCurso / totalEmpleados * 100;

        logger.info(messageSource.getMessage("basedatos.curso.obtenerPorcentaje", new Object[]{curso.getIdCurso()}, LocaleContextHolder.getLocale()));
        return porcentaje;
    }

    // Método para obtener cursos por tipo
    public List<Curso> obtenerCursosPorTipo(Tipo tipo) {
        logger.info(messageSource.getMessage("basedatos.curso.obtenerPorTipo", new Object[]{tipo}, LocaleContextHolder.getLocale()));
        return repositorioCurso.findByTipoCurso(tipo);
    }

    // Método para obtener cursos por proveedor
    public List<Curso> obtenerCursosPorProveedor(String proveedor) {
        logger.info(messageSource.getMessage("basedatos.curso.obtenerPorProveedor", new Object[]{proveedor}, LocaleContextHolder.getLocale()));
        return repositorioCurso.findByProveedor(proveedor);
    }

    // Método para obtener cursos por nombre y proveedor
    public List<Curso> obtenerCursosPorNombreYProveedor(String nombre, String proveedor) {
        logger.info(messageSource.getMessage("basedatos.curso.obtenerPorNombreYProveedor", new Object[]{nombre, proveedor}, LocaleContextHolder.getLocale()));
        return repositorioCurso.findByNombreCursoAndProveedor(nombre, proveedor);
    }

}
