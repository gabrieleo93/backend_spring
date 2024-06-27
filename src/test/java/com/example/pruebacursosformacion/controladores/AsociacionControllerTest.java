package com.example.pruebacursosformacion.controladores;
import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.servicios.EmpleadoCursoAsociacionServicio;
import com.example.pruebacursosformacion.solicitudes.AsociacionRequest;
import com.example.pruebacursosformacion.solicitudes.AsociacionSinFinRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AsociacionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AsociacionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmpleadoCursoAsociacionServicio asociacionServicio;
    @Autowired
    private ObjectMapper objectMapper;
    private Empleado empleado;
    private Curso curso;

    @BeforeEach
    public void init() {
        empleado = Empleado.builder()
                .nombreEmpleado("Empleado de Prueba")
                .password("password") // Proveer un valor de password para evitar problemas de deserialización
                .build();

        curso = Curso.builder()
                .nombreCurso("Curso de Prueba")
                .build();

    }


   /* @Test
    public void testAsociarCurso() throws Exception {
        // Datos de prueba
        int calificacion = 4;
        String titulo = "Título de Prueba";
        boolean estado = true;
        Date fechaInicio = new Date();
        Date fechaFin = new Date();

        // Crear el objeto de solicitud
        AsociacionRequest request = new AsociacionRequest();
        request.setIdEmpleado(empleado.getIdEmpleado());
        request.setCurso(curso);
        request.setCalificacion(calificacion);
        request.setTitulo(titulo);
        request.setEstado(estado);
        request.setFechaInicio(fechaInicio);
        request.setFechaFin(fechaFin);

        // Convertir objeto de solicitud a JSON
        String requestJson = objectMapper.writeValueAsString(request);

        // Realizar la solicitud POST y verificar la respuesta
        mockMvc.perform(post("/api/asociacion/asociar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Asociación creada exitosamente"));

        // Usar ArgumentCaptor para capturar los argumentos
        ArgumentCaptor<Empleado> empleadoCaptor = forClass(Empleado.class);
        ArgumentCaptor<Curso> cursoCaptor = forClass(Curso.class);
        ArgumentCaptor<Integer> calificacionCaptor = forClass(Integer.class);
        ArgumentCaptor<String> tituloCaptor = forClass(String.class);
        ArgumentCaptor<Boolean> estadoCaptor = forClass(Boolean.class);
        ArgumentCaptor<Date> fechaInicioCaptor = forClass(Date.class);
        ArgumentCaptor<Date> fechaFinCaptor = forClass(Date.class);

        verify(asociacionServicio, times(1)).asociarCurso(
                empleadoCaptor.capture(),
                cursoCaptor.capture(),
                calificacionCaptor.capture(),
                tituloCaptor.capture(),
                estadoCaptor.capture(),
                fechaInicioCaptor.capture(),
                fechaFinCaptor.capture()
        );

        // Verificar que los valores de los argumentos capturados son los esperados
        assertEquals(empleado.getIdEmpleado(), empleadoCaptor.getValue().getIdEmpleado());
        assertEquals(curso.getIdCurso(), cursoCaptor.getValue().getIdCurso());
        assertEquals(calificacion, calificacionCaptor.getValue().intValue());
        assertEquals(titulo, tituloCaptor.getValue());
        assertEquals(estado, estadoCaptor.getValue());
        assertEquals(fechaInicio, fechaInicioCaptor.getValue());
        assertEquals(fechaFin, fechaFinCaptor.getValue());


    }*/


   /* @Test
    public void testAsociarCursoSinFechaFin() throws Exception {
        // Datos de prueba
        int calificacion = 4;
        String titulo = "Título de Prueba";
        boolean estado = true;
        Date fechaInicio = new Date();

        // Crear el objeto de solicitud
        AsociacionSinFinRequest request = new AsociacionSinFinRequest();
        request.setEmpleado(empleado);
        request.setCurso(curso);
        request.setCalificacion(calificacion);
        request.setTitulo(titulo);
        request.setEstado(estado);
        request.setFechaInicio(fechaInicio);


        // Convertir objeto de solicitud a JSON
        String requestJson = objectMapper.writeValueAsString(request);

        // Realizar la solicitud POST y verificar la respuesta
        mockMvc.perform(post("/api/asociacion/asociarSinFin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Asociación creada exitosamente"));

        // Usar ArgumentCaptor para capturar los argumentos
        ArgumentCaptor<Empleado> empleadoCaptor = forClass(Empleado.class);
        ArgumentCaptor<Curso> cursoCaptor = forClass(Curso.class);
        ArgumentCaptor<Integer> calificacionCaptor = forClass(Integer.class);
        ArgumentCaptor<String> tituloCaptor = forClass(String.class);
        ArgumentCaptor<Boolean> estadoCaptor = forClass(Boolean.class);
        ArgumentCaptor<Date> fechaInicioCaptor = forClass(Date.class);


        verify(asociacionServicio, times(1)).asociarCurso(
                empleadoCaptor.capture(),
                cursoCaptor.capture(),
                calificacionCaptor.capture(),
                tituloCaptor.capture(),
                estadoCaptor.capture(),
                fechaInicioCaptor.capture()
        );

        // Verificar que los valores de los argumentos capturados son los esperados
        assertEquals(empleado.getIdEmpleado(), empleadoCaptor.getValue().getIdEmpleado());
        assertEquals(curso.getIdCurso(), cursoCaptor.getValue().getIdCurso());
        assertEquals(calificacion, calificacionCaptor.getValue().intValue());
        assertEquals(titulo, tituloCaptor.getValue());
        assertEquals(estado, estadoCaptor.getValue());
        assertEquals(fechaInicio, fechaInicioCaptor.getValue());


    }*/





}
