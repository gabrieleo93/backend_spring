package com.example.pruebacursosformacion.controladores;

import com.example.pruebacursosformacion.configuracion.JwtUtil;
import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Tipo;
import com.example.pruebacursosformacion.servicios.CursoDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@WebMvcTest(controllers = CursoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(JwtUtil.class)
public class CursoControllerTest2 {
    @Mock
    private CursoDAO cursoDAO;

    @InjectMocks
    private CursoController cursoController;

    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Curso curso;


    private List<Curso> cursos;

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
    public void testInsertarCurso2() throws Exception {

        String cursoJson = objectMapper.writeValueAsString(curso);

        when(cursoDAO.insertarActualizarCurso(any(Curso.class))).thenReturn(true);

        mockMvc = MockMvcBuilders.standaloneSetup(cursoController).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cursos/insertar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cursoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));

        verify(cursoDAO, times(1)).insertarActualizarCurso(any(Curso.class));

    }

}
