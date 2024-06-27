package com.example.pruebacursosformacion.servicios;

import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.entidades.Tipo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional

public class CursoDAOTest {

    @Autowired
    private CursoDAO cursoDAO;
    @Autowired
    private EmpleadoDAO empleadoDAO;
    @Autowired
    private EmpleadoCursoAsociacionServicio empleadoCursoAsociacionServicio;

    @Test
    public void testInsertarActualizarCursos(){
        Curso curso = Curso.builder()
                .nombreCurso("Curso Prueba")
                .proveedor("Prueba")
                .precio(200.0)
                .tipoCurso(Tipo.BACKEND)
                .clasificacionFinal(4.2)
                .urlCurso("https://ejemplo.com/curso")
                .build();

        // Insertar o actualizar el curso
        boolean resultado = cursoDAO.insertarActualizarCurso(curso);

        // Verificar que la operación se haya realizado correctamente
        Assertions.assertTrue(resultado);

        // Obtener el curso recién insertado o actualizado por su nombre
        Curso cursoGuardado = cursoDAO.obtenerCursoPorNombre("Curso Prueba");

        // Verificar que el curso guardado no sea nulo
        Assertions.assertNotNull(cursoGuardado);

        // Verificar que el nombre del curso guardado sea el mismo que el nombre del curso de prueba
        Assertions.assertEquals("Curso Prueba", cursoGuardado.getNombreCurso());

    }

    @Test
    public void testObtenerTodosLosCursos() {
        // Obtener todos los cursos
        List<Curso> cursos = cursoDAO.obtenerTodosLosCursos();

        // Verificar que la lista de cursos no sea nula
        Assertions.assertNotNull(cursos);

        // Verificar que la lista de cursos no esté vacía
        Assertions.assertFalse(cursos.isEmpty());

    }

    @Test
    public void testObtenerCursoPorId() {
        // ID de curso existente en la base de datos
        long idCursoExistente = 1;

        // Obtener el curso por ID
        Curso curso = cursoDAO.obtenerCursoPorId(idCursoExistente);

        // Verificar que el curso no sea nulo
        Assertions.assertNotNull(curso);

        // Verificar que el ID del curso obtenido sea igual al ID esperado
        Assertions.assertEquals(idCursoExistente, curso.getIdCurso());
    }

    @Test
    public void testObtenerCursoPorNombre() {
        // Nombre de curso existente en la base de datos
        String nombreCursoExistente = "Curso Angular";

        // Obtener el curso por nombre
        Curso curso = cursoDAO.obtenerCursoPorNombre(nombreCursoExistente);

        // Verificar que el curso no sea nulo
        Assertions.assertNotNull(curso);

        // Verificar que el nombre del curso obtenido sea igual al nombre esperado
        Assertions.assertEquals(nombreCursoExistente, curso.getNombreCurso());
    }

    @Test
    public void testEliminarCursoPorId() {
        // Crear un nuevo curso para ser eliminado
        Curso curso = new Curso();
        curso.setNombreCurso("Curso a eliminar");
        cursoDAO.insertarActualizarCurso(curso);

        // Obtener el ID del curso recién creado
        long idCurso = curso.getIdCurso();

        // Eliminar el curso por su ID
        boolean eliminado = cursoDAO.eliminarCursoPorId(idCurso);

        // Verificar que el curso haya sido eliminado correctamente
        Assertions.assertTrue(eliminado);

        // Intentar obtener el curso eliminado por su ID
        Assertions.assertThrows(JpaObjectRetrievalFailureException.class, () -> {
            cursoDAO.obtenerCursoPorId(idCurso);
        });
    }

    @Test
    public void testEliminarCurso(){
        // Crear un nuevo curso para ser eliminado
        Curso curso = new Curso();
        curso.setNombreCurso("Curso a eliminar");
        cursoDAO.insertarActualizarCurso(curso);

        // Obtener el ID del curso recién creado
        long idCurso = curso.getIdCurso();

        // Eliminar el curso por entidad
        boolean eliminado = cursoDAO.eliminarCurso(curso);

        // Intentar obtener el curso eliminado
        Assertions.assertThrows(JpaObjectRetrievalFailureException.class, () -> {
            cursoDAO.obtenerCursoPorId(idCurso);
        });

    }

    @Test
    public void testObtenerCursosPorEmpleado() {
        // Crear un nuevo empleado
        Empleado empleado = new Empleado();
        empleado.setNombreEmpleado("Empleado de prueba");
        empleado.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado);

