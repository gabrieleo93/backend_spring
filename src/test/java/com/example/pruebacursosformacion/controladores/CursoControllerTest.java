package com.example.pruebacursosformacion.controladores;

import com.example.pruebacursosformacion.configuracion.JwtUtil;
import com.example.pruebacursosformacion.configuracion.SecurityConfig;
import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.entidades.Tipo;
import com.example.pruebacursosformacion.servicios.CursoDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(controllers = CursoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(JwtUtil.class)
//@Import(JacksonConfig.class) // Para los Enum
public class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoDAO cursoDAO;

    @Autowired
    private ObjectMapper objectMapper;

    private Curso curso;


    private List<Curso> cursos;

    private List<Curso> cursosOrdenados;

    @InjectMocks
    private CursoController cursoController;

    @BeforeEach
    public  void init(){

         curso = Curso.builder()
                .nombreCurso("Curso BBDD")
                .proveedor("Prueba")
                .precio(200.0)
                .tipoCurso(Tipo.BBDD)
                .clasificacionFinal(4.2)
                .urlCurso("https://ejemplo.com/curso")
                .build();


        cursos = Arrays.asList(
                Curso.builder()
                        .nombreCurso("Curso 1")
                        .clasificacionFinal(4.5)
                        .build(),
                Curso.builder()
                        .nombreCurso("Curso 2")
                        .clasificacionFinal(4.0)
                        .build(),
                Curso.builder()
                        .nombreCurso("Curso 3")
                        .clasificacionFinal(5.0)
                        .build()
        );

    }


    @Test
    public void testInsertarCurso() throws Exception{
        // Simular comportamiento del DAO
        given(cursoDAO.insertarActualizarCurso(any(Curso.class))).willReturn(true);

        // Convertir objeto curso a JSON
        String cursoJson = objectMapper.writeValueAsString(curso);
        //String cursoJson = "{\"nombreCurso\":\"Curso Prueba\",\"proveedor\":\"Prueba\",\"fechaInicio\":\"2024-05-10\",\"fechaFin\":\"2024-05-11\",\"precio\":200.0,\"tipoCurso\":\"BACKEND\",\"clasificacionFinal\":4.2,\"urlCurso\":\"https://ejemplo.com/curso\"}";

        // Realizar la petición y verificar el resultado
        mockMvc.perform(post("/api/cursos/insertar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cursoJson))
                        .andExpect(status().isOk())
                        .andExpect(content().string("true"));

        // Verificar que el método insertarActualizarCurso se llamó una vez con el curso correcto
        verify(cursoDAO, times(1)).insertarActualizarCurso(any(Curso.class));

    }

    @Test
    public void testInsertarCurso2() throws Exception {
       // Curso curso = new Curso(/* Set up your Curso object here */);
        String cursoJson = objectMapper.writeValueAsString(curso);

        when(cursoDAO.insertarActualizarCurso(any(Curso.class))).thenReturn(true);

        mockMvc = MockMvcBuilders.standaloneSetup(cursoController).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cursos/insertar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cursoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }


    @Test
    public void testActualizarCurso() throws Exception {
        long id = 1L;

        // Simular comportamiento del DAO
        given(cursoDAO.insertarActualizarCurso(any(Curso.class))).willReturn(true);

        // Convertir objeto curso a JSON
        String cursoJson = objectMapper.writeValueAsString(curso);

        // Realizar la petición y verificar el resultado
        mockMvc.perform(put("/api/cursos/actualizar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cursoJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Verificar que el método insertarActualizarCurso se llamó una vez con el curso correcto
        verify(cursoDAO, times(1)).insertarActualizarCurso(any(Curso.class));
    }

    @Test
    public void testObtenerTodosLosCursos() throws Exception {
        // Crear una lista de cursos para simular el comportamiento del DAO
        List<Curso> cursos = Arrays.asList(curso);

        // Simular comportamiento del DAO
        given(cursoDAO.obtenerTodosLosCursos()).willReturn(cursos);

        // Realizar la petición y verificar el resultado
        mockMvc.perform(get("/api/cursos/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].nombreCurso").value("Curso BBDD"))
                        .andExpect(jsonPath("$[0].proveedor").value("Prueba"))
                        .andExpect(jsonPath("$[0].precio").value(200.0))
                        .andExpect(jsonPath("$[0].tipoCurso").value(Tipo.BBDD.name()));

        // Verificar que el método obtenerTodosLosCursos se llamó una vez
        verify(cursoDAO, times(1)).obtenerTodosLosCursos();
    }

    @Test
    public void testEliminarCursoPorId() throws Exception {
        long id = 1L;

        // Simular comportamiento del DAO
        given(cursoDAO.eliminarCursoPorId(id)).willReturn(true);

        // Realizar la petición y verificar el resultado
        mockMvc.perform(delete("/api/cursos/eliminar/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Verificar que el método eliminarCursoPorId se llamó una vez con el ID correcto
        verify(cursoDAO, times(1)).eliminarCursoPorId(id);
    }

    @Test
    public void testEliminarCurso() throws Exception {
        // Simular comportamiento del DAO
        given(cursoDAO.eliminarCurso(any(Curso.class))).willReturn(true);

        // Convertir objeto curso a JSON
        String cursoJson = objectMapper.writeValueAsString(curso);

        // Realizar la petición y verificar el resultado
        mockMvc.perform(delete("/api/cursos/eliminar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cursoJson))
                        .andExpect(status().isOk())
                        .andExpect(content().string("true"));

        // Verificar que el método eliminarCurso se llamó una vez con el curso correcto
        verify(cursoDAO, times(1)).eliminarCurso(any(Curso.class));
    }

    @Test
    public void testObtenerCursosPorEmpleado() throws Exception {
        // Crear un empleado para simular el comportamiento del DAO
        Empleado empleado = new Empleado();
        //empleado.setIdEmpleado(1L);
        empleado.setNombreEmpleado("Pepe");
        empleado.setPassword("password");  // Proveer un valor de password para evitar problemas de deserialización

        // Crear una lista de cursos para simular el comportamiento del DAO
        List<Curso> cursos = Arrays.asList(
                Curso.builder()
                        .nombreCurso("Curso 1")
                        .build(),
                Curso.builder()
                        .nombreCurso("Curso 2")
                        .build()
        );

        // Simular comportamiento del DAO
        given(cursoDAO.obtenerCursosPorEmpleado(any(Empleado.class))).willReturn(cursos);

        // Convertir objeto empleado a JSON
        String empleadoJson = objectMapper.writeValueAsString(empleado);

        // Realizar la petición y verificar el resultado
        mockMvc.perform(post("/api/cursos/por-empleado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(empleadoJson))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(2))) // Verificar que se devuelvan 2 cursos
                        .andExpect(jsonPath("$[0].nombreCurso").value("Curso 1"))
                        .andExpect(jsonPath("$[1].nombreCurso").value("Curso 2"));

        // Verificar que el método obtenerCursosPorEmpleado se llamó una vez con el empleado correcto
        verify(cursoDAO, times(1)).obtenerCursosPorEmpleado(any(Empleado.class));
    }

    @Test
    public void testObtenerCursosOrdenadosPorCalificacionFinal() throws Exception {
        // Simular comportamiento del DAO
        given(cursoDAO.obtenerCursosOrdenadosPorCalificacionFinal()).willReturn(cursos);

        // Realizar la petición y verificar el resultado
        mockMvc.perform(get("/api/cursos/ordenados-por-calificacion")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(3))); // Verificar que se devuelvan 3 cursos

        // Verificar que el método obtenerCursosOrdenadosPorCalificacionFinal se llamó una vez
        verify(cursoDAO, times(1)).obtenerCursosOrdenadosPorCalificacionFinal();
    }

    @Test
    void testObtenerPorcentajeEmpleadosQueRealizanCurso() throws Exception {
        // Definir el comportamiento esperado del servicio
        given(cursoDAO.obtenerPorcentajeEmpleadosQueRealizanCurso(any(Curso.class))).willReturn(50.0);

        // Convertir el objeto Curso a JSON
        String cursoJson = objectMapper.writeValueAsString(curso);

        // Realizar la solicitud POST y verificar el resultado esperado
        mockMvc.perform(post("/api/cursos/porcentaje-empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cursoJson))
                .andExpect(status().isOk())
                .andExpect(content().string("50.0"));


    }

    @Test
    void testObtenerCursosPorTipo() throws Exception {
        // Crear una lista de cursos de ejemplo
        List<Curso> cursos = Arrays.asList(
                Curso.builder().nombreCurso("Curso 1").tipoCurso(Tipo.BACKEND).build(),
                Curso.builder().nombreCurso("Curso 2").tipoCurso(Tipo.BACKEND).build()
        );

        // Definir el comportamiento esperado del servicio
        given(cursoDAO.obtenerCursosPorTipo(any(Tipo.class))).willReturn(cursos);

        // Crear un objeto Tipo para enviar en la solicitud
        Tipo tipo = Tipo.BACKEND;

        // Convertir el objeto Tipo a JSON
        String tipoJson = objectMapper.writeValueAsString(tipo);

        // Realizar la solicitud POST y verificar el resultado esperado
        mockMvc.perform(post("/api/cursos/por-tipo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tipoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cursos)));
    }



}
