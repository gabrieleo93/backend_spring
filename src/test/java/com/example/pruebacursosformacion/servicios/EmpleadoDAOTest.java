package com.example.pruebacursosformacion.servicios;

import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional

public class EmpleadoDAOTest {

    @Autowired
    private EmpleadoDAO empleadoDAO;
    @Autowired
    private CursoDAO cursoDAO;


    @Test
    public void testInsertarActualizarEmpleado(){

        Empleado empleadoPrueba = Empleado.builder()
                .nombreEmpleado("Nombre")
                .apellidosEmpleado("Apellidos")
                .emailEmpleado("email@example.com")
                .password("password123")
                .rol("empleado")
                .build();

        boolean resultado = empleadoDAO.insertarActualizaEmlpleado(empleadoPrueba);

        Assertions.assertTrue(resultado);

        Empleado empleadoGuardado = empleadoDAO.obtenerEmpleadoPorNombre("Nombre");
        Assertions.assertTrue(empleadoGuardado != null);

    }

    @Test
    public void testObtenerTodosLosEmpleados(){

        List<Empleado> listaEmpleados = empleadoDAO.obtenerTodosLosEmpleados();

        // Verifica que la lista no es nula y contenga al menos un empleado
        Assertions.assertNotNull(listaEmpleados);
        Assertions.assertFalse(listaEmpleados.isEmpty());

    }

    @Test void testObtenerEmpleadoPorId(){

        long idEmpleadoExistente = 1L;
        Empleado empleado = empleadoDAO.obtenerEmpleadoPorId(idEmpleadoExistente);

        // Verifica que el empleado no sea nulo
        Assertions.assertNotNull(empleado);

        // Verifica que el identificador es el mismo
        Assertions.assertEquals(idEmpleadoExistente, empleado.getIdEmpleado());
    }

    @Test
    public void testObtenerEmpleadoPorNombre(){

        String nombreEmpleadoExistente = "Juan";
        Empleado empleado = empleadoDAO.obtenerEmpleadoPorNombre(nombreEmpleadoExistente);

        //Verificar que el empleado no es nulo
        Assertions.assertNotNull(empleado);

        // Verificar que el nombre coincida
        Assertions.assertEquals(nombreEmpleadoExistente, empleado.getNombreEmpleado());
    }

    @Test
    public void testObtenerEmpleadosPorCurso(){

        Curso curso = cursoDAO.obtenerCursoPorId(1L);
        Assertions.assertNotNull(curso);

        List<Empleado> empleadosList = empleadoDAO.obtenerEmpleadosPorCurso(curso);
        // Verificar que la lista no sea nula
        Assertions.assertNotNull(empleadosList);
        // Verificar que la lista no este vacia
        Assertions.assertFalse(empleadosList.isEmpty());

    }

    @Test
    public void  obtenerEmpleadoPorEmail(){
        Empleado empleadoPrueba = Empleado.builder()
                .nombreEmpleado("Nombre")
                .apellidosEmpleado("Apellidos")
                .emailEmpleado("email@example.com")
                .password("password123")
                .rol("ADMIN")
                .build();

        boolean resultado = empleadoDAO.insertarActualizaEmlpleado(empleadoPrueba);

        Empleado empleadoGuardado = empleadoDAO.obtenerEmpleadoPorEmail("email@example.com");
        Assertions.assertTrue(empleadoGuardado != null);
        Assertions.assertEquals("Nombre", empleadoGuardado.getNombreEmpleado());

    }

}