        // Crear algunos cursos
        Curso curso1 = new Curso();
        curso1.setNombreCurso("Curso 1");
        cursoDAO.insertarActualizarCurso(curso1);

        Curso curso2 = new Curso();
        curso2.setNombreCurso("Curso 2");
        cursoDAO.insertarActualizarCurso(curso2);

        // Asociar los cursos al empleado
        empleadoCursoAsociacionServicio.asociarCurso(empleado.getIdEmpleado(), curso1, 5, "TituloCurso1.pdf", true, new Date(), new Date());
        empleadoCursoAsociacionServicio.asociarCurso(empleado.getIdEmpleado(), curso2, 3, "TituloCurso2.pdf", true, new Date(), new Date());

        // Obtener los cursos por empleado
        List<Curso> cursosPorEmpleado = cursoDAO.obtenerCursosPorEmpleado(empleado);

        // Verificar que la lista no esté vacía
        Assertions.assertFalse(cursosPorEmpleado.isEmpty());

        // Verificar que la lista contenga los cursos esperados
        Assertions.assertTrue(cursosPorEmpleado.contains(curso1));
        Assertions.assertTrue(cursosPorEmpleado.contains(curso2));

    }

    @Test
    public void testObtenerCursosOrdenadosPorCalificacionFinal() {
        // Crear algunos cursos con diferentes calificaciones finales
        Curso curso1 = Curso.builder().nombreCurso("Curso 1").clasificacionFinal(4.5).build();
        Curso curso2 = Curso.builder().nombreCurso("Curso 2").clasificacionFinal(3.8).build();
        Curso curso3 = Curso.builder().nombreCurso("Curso 3").clasificacionFinal(5.0).build();

        // Insertar o actualizar los cursos en la base de datos
        cursoDAO.insertarActualizarCurso(curso1);
        cursoDAO.insertarActualizarCurso(curso2);
        cursoDAO.insertarActualizarCurso(curso3);

        // Obtener la lista de cursos ordenados por calificación final
        List<Curso> cursosOrdenados = cursoDAO.obtenerCursosOrdenadosPorCalificacionFinal();

        // Verificar que la lista no sea nula
        Assertions.assertNotNull(cursosOrdenados);

        // Verificar que cada calificación final sea mayor o igual que la siguiente
        for (int i = 0; i < cursosOrdenados.size() - 1; i++) {
            double calificacionActual = cursosOrdenados.get(i).getClasificacionFinal();
            double calificacionSiguiente = cursosOrdenados.get(i + 1).getClasificacionFinal();
            Assertions.assertTrue(calificacionActual >= calificacionSiguiente);
        }

    }

    @Test
    public void testObtenerPorcentajeEmpleadosQueRealizanCurso() {
        // Crear un curso de prueba
        Curso curso = Curso.builder()
                .nombreCurso("Curso Prueba")
                .proveedor("Proveedor Prueba")
                .precio(100.0)
                .build();

        // Insertar el curso en la base de datos
        cursoDAO.insertarActualizarCurso(curso);

        // Crear algunos empleados
        Empleado empleado1 = new Empleado();
        empleado1.setNombreEmpleado("Empleado 1");
        empleado1.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombreEmpleado("Empleado 2");
        empleado2.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado2);

        Empleado empleado3 = new Empleado();
        empleado3.setNombreEmpleado("Empleado 3");
        empleado3.setPassword("1234");
        empleadoDAO.insertarActualizaEmlpleado(empleado3);

        // Asociar algunos empleados al curso
        empleadoCursoAsociacionServicio.asociarCurso(empleado1.getIdEmpleado(), curso, 5, "Titulo Curso Prueba", true, new Date(), new Date());
        empleadoCursoAsociacionServicio.asociarCurso(empleado2.getIdEmpleado(), curso, 4, "Titulo Curso Prueba", true, new Date(), new Date());

        // Calcular el porcentaje de empleados que realizaron el curso
        double porcentaje = cursoDAO.obtenerPorcentajeEmpleadosQueRealizanCurso(curso);

        long totalEmpleados = empleadoDAO.obtenerTodosLosEmpleados().size();

        // Obtener el número de empleados que han realizado el curso dado
        long empleadosQueRealizaronCurso = 2;

        // Calcular el porcentaje de empleados que realizaron el curso
        double porcentajeEsperado = (double) empleadosQueRealizaronCurso / totalEmpleados * 100;

        // Verificar que el porcentaje calculado sea correcto
        Assertions.assertEquals(porcentajeEsperado, porcentaje, 0.01);
        // delta: tolerancia o margen de error para la comparación de valores de punto flotante
    }

    @Test
    public void testObtenerCursosPorTipo() {
        // Crear algunos cursos de prueba con un tipo específico
        Curso curso1 = Curso.builder().nombreCurso("Curso 1 p").tipoCurso(Tipo.BACKEND).build();
        Curso curso2 = Curso.builder().nombreCurso("Curso 2 p").tipoCurso(Tipo.FRONTEND).build();
        Curso curso3 = Curso.builder().nombreCurso("Curso 3 p").tipoCurso(Tipo.BACKEND).build();

        // Insertar o actualizar los cursos en la base de datos
        cursoDAO.insertarActualizarCurso(curso1);
        cursoDAO.insertarActualizarCurso(curso2);
        cursoDAO.insertarActualizarCurso(curso3);

        // Obtener cursos por tipo
        List<Curso> cursosBackend = cursoDAO.obtenerCursosPorTipo(Tipo.BACKEND);
        List<Curso> cursosFrontend = cursoDAO.obtenerCursosPorTipo(Tipo.FRONTEND);

        // Verificar que las listas no sean nulas
        Assertions.assertNotNull(cursosBackend);
        Assertions.assertNotNull(cursosFrontend);

        // Verificar que las listas contengan los cursos esperados
        Assertions.assertFalse(cursosBackend.isEmpty());
        Assertions.assertFalse(cursosFrontend.isEmpty());

        // Verificar que los cursos tienen el tipo esperado
        for (Curso curso : cursosBackend) {
            Assertions.assertEquals(Tipo.BACKEND, curso.getTipoCurso());
        }

        for (Curso curso : cursosFrontend) {
            Assertions.assertEquals(Tipo.FRONTEND, curso.getTipoCurso());
        }

    }

    // sp
    @Test
    public void testObtenerCursosPorProveedor() {
        // Crear algunos cursos de prueba con diferentes proveedores
        Curso curso1 = Curso.builder().nombreCurso("Curso 1").proveedor("Proveedor A").build();
        Curso curso2 = Curso.builder().nombreCurso("Curso 2").proveedor("Proveedor B").build();
        Curso curso3 = Curso.builder().nombreCurso("Curso 3").proveedor("Proveedor A").build();

        // Insertar o actualizar los cursos en la base de datos
        cursoDAO.insertarActualizarCurso(curso1);
        cursoDAO.insertarActualizarCurso(curso2);
        cursoDAO.insertarActualizarCurso(curso3);

        // Obtener cursos por proveedor
        List<Curso> cursosProveedorA = cursoDAO.obtenerCursosPorProveedor("Proveedor A");

        // Verificar que la lista no sea nula
        Assertions.assertNotNull(cursosProveedorA);

        // Verificar que la lista contenga los cursos esperados
        Assertions.assertFalse(cursosProveedorA.isEmpty());
        Assertions.assertEquals(2, cursosProveedorA.size());
        Assertions.assertTrue(cursosProveedorA.contains(curso1));
        Assertions.assertTrue(cursosProveedorA.contains(curso3));
    }

    //sp
    @Test
    public void testObtenerCursosPorNombreYProveedor() {
        // Crear algunos cursos de prueba con diferentes nombres y proveedores
        Curso curso1 = Curso.builder().nombreCurso("Curso 1").proveedor("Proveedor A").build();
        Curso curso2 = Curso.builder().nombreCurso("Curso 3").proveedor("Proveedor B").build();
        Curso curso3 = Curso.builder().nombreCurso("Curso 2").proveedor("Proveedor A").build();

        // Insertar o actualizar los cursos en la base de datos
        cursoDAO.insertarActualizarCurso(curso1);
        cursoDAO.insertarActualizarCurso(curso2);
        cursoDAO.insertarActualizarCurso(curso3);

        // Obtener cursos por nombre y proveedor
        List<Curso> cursosNombreYProveedor = cursoDAO.obtenerCursosPorNombreYProveedor("Curso 1", "Proveedor A");

        // Verificar que la lista no sea nula
        Assertions.assertNotNull(cursosNombreYProveedor);

        // Verificar que la lista contenga el curso esperado
        Assertions.assertFalse(cursosNombreYProveedor.isEmpty());
        Assertions.assertEquals(1, cursosNombreYProveedor.size());
        Assertions.assertTrue(cursosNombreYProveedor.contains(curso1));
    }

}


