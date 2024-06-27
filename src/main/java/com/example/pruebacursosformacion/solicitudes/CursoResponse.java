package com.example.pruebacursosformacion.solicitudes;

import com.example.pruebacursosformacion.entidades.Tipo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoResponse {
    private long idCurso;
    private String nombreCurso;
    private String proveedor;
    private String urlCurso;
    private Tipo tipoCurso;
    private double clasificacionFinal;
    private Double precio;


}
